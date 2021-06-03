package com.cxnet.project.system.file.fileUpload.service;

import java.util.List;

import com.cxnet.common.config.BaseConfig;
import com.cxnet.common.utils.DateUtils;
import com.cxnet.common.utils.file.FileUtils;
import com.cxnet.framework.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cxnet.project.system.file.fileUpload.domain.FileUpload;
import com.cxnet.project.system.file.fileUpload.mapper.FileUploadMapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * 文件上传Service业务层处理
 *
 * @author cxnet
 * @date 2020-07-24
 */
@Service
public class FileUploadServiceImpl implements FileUploadServiceI {
    @Autowired(required = false)
    private FileUploadMapper fileUploadMapper;

    /**
     * 查询文件上传
     *
     * @param fileId 文件上传ID
     * @return 文件上传
     */
    @Override
    public FileUpload selectFileUploadById(String fileId) {
        return fileUploadMapper.selectFileUploadById(fileId);
    }

    /**
     * 查询文件上传 ByFileName
     *
     * @param fileName 文件上传名称
     * @return 文件上传
     */
    @Override
    public FileUpload selectFileUploadByFileName(String fileName) {
        return fileUploadMapper.selectFileUploadByFileName(fileName);
    }

    /**
     * 查询文件上传集合
     *
     * @param fileUpload 文件上传
     * @return 文件上传
     */
    @Override
    public List<FileUpload> selectFileUploadList(FileUpload fileUpload) {
        return fileUploadMapper.selectFileUploadList(fileUpload);
    }

    /**
     * 新增文件上传
     *
     * @param fileUpload 文件上传
     * @return 结果
     */
    @Override
    public int insertFileUpload(FileUpload fileUpload) {
        fileUpload.setCreateTime(DateUtils.getNowDate());
        fileUpload.setCreateBy(SecurityUtils.getUsername());
        return fileUploadMapper.insertFileUpload(fileUpload);
    }

    /**
     * 批量新增文件上传
     *
     * @param fileUploads 文件上传
     * @return 结果
     */
    @Override
    public int insertBatchFileUpload(List<FileUpload> fileUploads) {
        return fileUploadMapper.insertBatchFileUpload(fileUploads);
    }

    /**
     * 修改文件上传
     *
     * @param fileUpload 文件上传
     * @return 结果
     */
    @Override
    public int updateFileUpload(FileUpload fileUpload) {
        return fileUploadMapper.updateFileUpload(fileUpload);
    }

    /**
     * 批量修改文件上传
     *
     * @param fileUploads 文件上传
     * @return 结果
     */
    @Override
    public int updateBatchFileUpload(List<FileUpload> fileUploads) {
        return fileUploadMapper.updateBatchFileUpload(fileUploads);
    }

    /**
     * 批量删除文件上传
     *
     * @param fileIds 需要删除的文件上传ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteFileUploadByIds(String[] fileIds) {
        String[] names = fileUploadMapper.selectFileNamesByFileIds(fileIds);
        for (String fileName : names) {
            String filePath = BaseConfig.getUploadPath() + fileName;
            FileUtils.deleteFile(filePath);
        }
        return fileUploadMapper.deleteFileUploadByIds(fileIds);
    }

    /**
     * 删除文件上传信息
     *
     * @param fileId 文件上传ID
     * @return 结果
     */
    @Override
    public int deleteFileUploadById(String fileId) {
        return fileUploadMapper.deleteFileUploadById(fileId);
    }


    /**
     * 删除文件上传
     *
     * @param fileName 文件上传名称
     * @return 结果
     */
    @Override
    public int deleteFileUploadByFileName(String fileName) {
        return fileUploadMapper.deleteFileUploadByFileName(fileName);
    }

    /**
     * 查询文件上传 Id
     */
    @Override
    public String selectFileId() {
        return fileUploadMapper.selectFileId();
    }


    /**
     * 查询文件上传名称集合 ByFileIds
     *
     * @param ids
     */
    @Override
    public String[] selectFileNamesByFileIds(String[] ids) {
        return fileUploadMapper.selectFileNamesByFileIds(ids);
    }

    @Override
    public List<FileUpload> selectFileList(List<String> fileId) {
        return fileUploadMapper.selectFileList(fileId);
    }

    @Override
    public FileUpload selectFileUploadByFileId(String fileId) {
        return fileUploadMapper.selectFileUploadByFileId(fileId);
    }
}
