package com.jkr.modules.sys.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.jkr.common.context.BaseContextHandler;
import com.jkr.core.base.service.impl.BaseServiceImpl;
import com.jkr.modules.sys.file.mapper.FileInfoMapper;
import com.jkr.modules.sys.file.model.FileInfo;
import com.jkr.modules.sys.file.service.IFileInfoService;
import com.jkr.utils.PDFUtil;
import org.apache.commons.io.FilenameUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Author：jikeruan
 * @Description:
 * @Date: 2019/12/17 9:03
 */
@Service
public class FileInfoServiceImpl extends BaseServiceImpl<FileInfoMapper, FileInfo> implements IFileInfoService {

    @Autowired
    private FastFileStorageClient storageClient;

    @Value("${fdfs.web-url}")
    private String fdfsWebUrl;

    @Value("${fdfs.storageServerIp}")
    private String storageServerIp;

    @Value("${fdfs.storageServerPort}")
    private String storageServerPort;

    @Value("${fdfs.mx}")
    private String mx;

    @Override
    public FileInfo uploadFile(MultipartFile file) throws Exception {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(file.getOriginalFilename());
        fileInfo.setFileType(file.getContentType());
        fileInfo.setFileSize(file.getSize());
        fileInfo.setCreateBy(BaseContextHandler.getUserID());
        fileInfo.setCreateDate(LocalDateTime.now());
        uploadFile(file, fileInfo);
        // 将文件信息保存到数据库
        baseMapper.insert(fileInfo);
        return fileInfo;
    }

    private StorageServer getStorageServer(String storageIp) {
        StorageServer storageServer = null;
        if (storageIp != null && !("").equals(storageIp)) {
            try {
                storageServer = new StorageServer(storageServerIp, Integer.parseInt(storageServerPort), Integer.parseInt(mx));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return storageServer;
    }

    /**
     * @param @param  inputStream 文件流
     * @param @param  filename 文件名称
     * @param @return
     * @param @throws IOException
     * @param @throws MyException
     * @return String 返回文件在FastDfs的存储路径
     * @throws
     * @Title: fdfsUpload
     * @Description: 通过文件流上传文件
     */
    public String fdfsUpload(InputStream inputStream, String filename) throws IOException, MyException {
        try {
            ClientGlobal.init("fdfs_client.co" +
                    "nf");
        } catch (MyException e) {
            e.printStackTrace();
        }
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageServer storageServer = this.getStorageServer(storageServerIp);
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);
        //后缀名
        String suffix;
        try {
            suffix = filename.substring(filename.lastIndexOf(".") + 1);
        } catch (Exception e) {
            throw new RuntimeException("参数filename不正确!格式例如：a.png");
        }
        //FastDfs的存储路径
        StringBuilder savePath = new StringBuilder();
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buff)) != -1) {
            swapStream.write(buff, 0, len);
        }
        byte[] in2b = swapStream.toByteArray();
        //上传文件
        String[] strings = storageClient.upload_file(in2b, suffix, null);
        for (String str : strings) {
            //拼接路径
            savePath.append("/").append(str);
        }
        return savePath.toString();
    }

    private void uploadFile(MultipartFile file, FileInfo fileInfo) throws IOException {
        String storePath;
        try {
            storePath = fdfsUpload(file.getInputStream(), FilenameUtils.getExtension(file.getOriginalFilename()));
            fileInfo.setFileUrl(fdfsWebUrl + "/" + storePath);
            fileInfo.setFilePath(storePath);
            fileInfo.setUrl(fileInfo.getFileUrl());
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteFile(String id) {
        FileInfo fileInfo = baseMapper.selectById(id);
        if (fileInfo != null) {
            baseMapper.deleteById(fileInfo.getId());
            StorePath storePath = StorePath.parseFromUrl(fileInfo.getFilePath());
            storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
        }
    }

    @Override
    public InputStream downloadFile(String path) {
        StorePath storePath = StorePath.parseFromUrl(path);
        return storageClient.downloadFile(storePath.getGroup(), storePath.getPath(), ins -> ins);
    }

    /**
     * method_name: downloadFileById
     * create_user: zhaosongming
     * create_date: 2020-06-12
     * create_time: 08:55:46
     * describe: 根据id下载文件
     * param : [response, fileId]
     * return : void
     */
    @Override
    public InputStream downloadFileById(String fileId) {
        FileInfo fileInfo = baseMapper.selectById(fileId);
        StorePath storePath = StorePath.parseFromUrl(fileInfo.getFilePath());
        return storageClient.downloadFile(storePath.getGroup(), storePath.getPath(), ins -> ins);
    }

    /**
     * method_name: downloadFileAll
     * create_user: zhaosongming
     * create_date: 2020-06-12
     * create_time: 11:19:01
     * describe: 多文件以压缩包下载
     * param : [fileList] 文件id及文件名称集合
     * return : void
     */
    @Override
    public void downloadFileAll(HttpServletResponse response, String[] fileIds, String[] fileNames) throws Exception {

        ZipOutputStream zipStream;
        BufferedInputStream bufferStream = null;
        StringBuilder sbr;
        File zipFile;
        String strZipPath;
        String zipFileName = null;

        // 获取当前系统临时文件夹路径
        String directory = PDFUtil.createTempPath();
        // 创建临时文件夹
        File directoryFile = new File(directory);
        if (!directoryFile.isDirectory() && !directoryFile.exists()) {
            directoryFile.mkdirs();
        }
        //设置最终输出zip文件的目录+文件名
        if (fileNames.length > 0) {
            for (int i = 0; i < fileNames.length; i++) {
                fileNames[i] = URLDecoder.decode(fileNames[i], "utf-8");
            }
            zipFileName = fileNames[0] + "等文件.zip";
        }
        strZipPath = directory + "/" + zipFileName;
        sbr = new StringBuilder();
        zipFile = new File(strZipPath);

        // 循环处理同名文件
        int num = 1;
        for (int i = 0; i < fileNames.length; i++) {
            String fileName = fileNames[i];
            for (int j = i + 1; j < fileNames.length; j++) {
                if (fileName.equals(fileNames[j])) {
                    String name = fileNames[j].substring(0, fileNames[j].lastIndexOf("."));
                    String fileType = fileNames[j].substring(fileNames[j].lastIndexOf("."));
                    fileNames[j] = sbr.append(name).append("（").append(num).append("）").append(fileType).toString();
                    num++;
                }
            }
            num = 1;
        }

        //构造最终压缩包的输出流
        zipStream = new ZipOutputStream(new FileOutputStream(zipFile));
        for (int i = 0; i < fileIds.length; i++) {
            InputStream inputStream = downloadFileById(fileIds[i]);
            /**
             * 压缩条目不是具体独立的文件，而是压缩包文件列表中的列表项，称为条目，就像索引一样这里的name就是文件名,
             * 文件名和之前的重复就会导致文件被覆盖
             */
            // 在压缩目录中文件的名字
            ZipEntry zipEntry = new ZipEntry(fileNames[i]);
            // 定位该压缩条目位置，开始写入文件到压缩包中
            zipStream.putNextEntry(zipEntry);
            bufferStream = new BufferedInputStream(inputStream, 1024 * 10);
            int read = 0;
            byte[] buf = new byte[1024 * 10];
            while ((read = bufferStream.read(buf, 0, 1024 * 10)) != -1) {
                zipStream.write(buf, 0, read);
            }
        }
        //关闭流
        if (null != bufferStream) {
            bufferStream.close();
        }
        if (null != zipStream) {
            zipStream.flush();
            zipStream.close();
        }
        //判断系统压缩文件是否存在：true-把该压缩文件通过流输出给客户端后删除该压缩文件  false-未处理
        if (zipFile.exists()) {
            downZip(response, zipFileName, strZipPath);
            if (zipFile.exists()) {
                zipFile.delete();
            }
            if (directoryFile.exists()) {
                directoryFile.delete();
            }
        }

    }

    /**
     * method_name: downZip
     * create_user: zhaosongming
     * create_date: 2020-06-15
     * create_time: 08:08:59
     * describe: 多文件zip下载
     * param : [response, filename, path]
     * return : void
     */
    public void downZip(HttpServletResponse response, String fileName, String path) throws Exception {
        if (fileName != null) {
            FileInputStream is;
            BufferedInputStream bs;
            OutputStream os;

            File file = new File(path);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            is = new FileInputStream(file);
            bs = new BufferedInputStream(is);
            os = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bs.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }

            // 关闭流
            if (os != null) {
                os.flush();
                os.close();
            }
            bs.close();
            is.close();
        }
    }

    @Override
    public List<FileInfo> findByIds(String[] ids) {
        return baseMapper.selectList(new QueryWrapper<FileInfo>().in("id", ids));
    }

    @Override
    public void deleteByIds(List<String> delIds) {
        baseMapper.deleteBatchIds(delIds);
    }

}
