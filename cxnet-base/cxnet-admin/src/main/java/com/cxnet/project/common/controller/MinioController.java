package com.cxnet.project.common.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import com.cxnet.common.config.BaseConfig;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.constant.Constants;
import com.cxnet.common.utils.QRCodeUtils;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.common.utils.file.FileUploadUtils;
import com.cxnet.common.utils.file.FileUtils;
import com.cxnet.framework.config.ServerConfig;
import com.cxnet.framework.minio.MinioBucketConstants;
import com.cxnet.framework.minio.MinioUtil;
import com.cxnet.framework.redis.RedisUtil;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.project.common.domain.CacheReqData;
import com.cxnet.project.system.file.fileUpload.domain.FileUpload;
import com.cxnet.project.system.file.fileUpload.service.FileUploadServiceI;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 通用请求处理
 *
 * @author cxnet
 */
@RestController
@RequestMapping("/minio")
@Api(tags = "minio附件上传/下载")
public class MinioController {
    private static final Logger log = LoggerFactory.getLogger(MinioController.class);

    @Autowired(required = false)
    private FileUploadServiceI fileUploadService;

    @Autowired(required = false)
    private ServerConfig serverConfig;

    @Autowired(required = false)
    private RedisUtil redisUtil;

    @Autowired(required = false)
    private MinioUtil minioUtil;

    @Value("${cxnet.name}")
    private String cxnetName;


    @ApiOperation("查询系统名称")
    @GetMapping("/getServerName")
    public String getServerName() {
        return cxnetName;
    }

    /**
     * minio文件资源删除
     *
     * @param fileName 文件名称
     */
    @ApiOperation("minio文件资源删除")
    @DeleteMapping("/delete/resource")
    public AjaxResult resourceDelete(String bucketName, String fileName) {
        //目前通过发票系统测试minio文件服务
        bucketName = MinioBucketConstants.INVOICE;
        //调用minio删除附件方法 判断桶是否存在
        if (minioUtil.bucketExists(bucketName)) {
            //判断文件是否存在
            if (minioUtil.getFileUrl(bucketName, fileName) != null || StringUtils.isNotEmpty(minioUtil.getFileUrl(bucketName, fileName))) {
                minioUtil.deleteFile(bucketName, fileName);
            }
        }
        return AjaxResult.success();
    }


    /**
     * minio文件资源下载
     *
     * @param fileName 文件名称
     */
    @ApiOperation("minio文件资源下载")
    @GetMapping("/download/resource")
    public void resourceDownload(String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //目前通过发票系统测试minio文件服务
        String bucketName = MinioBucketConstants.INVOICE;
        if (minioUtil.bucketExists(bucketName)) {
            //判断文件是否存在
            if (minioUtil.getFileUrl(bucketName, fileName) != null || StringUtils.isNotEmpty(minioUtil.getFileUrl(bucketName, fileName))) {
                minioUtil.download(bucketName, fileName, response);
                FileUpload fileUpload = fileUploadService.selectFileUploadByFileName(fileName);
                if (fileUpload == null) {
                    log.info("本地资源不存在");
                    return;
                }
                // 下载次数
                fileUpload.setDownCount(fileUpload.getDownCount() + 1);
                fileUploadService.updateFileUpload(fileUpload);
            } else {
                log.info("文件资源不存在");
                return;
            }

        }
    }

    /**
     * 获取多个上传文件
     *
     * @param fileIds 上传文件id数组
     */
    @ApiOperation("获取多个上传文件")
    @PostMapping("/file")
    public AjaxResult getBatchFile(@RequestBody List<String> fileIds) throws IOException {
        // 去除空数据
        if (CollectionUtil.isNotEmpty(fileIds)) {
            fileIds = fileIds.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
        }
        List<FileUpload> fileList = fileUploadService.selectFileList(fileIds);
        return AjaxResult.success(fileList);
    }


    /**
     * 通用上传请求
     *
     * @param file 上传文件
     */
    @ApiOperation("minio上传请求")
    @PostMapping("/upload")
    public AjaxResult uploadFile(MultipartFile file) {
        AjaxResult ajax = AjaxResult.success();
        try {
//            String fileName = FileUploadUtils.upload(BaseConfig.getUploadPath(), file);
            String bucketName = MinioBucketConstants.INVOICE;
            if (minioUtil.bucketExists(bucketName)) {
                String url = minioUtil.upload(bucketName, file);
                FileUpload fileUpload = new FileUpload();
                fileUpload.setFileType(file.getContentType());
                //String filename = file.getResource().getFilename();
                //对上传的文件名称进行编码
                String filename = FileUploadUtils.extractFilename(file);
                String originName = file.getOriginalFilename();
                fileUpload.setRealName(originName);
                fileUpload.setFileSuffix(filename.substring(filename.lastIndexOf(".") + 1));
                fileUpload.setFileSize((file.getSize()));
                fileUpload.setFileName(filename);

                fileUpload.setFileUrl(url);

                fileUpload.setCreateBy(SecurityUtils.getUsername());
                fileUpload.setCreateName(SecurityUtils.getLoginUser().getUser().getNickName());
                fileUploadService.insertFileUpload(fileUpload);
                ajax.put("fileId", fileUpload.getFileId());
                ajax.put("fileName", filename);
                ajax.put("realName", originName);
                ajax.put("fileUpload", fileUpload);
                ajax.put("url", url);
                return ajax;
            } else {
                minioUtil.createBucket("invoice");
            }

        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
        return ajax;
    }

    /**
     * 通过key存储redis缓存
     *
     * @param reqData key value
     */
    @ApiOperation("通过key存储redis缓存")
    @PostMapping("/setRedisCache")
    public AjaxResult setRedisCache(@Validated @RequestBody CacheReqData reqData) {
        redisUtil.set(reqData.getKey(), reqData.getValue());
        return AjaxResult.success();
    }


    /**
     * 通过key获取redis缓存
     *
     * @param key 键
     */
    @ApiOperation("通过key获取redis缓存")
    @GetMapping("/getRedisCache/{key}")
    public AjaxResult getRedisCache(@PathVariable String key) {
        return AjaxResult.success(redisUtil.get(key));
    }


    /**
     * zxing方式生成二维码
     * 注意：
     * 1,文本生成二维码的方法独立出来,返回image流的形式,可以输出到页面
     * 2,设置容错率为最高,一般容错率越高,图片越不清晰, 但是只有将容错率设置高一点才能兼容logo图片
     * 3,logo图片默认占二维码图片的20%,设置太大会导致无法解析
     *
     * @param content 二维码包含的内容，文本或网址
     * @param size    生成的二维码图片尺寸 可以自定义或者默认（250）
     */
    @ApiOperation("生成二维码")
    @GetMapping("/zxingCodeCreate")
    public AjaxResult zxingCodeCreate(String content, int size) {
        try {
            String fileName = QRCodeUtils.zxingCodeCreate(content, size);
            String url = serverConfig.getUrl() + fileName;
            FileUpload fileUpload = new FileUpload();
            fileUpload.setFileSuffix("jpg");
            fileUpload.setFileName(fileName);
            fileUpload.setRealName(fileName);
            fileUpload.setFileUrl(url);
            fileUpload.setFileSize(Long.valueOf(size));
            fileUpload.setCreateBy(SecurityUtils.getUsername());
            fileUploadService.insertFileUpload(fileUpload);
            AjaxResult ajax = AjaxResult.success();
            ajax.put("fileId", fileUpload.getFileId());
            ajax.put("fileName", fileName);
            ajax.put("url", url);
            return ajax;
        } catch (IOException e) {
            log.error(e.getMessage());
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 二维码的解析方法
     *
     * @param fileName 二维码图片路径名称
     */
    @ApiOperation("解析二维码")
    @GetMapping("/zxingCodeAnalyze")
    public AjaxResult zxingCodeAnalyze(String fileName) {
        return AjaxResult.success(QRCodeUtils.zxingCodeAnalyze(fileName));
    }

}