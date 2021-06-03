package com.cxnet.asset.astfield.controller;

import static com.cxnet.common.constant.AjaxResult.success;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxnet.asset.astfield.domain.AstField;
import com.cxnet.asset.astfield.service.AstFieldService;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.common.utils.poi.ExcelUtil;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.framework.web.controller.BaseController;

import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 卡片字段表(AstField)表控制层
 *
 * @author guks
 * @since 2021-04-25 14:31:57
 */
@Api(tags = "卡片字段表")
@RestController
@RequestMapping("/astField")
public class AstFieldController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private AstFieldService astFieldService;


    /**
     * 期初卡片导入模板字段
     *
     * @param astField 查询实体
     * @return 所有数据
     */
    @ApiOperation("期初卡片导入模板字段")
    @GetMapping("/selList")
    public AjaxResult selList(AstField astField) {
        QueryWrapper<AstField> qw = new QueryWrapper<>();
        qw.lambda().eq(AstField::getIsEnable, "0").eq(AstField::getIsImport, "0")
                .eq(AstField::getIsRequired, "0");
        //按照顺序排序
        qw.orderByAsc("ORDERS");
        List<AstField> list = astFieldService.list(qw);
        return success(list);
    }

    /**
     * 分页查询所有数据
     *
     * @param astField 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询所有数据")
    @GetMapping
    public AjaxResult selectAll(AstField astField) {
        startPage();
        QueryWrapper<AstField> qw = new QueryWrapper<>();
        //关键字查询
        if (StringUtils.isNotBlank(astField.getSearchValue())) {
            qw.and(
                    wrapper ->
                            wrapper.like("FIELD_CODE", astField.getSearchValue()).or().like("FIELD_NAME", astField.getSearchValue())
            );
        }
        qw.eq("IS_ENABLE", astField.getIsEnable());
        //按照顺序排序
        qw.orderByAsc("ORDERS");
        List<AstField> list = astFieldService.list(qw);

        return success(getDataTable(list));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @GetMapping("{id}")
    public AjaxResult selectOne(@PathVariable Serializable id) {
        return success(astFieldService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param astField 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @Log(title = "卡片字段表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult insert(@RequestBody AstField astField) {
        Map<String, Object> columnMap = new HashedMap<>();

        columnMap.put("UNIT_ID", astField.getUnitId());
        columnMap.put("FIELD_CODE", astField.getFieldCode());
        if (CollectionUtil.isNotEmpty(astFieldService.listByMap(columnMap))) {
            throw new CustomException("字段代码不可以重复");
        }
        return success(astFieldService.save(astField));
    }

    /**
     * 修改数据
     *
     * @param astField 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @Log(title = "卡片字段表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@RequestBody AstField astField) {
        return success(astFieldService.updateById(astField));
    }

    /**
     * 删除数据
     *
     * @param ids 主键结合
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @Log(title = "卡片字段表", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult delete(@RequestBody List<String> ids) {
        return success(astFieldService.removeByIds(ids));
    }


}