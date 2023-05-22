package com.jkr.modules.sys.file.service;

import com.jkr.core.base.service.BaseService;
import com.jkr.modules.sys.file.model.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author jikeruan
 * @since 2019-09-18
 */
public interface IFileInfoService extends BaseService<FileInfo> {

    /**
     * 上传文件
     *
     * @param file
     * @return
     * @throws Exception
     */
    FileInfo uploadFile(MultipartFile file) throws Exception;

    /**
     * 删除文件
     *
     * @param id
     */
    void deleteFile(String id);

    /**
     * 下载文件
     *
     * @param path
     * @return
     */
    InputStream downloadFile(String path);

    /**
     * method_name: downloadFileById
     * create_user: zhaosongming
     * create_date: 2020-06-12
     * create_time: 08:55:46
     * describe: 根据id下载文件
     * param : [response, fileId]
     * return : void
     */
    InputStream downloadFileById(String fileId) throws Exception;

    /**
     * method_name: downloadFileAll
     * create_user: zhaosongming
     * create_date: 2020-06-12
     * create_time: 11:19:01
     * describe: 多文件以压缩包下载
     * param : [fileList] 文件id及文件名称集合
     * return : void
     */
    void downloadFileAll(HttpServletResponse response, String[] fileIds, String[] fileNames) throws Exception;

    /**
     * method_name: findByIds
     * create_user: liangzhongyuan
     * create_date: 2021/6/1
     * create_time: 11:49
     * describe: 根据文件ids查询文件
     * param: [ids]
     * return: java.util.List<com.jkr.modules.file.model.FileInfo>
     * @param ids
     */
    List<FileInfo> findByIds(String[] ids);

    /**
     * method_name: deleteByIds
     * create_user: liangzhongyuan
     * create_date: 2021/6/2
     * create_time: 14:25
     * describe: 批量删除文件
     * param: [delIds]
     * return: void
     */
    void deleteByIds(List<String> delIds);

}
