package com.cxnet.project.system.file.fileUpload.mapper;

import com.cxnet.project.system.file.fileUpload.domain.FileUpload;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文件上传Mapper接口
 *
 * @author cxnet
 * @date 2020-07-24
 */
public interface FileUploadMapper {
    /**
     * 查询文件上传
     *
     * @param fileId 文件上传ID
     * @return 文件上传
     */
    public FileUpload selectFileUploadById(String fileId);

    /**
     * 查询文件上传 ByFileName
     *
     * @param fileName 文件上传名称
     * @return 文件上传
     */
    public FileUpload selectFileUploadByFileName(String fileName);

    /**
     * 查询文件上传集合
     *
     * @param fileUpload 文件上传
     * @return 文件上传集合
     */
    public List<FileUpload> selectFileUploadList(FileUpload fileUpload);

    /**
     * 新增文件上传
     *
     * @param fileUpload 文件上传
     * @return 结果
     */
    public int insertFileUpload(FileUpload fileUpload);

    /**
     * 批量新增文件上传
     *
     * @param fileUploads 文件上传
     * @return 结果
     */
    public int insertBatchFileUpload(List<FileUpload> fileUploads);

    /**
     * 修改文件上传
     *
     * @param fileUpload 文件上传
     * @return 结果
     */
    public int updateFileUpload(FileUpload fileUpload);

    /**
     * 批量修改文件上传
     *
     * @param fileUploads 文件上传
     * @return 结果
     */
    public int updateBatchFileUpload(List<FileUpload> fileUploads);

    /**
     * 删除文件上传
     *
     * @param fileId 文件上传ID
     * @return 结果
     */
    public int deleteFileUploadById(String fileId);

    /**
     * 批量删除文件上传
     *
     * @param fileIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteFileUploadByIds(String[] fileIds);

    /**
     * 删除文件上传
     *
     * @param fileName 文件上传名称
     * @return 结果
     */
    public int deleteFileUploadByFileName(String fileName);


    /**
     * 查询文件上传 Id
     */
    public String selectFileId();

    /**
     * 查询文件上传名称集合 ByFileIds
     */
    public String[] selectFileNamesByFileIds(String[] ids);

    List<FileUpload> selectFileList(List<String> fileId);

    FileUpload selectFileUploadByFileId(@Param("fileId") String fileId);
}
