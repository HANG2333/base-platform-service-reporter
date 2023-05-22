package com.jkr.modules.sys.file.controller;

import cn.hutool.core.io.IoUtil;
import com.jkr.common.annotation.WebLog;
import com.jkr.common.model.ResponseData;
import com.jkr.modules.sys.file.model.FileInfo;
import com.jkr.modules.sys.file.service.IFileInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

/**
 * @Author：jikeruan
 * @Description:
 * @Date: 2019/9/18 14:02
 */
@RestController
@RequestMapping("/file")
@Api(value = "FileController", tags = {"文件操作接口"})
public class FileController {

    @Autowired
    private IFileInfoService iFileInfoService;

    /**
     * 文件上传
     * 根据fileType选择上传方式
     *
     * @param file
     * @return
     * @throws Exception
     */
    @WebLog(description = "文件上传-根据fileType选择上传方式")
    @PostMapping("/upload")
    public ResponseData<FileInfo> upload(@RequestParam("file") MultipartFile file) {
        FileInfo fileInfo = null;
        try {
            fileInfo = iFileInfoService.uploadFile(file);
        } catch (Exception exception) {
            return ResponseData.error(exception.getMessage());
        }
        return ResponseData.success(fileInfo);
    }

    /**
     * 文件删除
     *
     * @param id
     */
    @ApiOperation(value = "文件删除", notes = "文件删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文件id", dataType = "String", paramType = "query")
    })
    @WebLog(description = "文件删除")
    @PostMapping("/delete")
    public ResponseData<Boolean> delete(String id) {
        iFileInfoService.deleteFile(id);
        return ResponseData.success(true);
    }

    /**
     * 文件下载
     *
     * @param path
     * @param name
     * @param response
     * @throws Exception
     */
    @WebLog(description = "文件下载")
    @GetMapping("/download")
    public void download(@RequestParam("fileUrl") String path, String name, HttpServletResponse response) throws Exception {
        InputStream inputStream = iFileInfoService.downloadFile(path);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(name + ".pdf", "UTF-8"));
        // 写出
        ServletOutputStream outputStream = response.getOutputStream();
        IoUtil.copy(inputStream, outputStream);
    }

    /**
     * method_name:
     * create_user: jikeruan
     * create_date: 2020/6/4
     * create_time: 13:18
     * describe: 多文件一次上传
     * param :
     * return :
     */
    @WebLog(description = "多文件一次上传")
    @PostMapping("/uploadFile")
    @ResponseBody
    public ResponseData<String> uploadFile(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultiValueMap<String, MultipartFile> multiFileMap = multipartRequest.getMultiFileMap();
        List<MultipartFile> files = multiFileMap.get("files");
        StringBuilder ids = new StringBuilder();
        for (int i = 0; i < files.size(); i++) {
            try {
                if (ids.length() > 0) {
                    ids.append(",").append(iFileInfoService.uploadFile(files.get(i)).getId());
                } else {
                    ids = new StringBuilder(iFileInfoService.uploadFile(files.get(i)).getId());
                }
            } catch (Exception e) {
                return ResponseData.error(e.getMessage());
            }
        }
        return ResponseData.success(ids.toString());
    }

    /**
     * method_name: downloadFileById
     * create_user: zhaosongming
     * create_date: 2020-06-12
     * create_time: 08:55:46
     * describe: 据id下载文件
     * param : [response, fileId]
     * return : void
     */
    @WebLog(description = "据id下载文件")
    @GetMapping("downloadFileById")
    public void downloadFileById(HttpServletResponse response, String fileId, String fileName) {
        try {
            InputStream inputStream = iFileInfoService.downloadFileById(fileId);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            // 写出
            ServletOutputStream outputStream = response.getOutputStream();
            IoUtil.copy(inputStream, outputStream);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
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
    @WebLog(description = "多文件以压缩包下载")
    @GetMapping("downloadFileAll")
    public void downloadFileAll(HttpServletResponse response, String[] fileIds, String[] fileNames) throws Exception {
        iFileInfoService.downloadFileAll(response, fileIds, fileNames);
    }

    /**
     * method_name:downloadFileOnlyById
     * create_user: DaiFuyou
     * create_date:2020/9/3
     * create_time:10:44
     * describe: 只根据文件ID下载文件
     * param:[response, fileId]
     * return:void
     */
    @WebLog(description = "只根据文件ID下载文件")
    @GetMapping("downloadFileOnlyById")
    public void downloadFileOnlyById(HttpServletResponse response, String fileId) {
        String name = "";
        String path = "";
        FileInfo fileInfo = iFileInfoService.getById(fileId);
        if (null != fileInfo) {
            name = fileInfo.getFileName();
            path = fileInfo.getFileUrl();
        }
        InputStream inputStream = iFileInfoService.downloadFile(path);
        response.setCharacterEncoding("UTF-8");
        try {
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(name, "UTF-8"));
            // 写出
            ServletOutputStream outputStream = response.getOutputStream();
            IoUtil.copy(inputStream, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @WebLog(description = "只根据文件ID下载文件")
    @RequestMapping("/findByIds")
    public ResponseData<List<FileInfo>> findByIds(String ids) {
        String[] split = ids.split(",");
        List<String> strings = Arrays.asList(split);
        return ResponseData.success(iFileInfoService.listByIds(strings));
    }

    /**
     * method_name:deleteById
     * create_user: YZL
     * create_date: 2021/3/6
     * create_time: 13:57
     * describe: 删除
     *
     * @param id
     * @eturn com.jkr.common.model.ResponseData
     */
    @WebLog(description = "删除")
    @RequestMapping("/deleteById")
    public ResponseData<Boolean> deleteById(String id) {
        iFileInfoService.deleteFile(id);
        return ResponseData.success(true);
    }

}
