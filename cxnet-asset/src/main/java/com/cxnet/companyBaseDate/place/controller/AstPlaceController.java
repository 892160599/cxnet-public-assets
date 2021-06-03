package com.cxnet.companyBaseDate.place.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONString;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.companyBaseDate.place.domain.AstPlace;
import com.cxnet.companyBaseDate.place.domain.MapRange;
import com.cxnet.companyBaseDate.place.service.AstPlaceService;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.framework.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.cxnet.common.constant.AjaxResult.success;

/**
 * 存放地点(AstPlace)表控制层
 *
 * @author zhangyl
 * @since 2021-03-30 09:55:27
 */
@Slf4j
@Api(tags = "存放地点")
@RestController
@RequestMapping("/astPlace")
public class AstPlaceController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private AstPlaceService astPlaceService;


    /**
     * 查询存放地点tree
     *
     * @param bdExpfunc 存放地点
     */
    @ApiOperation("查询存放地点集合tree")
    @GetMapping("/tree")
    public AjaxResult treeastPlaceUser(AstPlace astPlace) {
        return success(astPlaceService.selectAstPlaceTree(astPlace));
    }


    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
//    @PreAuthorize("@ss.hasPermi('astPlace:astPlace:query')")
    @GetMapping("{id}")
    public AjaxResult selectOne(@PathVariable Serializable id) {
        QueryWrapper<AstPlace> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AstPlace::getDelFlag, "0").eq(AstPlace::getId, id);
        AstPlace one = astPlaceService.getOne(wrapper);
        String oneMapRange = one.getMapRange();
        //地图范围字符串转集合
        List<MapRange> mapRangeList = new ArrayList<>();
        if (StringUtils.isNotEmpty(oneMapRange)) {
            mapRangeList = com.alibaba.fastjson.JSONArray.parseArray(oneMapRange, MapRange.class);
        }
        one.setList(mapRangeList);
        return success(one);
    }

    /**
     * 新增数据
     *
     * @param astPlace 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
//    @PreAuthorize("@ss.hasPermi('astPlace:astPlace:insert')")
    @Log(title = "存放地点", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult insert(@RequestBody AstPlace astPlace) {
        QueryWrapper<AstPlace> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AstPlace::getPlaceCode, astPlace.getPlaceCode()).eq(AstPlace::getDelFlag, "0");
        List<AstPlace> list = astPlaceService.list(wrapper);
        if (list.size() > 0) {
            throw new CustomException("存放地点编码不可重复");
        }
        //地图范围集合转字符串
        List<MapRange> mapList = astPlace.getList();
        if (CollectionUtils.isNotEmpty(mapList)) {
            JSONArray json = new JSONArray();
            for (MapRange pLog : mapList) {
                JSONObject jo = new JSONObject();
                jo.put("lat", pLog.getLat());
                jo.put("lng", pLog.getLng());
                json.put(jo);
            }
            astPlace.setMapRange(json.toString());
        }
        return success(astPlaceService.save(astPlace));
    }

    /**
     * 修改数据
     *
     * @param astPlace 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
//    @PreAuthorize("@ss.hasPermi('astPlace:astPlace:update')")
    @Log(title = "存放地点", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@RequestBody AstPlace astPlace) {
        List<MapRange> mapList = astPlace.getList();
        if (CollectionUtils.isNotEmpty(mapList)) {
            JSONArray json = new JSONArray();
            for (MapRange pLog : mapList) {
                JSONObject jo = new JSONObject();
                jo.put("lng", pLog.getLng());
                jo.put("lat", pLog.getLat());
                json.put(jo);
            }
            astPlace.setMapRange(json.toString());
        }
        return success(astPlaceService.updateById(astPlace));
    }

    /**
     * 删除数据
     *
     * @param ids 主键结合
     * @return 删除结果
     */
    @ApiOperation("删除数据")
//    @PreAuthorize("@ss.hasPermi('astPlace:astPlace:delete')")
    @Log(title = "存放地点", businessType = BusinessType.DELETE)
    @GetMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable String id) {
        AstPlace byId = astPlaceService.getById(id);
        if ("00".equals(byId.getPlaceCode())) {
            throw new CustomException("存放地点不可删除");
        } else {
            byId.setDelFlag("2");
            astPlaceService.saveOrUpdate(byId);
        }
        QueryWrapper<AstPlace> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AstPlace::getDelFlag, "0");
        List<AstPlace> list = astPlaceService.list(wrapper);
        return success(list);
    }
}

