package com.cxnet.project.system.dict.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cxnet.common.core.text.Convert;
import com.cxnet.common.utils.TreeUtil;
import com.cxnet.project.assets.data.DataMgr;
import com.cxnet.project.assets.data.cache.CacheKey;
import com.cxnet.project.assets.data.cache.CacheMgr;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.common.utils.poi.ExcelUtil;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.framework.web.controller.BaseController;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.project.system.dict.domain.SysDictData;
import com.cxnet.project.system.dict.service.SysDictDataServiceI;

import static com.cxnet.common.constant.AjaxResult.error;
import static com.cxnet.common.constant.AjaxResult.success;

/**
 * 数据字典信息
 *
 * @author cxnet
 */
@Slf4j
@RestController
@Api(tags = "数据字典")
@RequestMapping("/system/dict/data")
public class SysDictDataController extends BaseController {
    @Autowired(required = false)
    private SysDictDataServiceI dictDataService;

    @ApiOperation("获取字典数据列表")
    @GetMapping
    public AjaxResult list(SysDictData dictData) {
        return success(getCacheDataTable(CacheKey.CacheDictData.name(), Convert.entityToMap(dictData), new HashMap<String, String>(1) {{
            put("dictSort", "desc");
        }}));
    }

    @ApiOperation("导出字典数据")
    @Log(title = "字典数据", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:dict:export')")
    @GetMapping("/export")
    public AjaxResult export(SysDictData dictData) {
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        ExcelUtil<SysDictData> util = new ExcelUtil<>(SysDictData.class);
        return util.exportExcel(list, "字典数据");
    }

    /**
     * 查询字典数据详细
     */
    @ApiOperation("查询字典数据详细")
    @GetMapping(value = "/{dictCode}")
    public AjaxResult getInfo(@PathVariable String dictCode) {
        try {
            Object result = new CacheMgr().getCache(CacheKey.CacheDictData.name(), dictCode);
            if (result != null) {
                return success(result);
            }
        } catch (Exception e) {
            log.error("Catch exception infoCacheDictData error:" + e.toString());
        }
        return error();
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @ApiOperation("根据字典类型查询字典数据信息")
    @GetMapping(value = "/dictType/{dictType}")
    public AjaxResult dictType(@PathVariable String dictType) {
        try {
            Object result = new CacheMgr().getCache(CacheKey.CacheDictData.name(), new HashMap<String, String>(1) {{
                put("dictType", dictType);
            }}, new HashMap<String, String>(1) {{
                put("dictSort", "asc");
            }});
            if (result != null) {
                List<SysDictData> sysDictData = (List<SysDictData>) result;
                List<SysDictData> collect = sysDictData.stream().filter(v -> "0".equals(v.getStatus())).collect(Collectors.toList());
                return success(collect);
            }
        } catch (Exception e) {
            log.error("Catch exception getDictType error:" + e.toString());
        }
        return error();
    }

    /**
     * 根据字典类型查询所有
     */
    @ApiOperation("根据字典类型查询所有")
    @GetMapping(value = "/getDictDataAllByType/{dictType}")
    public AjaxResult getDictDataAllByType(@PathVariable String dictType, String searchValue) {
        try {
            List<SysDictData> sysDictData = dictDataService.getDictDataAllByType(dictType, searchValue);
            if (!sysDictData.isEmpty()) {
                return success(new TreeUtil().toTree(JSONArray.parseArray(JSON.toJSONString(sysDictData)), "dictCode", "parentId"));
            }
        } catch (Exception e) {
            log.error("Catch exception getDictDataTree error:" + e.toString());
        }
        return success(new ArrayList<>());
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @ApiOperation("根据字典类型查询字典数据信息")
    @GetMapping(value = "/getDictDataByType/{dictType}")
    public AjaxResult getDictDataByType(@PathVariable String dictType) {
        try {
            List<SysDictData> sysDictData = dictDataService.selectDictDataStatus(dictType);
            if (!sysDictData.isEmpty()) {
                return success(new TreeUtil().toTree(JSONArray.parseArray(JSON.toJSONString(sysDictData)), "dictCode", "parentId"));
            }
        } catch (Exception e) {
            log.error("Catch exception getDictDataTree error:" + e.toString());
        }
        return success(new ArrayList<>());
    }

    /**
     * 根据字典类型查询字典数据
     * 是树的话就以树的形式展示
     *
     * @param dictType 字典类型
     * @return
     */
    @ApiOperation("根据字典类型查询字典数据信息,是树就以树形显示")
    @GetMapping(value = "/dictDataTree/{dictType}")
    public AjaxResult dictDataTree(@PathVariable String dictType) {
        try {
            List<SysDictData> sysDictData = dictDataService.selectDictDataListTree(dictType);
            if (!sysDictData.isEmpty()) {
                return success(new TreeUtil().toTree(JSONArray.parseArray(JSON.toJSONString(sysDictData)), "dictCode", "parentId"));
            }
        } catch (Exception e) {
            log.error("Catch exception getDictDataTree error:" + e.toString());
        }
        return success(new ArrayList<>());
    }

    /**
     * 新增字典类型
     */
    @ApiOperation("新增字典类型")
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult add(@Validated @RequestBody SysDictData dict) {
        dict.setCreateBy(SecurityUtils.getUsername());
        if ("Y".equals(dict.getIsDefault())) {
            dictDataService.updateDefault(dict.getDictType());
        }
        dictDataService.insertDictData(dict);
        new CacheMgr().setCache(CacheKey.CacheDictData.name(), DataMgr.getDictData());
        return success();
    }

    /**
     * 修改保存字典类型
     */
    @ApiOperation("修改保存字典类型")
    @PreAuthorize("@ss.hasPermi('system:dict:update')")
    @Log(title = "字典数据", businessType = BusinessType.UPDATE)
    @PutMapping
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult update(@Validated @RequestBody SysDictData dict) {
        dict.setUpdateBy(SecurityUtils.getUsername());
        dictDataService.updateDefault(dict.getDictType());
        dictDataService.updateDictData(dict);
        new CacheMgr().setCache(CacheKey.CacheDictData.name(), DataMgr.getDictData());
        return success();
    }

    /**
     * 删除字典类型
     */
    @ApiOperation("删除字典类型")
    @PreAuthorize("@ss.hasPermi('system:dict:delete')")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictCodes}")
    public AjaxResult delete(@PathVariable String[] dictCodes) {
        dictDataService.deleteDictDataByIds(dictCodes);
        new CacheMgr().setCache(CacheKey.CacheDictData.name(), DataMgr.getDictData());
        return success();
    }
}
