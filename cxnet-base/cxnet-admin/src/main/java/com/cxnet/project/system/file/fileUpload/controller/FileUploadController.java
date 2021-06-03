package com.cxnet.project.system.file.fileUpload.controller;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.project.system.file.fileUpload.domain.FileUpload;
import com.cxnet.project.system.file.fileUpload.service.FileUploadServiceI;
import com.cxnet.framework.web.controller.BaseController;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.utils.poi.ExcelUtil;

import static com.cxnet.common.constant.AjaxResult.success;

/**
 * 文件上传控制层
 *
 * @author cxnet
 * @date 2020-07-24
 */
@Slf4j
@RestController
@Api(tags = "文件上传")
@RequestMapping("/file/fileUpload")
public class FileUploadController extends BaseController {

    @Autowired(required = false)
    private FileUploadServiceI fileUploadService;


    /**
     * 查询文件上传集合
     *
     * @param fileUpload 文件上传
     */
    @ApiOperation("查询文件上传集合")
    @PreAuthorize("@ss.hasPermi('file:fileUpload:query')")
    @GetMapping
    public AjaxResult listFileUpload(FileUpload fileUpload) {
        startPage();
        List<FileUpload> list = fileUploadService.selectFileUploadList(fileUpload);
        return success(getDataTable(list));
    }


    /**
     * 查询文件上传明细
     *
     * @param id 文件上传ID
     */
    @ApiOperation("查询文件上传明细")
    @PreAuthorize("@ss.hasPermi('file:fileUpload:query')")
    @GetMapping("/{id}")
    public AjaxResult getFileUpload(@PathVariable String id) {
        return success(fileUploadService.selectFileUploadById(id));
    }

    /**
     * 新增文件上传
     *
     * @param fileUpload 文件上传
     */
    @ApiOperation("新增文件上传")
    @PreAuthorize("@ss.hasPermi('file:fileUpload:add')")
    @Log(title = "文件上传", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult addFileUpload(@RequestBody FileUpload fileUpload) {
        return toAjax(fileUploadService.insertFileUpload(fileUpload));
    }

    /**
     * 修改文件上传
     *
     * @param fileUpload 文件上传
     */
    @ApiOperation("修改文件上传")
    @PreAuthorize("@ss.hasPermi('file:fileUpload:update')")
    @Log(title = "文件上传", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateFileUpload(@Validated @RequestBody FileUpload fileUpload) {
        return toAjax(fileUploadService.updateFileUpload(fileUpload));
    }

    /**
     * 删除文件上传
     *
     * @param id 文件上传ID
     */
    @ApiOperation("删除文件上传")
    @PreAuthorize("@ss.hasPermi('file:fileUpload:delete')")
    @Log(title = "文件上传", businessType = BusinessType.DELETE)
    @DeleteMapping()
    public AjaxResult deleteFileUpload(String[] id) {
        return toAjax(fileUploadService.deleteFileUploadByIds(id));
    }

    /**
     * 导出文件上传
     *
     * @param fileUpload 文件上传
     */
    @ApiOperation("导出文件上传")
    @PreAuthorize("@ss.hasPermi('file:fileUpload:export')")
    @Log(title = "文件上传", businessType = BusinessType.EXPORT)
    @PutMapping("/export")
    public AjaxResult exportFileUpload(FileUpload fileUpload) {
        List<FileUpload> list = fileUploadService.selectFileUploadList(fileUpload);
        ExcelUtil<FileUpload> util = new ExcelUtil<>(FileUpload.class);
        return util.exportExcel(list, "文件上传数据");
    }


}
