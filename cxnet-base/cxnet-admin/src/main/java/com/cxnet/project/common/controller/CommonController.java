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
import java.io.*;
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
@RequestMapping("/common")
@Api(tags = "通用请求")
public class CommonController {
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired(required = false)
    private FileUploadServiceI fileUploadService;

    @Autowired(required = false)
    private ServerConfig serverConfig;

    @Autowired(required = false)
    private RedisUtil redisUtil;

    @Value("${cxnet.name}")
    private String cxnetName;


    @ApiOperation("查询系统名称")
    @GetMapping("/getServerName")
    public String getServerName() {
        return cxnetName;
    }

    /**
     * 本地资源通用删除
     *
     * @param fileName 文件名称
     */
    @ApiOperation("本地资源通用删除")
    @DeleteMapping("/delete/resource")
    public AjaxResult resourceDelete(String fileName) {
        fileUploadService.deleteFileUploadByFileName(fileName);
        String filePath = BaseConfig.getProfile() + StringUtils.substringAfter(fileName, Constants.RESOURCE_PREFIX);
        if (FileUtil.exist(filePath)) {
            FileUtils.deleteFile(filePath);
        }
        return AjaxResult.success();
    }

    /**
     * 本地资源通用下载
     *
     * @param fileName 文件名称
     */
    @ApiOperation("本地资源通用下载")
    @GetMapping("/download/resource")
    public void resourceDownload(String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (fileName.startsWith("附件包-") && fileName.endsWith(".zip")) {
            // 本地资源路径
            String zipFilePath = BaseConfig.getUploadPath() + File.separator + fileName;
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, fileName));
            FileUtils.writeBytes(zipFilePath, response.getOutputStream());
            // 删除压缩包文件
            if (FileUtil.exist(zipFilePath)) {
                FileUtils.deleteFile(zipFilePath);
            }
            return;
        }

        FileUpload fileUpload = fileUploadService.selectFileUploadByFileName(fileName);
        if (fileUpload == null) {
            log.info("本地资源不存在");
            return;
        }
        // 本地资源路径
        String localPath = BaseConfig.getProfile();
        // 数据库资源地址
        String downloadPath = localPath + StringUtils.substringAfter(fileName, Constants.RESOURCE_PREFIX);
        // 下载名称
        String downloadName = fileUpload.getRealName();
        // 下载次数
        fileUpload.setDownCount(fileUpload.getDownCount() + 1);
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, downloadName));
        FileUtils.writeBytes(downloadPath, response.getOutputStream());
        fileUploadService.updateFileUpload(fileUpload);
    }

    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete   是否删除
     */
    @ApiOperation("通用下载请求")
    @GetMapping("/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (!FileUtils.isValidFilename(fileName)) {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = BaseConfig.getDownloadPath() + fileName;

            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition",
                    "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, realFileName));
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete && FileUtil.exist(filePath)) {
                FileUtils.deleteFile(filePath);
            }
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }


    /**
     * 单文件、批量下载通用请求
     *
     * @param fileIds 文件id集合
     * @return AjaxResult
     */
    @ApiOperation("批量下载请求")
    @PostMapping("/plLoads")
    public AjaxResult fileDownloads(@RequestBody List<String> fileIds) throws IOException {
        // 去除空数据
        if (CollectionUtil.isNotEmpty(fileIds)) {
            fileIds = fileIds.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
        }
        Map<String, String> map = new HashMap<>(2);
        /* 单文件 */
        if (fileIds.size() == 1) {
            FileUpload fileUpload = fileUploadService.selectFileUploadByFileId(fileIds.get(0));
            map.put("fileName", fileUpload.getFileName());
            map.put("name", fileUpload.getRealName());
            return AjaxResult.success("success", map);
            /* 批量文件打包 */
        } else if (fileIds.size() > 1) {
            List<FileUpload> fileList = fileUploadService.selectFileList(fileIds);
            // 创建压缩包
            String dates = DateFormatUtils.format(new Date(), "yyyyMMdd_S");
            String zipName = "附件包-" + dates + ".zip";
            String zipFilePath = BaseConfig.getUploadPath() + File.separator + zipName;
            byte[] buf = new byte[1024];
            File zipFile = new File(zipFilePath);
            // zip文件不存在，创建文件，用于压缩
            if (!zipFile.exists()) {
                zipFile.createNewFile();
            }
            FileInputStream fis = null;
            ZipOutputStream zos = null;
            try {
                zos = new ZipOutputStream(new FileOutputStream(zipFile));
                for (FileUpload file : fileList) {
                    String filePath = (BaseConfig.getDownloadsPath() + file.getFileName()).replace("/profile/", "");
                    log.info("filePath=======>{}", filePath);
                    if (StringUtils.isEmpty(filePath)) {
                        continue;
                    }
                    // 绝对路径找到file
                    File sourceFile = new File(filePath);
                    if (!sourceFile.exists()) {
                        continue;
                    }
                    // 防止重名文件
                    String realFileName = file.getRealName();
                    Long millis = System.currentTimeMillis();
                    String t = millis.toString().substring(9);
                    String name = realFileName.substring(0, realFileName.lastIndexOf("."));
                    String suffix = realFileName.substring(realFileName.lastIndexOf("."));
                    String finalName = name + "_" + t + suffix;
                    // 直接放到压缩包的根目录
                    zos.putNextEntry(new ZipEntry(finalName));
                    fis = new FileInputStream(sourceFile);
                    int len;
                    while ((len = fis.read(buf)) > 0) {
                        zos.write(buf, 0, len);
                    }
                    zos.closeEntry();
                    fis.close();
                    // 下载次数
                    file.setDownCount(file.getDownCount() + 1);
                    fileUploadService.updateFileUpload(file);
                }
                zos.close();
            } catch (Exception e) {
                log.error("错误原因:{}", e.getMessage(), e);
            } finally {
                if (fis != null) {
                    fis.close();
                }
                if (zos != null) {
                    zos.close();
                }
            }
            map.put("fileName", zipName);
            map.put("name", "");
            return AjaxResult.success("success", map);
        } else {
            return AjaxResult.error("没有文件可下载");
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
    @ApiOperation("通用上传请求")
    @PostMapping("/upload")
    public AjaxResult uploadFile(MultipartFile file) {
        try {
            String fileName = FileUploadUtils.upload(BaseConfig.getUploadPath(), file);
            String url = serverConfig.getUrl() + fileName;
            FileUpload fileUpload = new FileUpload();
            fileUpload.setFileType(file.getContentType());
            String filename = file.getResource().getFilename();
            String originName = file.getOriginalFilename();
            fileUpload.setRealName(originName);
            fileUpload.setFileSuffix(filename.substring(filename.lastIndexOf(".") + 1));
            fileUpload.setFileSize((file.getSize()));
            fileUpload.setFileName(fileName);
            fileUpload.setFileUrl(url);
            fileUpload.setCreateBy(SecurityUtils.getUsername());
            fileUpload.setCreateName(SecurityUtils.getLoginUser().getUser().getNickName());
            fileUploadService.insertFileUpload(fileUpload);
            AjaxResult ajax = AjaxResult.success();
            ajax.put("fileId", fileUpload.getFileId());
            ajax.put("fileName", fileName);
            ajax.put("realName", originName);
            ajax.put("fileUpload", fileUpload);
            ajax.put("url", url);
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 通用批量上传请求
     *
     * @param file 上传文件
     */
    @ApiOperation("通用批量上传请求")
    @PostMapping("/uploads")
    public AjaxResult uploadFiles(MultipartFile[] file) {
        try {
            List<FileUpload> fileUploads = new ArrayList<>();
            ArrayList fileIds = new ArrayList();
            ArrayList fileNames = new ArrayList();
            ArrayList urls = new ArrayList();
            for (MultipartFile nowFile : file) {
                String fileId = fileUploadService.selectFileId();
                String fileName = FileUploadUtils.upload(BaseConfig.getUploadPath(), nowFile);
                String url = serverConfig.getUrl() + fileName;
                FileUpload fileUpload = new FileUpload();
                fileUpload.setFileType(nowFile.getContentType());
                String filename = nowFile.getResource().getFilename();
                fileUpload.setRealName(filename);
                fileUpload.setFileSuffix(filename.substring(filename.lastIndexOf(".") + 1));
                fileUpload.setFileSize((nowFile.getSize()));
                fileUpload.setFileName(fileName);
                fileUpload.setFileUrl(url);
                fileUpload.setCreateBy(SecurityUtils.getUsername());
                fileUpload.setFileId(fileId);
                fileUploads.add(fileUpload);
                fileIds.add(fileId);
                fileNames.add(fileName);
                urls.add(url);
            }
            fileUploadService.insertBatchFileUpload(fileUploads);
            AjaxResult ajax = AjaxResult.success();
            ajax.put("fileIds", fileIds);
            ajax.put("fileNames", fileNames);
            ajax.put("urls", urls);
            return ajax;
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.error(e.getMessage());
        }
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