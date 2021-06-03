package com.cxnet.project.system.dict.controller;

import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cxnet.common.core.text.Convert;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.common.utils.TreeUtil;
import com.cxnet.common.utils.tree.Zone;
import com.cxnet.common.utils.tree.ZoneUtils;
import com.cxnet.project.assets.data.DataMgr;
import com.cxnet.project.assets.data.cache.CacheKey;
import com.cxnet.project.assets.data.cache.CacheMgr;
import com.cxnet.project.system.dict.domain.SysDictData;
import com.cxnet.project.system.dict.service.SysDictDataServiceI;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cxnet.common.constant.UserConstants;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.common.utils.poi.ExcelUtil;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.framework.web.controller.BaseController;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.project.system.dict.domain.SysDictType;
import com.cxnet.project.system.dict.service.SysDictTypeServiceI;

import static com.cxnet.common.constant.AjaxResult.error;
import static com.cxnet.common.constant.AjaxResult.success;

/**
 * 字典类型信息
 *
 * @author cxnet
 */
@Slf4j
@RestController
@Api(tags = "字典类型")
@RequestMapping("/system/dict/type")
public class SysDictTypeController extends BaseController {
    @Autowired(required = false)
    private SysDictTypeServiceI dictTypeService;
    @Autowired(required = false)
    private SysDictDataServiceI sysDictDataService;

    @ApiOperation("获取字典类型列表")
    @GetMapping
    public AjaxResult list(SysDictType dictType) {
        if (StringUtils.isNotEmpty(dictType.getSearchValue())) {
            startPage();
            return success(getDataTable(dictTypeService.selectDictTypeList(dictType)));
        } else {
            return success(getCacheDataTable(CacheKey.CacheDictType.name(), Convert.entityToMap(dictType), new HashMap<String, String>(1) {{
                put("createTime", "asc");
            }}));
        }
    }

    @ApiOperation("导出字典类型")
    @Log(title = "字典类型", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:dict:export')")
    @PutMapping("/export")
    public AjaxResult export(@RequestBody SysDictType dictType) {
        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
        ExcelUtil<SysDictType> util = new ExcelUtil<>(SysDictType.class);
        return util.exportExcel(list, "字典类型");
    }


    /**
     * 查询字典类型详细查询字典类型详细
     *
     * @param dictId 字典主键编号
     * @return
     */
    @ApiOperation("查询字典类型详细")
    @GetMapping(value = "/{dictId}")
    public AjaxResult getInfo(@PathVariable String dictId) {
        try {
            SysDictType sysDictType = dictTypeService.selectDictTypeById(dictId);
            Object result = new CacheMgr().getCache(CacheKey.CacheDictType.name(), dictId);
            if (result != null) {
                return success(result);
            }
        } catch (Exception e) {
            log.error("Catch exception infoCacheDictType error:" + e.toString());
        }
        return error();
    }

    /**
     * 新增字典类型
     */
    @ApiOperation("新增字典类型")
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @Log(title = "字典类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDictType dict) {
        if (UserConstants.NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict))) {
            return error("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setCreateBy(SecurityUtils.getUsername());
        dictTypeService.insertDictType(dict);
        new CacheMgr().setCache(CacheKey.CacheDictType.name(), DataMgr.getDictType());
        return success();
    }

    /**
     * 修改字典类型
     */
    @ApiOperation("修改字典类型")
    @PreAuthorize("@ss.hasPermi('system:dict:update')")
    @Log(title = "字典类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@Validated @RequestBody SysDictType dict) {
        if (UserConstants.NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict))) {
            return error("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setUpdateBy(SecurityUtils.getUsername());
        dictTypeService.updateDictType(dict);
        new CacheMgr().setCache(CacheKey.CacheDictType.name(), DataMgr.getDictType());
        return success();
    }

    /**
     * 删除字典类型
     */
    @ApiOperation("删除字典类型")
    @PreAuthorize("@ss.hasPermi('system:dict:delete')")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictIds}")
    public AjaxResult delete(@PathVariable String[] dictIds) {
        dictTypeService.deleteDictTypeByIds(dictIds);
        new CacheMgr().setCache(CacheKey.CacheDictType.name(), DataMgr.getDictType());
        return success();
    }

    /**
     * 获取字典选择框列表
     */
    @ApiOperation("获取字典选择框列表")
    @GetMapping("/optionselect")
    public AjaxResult optionselect() {
        List<SysDictType> dictTypes = dictTypeService.selectDictTypeAll();
        return success(dictTypes);
    }
}
