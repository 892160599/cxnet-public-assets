package com.cxnet.asset.astcardstyletype.controller;

import static com.cxnet.common.constant.AjaxResult.success;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxnet.asset.astcardstyletype.domain.AstCardStyleType;
import com.cxnet.asset.astcardstyletype.service.AstCardStyleTypeService;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.exception.CustomException;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.framework.web.controller.BaseController;

import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 卡片样式管理资产类别表(AstCardStyleType)表控制层
 *
 * @author makejava
 * @since 2021-04-26 15:31:36
 */
@Api(tags = "卡片样式管理资产类别表")
@RestController
@RequestMapping("/astCardStyleType")
public class AstCardStyleTypeController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private AstCardStyleTypeService astCardStyleTypeService;

    /**
     * 根据卡片样式id查询所有数据
     *
     * @param astCardStyleType 查询实体
     * @return 所有数据
     */
    @ApiOperation("查询关联资产类别")
    @GetMapping
    public AjaxResult selectAll(AstCardStyleType astCardStyleType) {
        QueryWrapper<AstCardStyleType> qw = new QueryWrapper<>(astCardStyleType);
        List<AstCardStyleType> list = astCardStyleTypeService.list(qw);
        return success(list);
    }


    /**
     * 设置资产类别
     *
     * @param astCardStyleType 实体对象
     * @return 修改结果
     */
    @ApiOperation("设置资产类别")
    @Log(title = "卡片样式管理资产类别表", businessType = BusinessType.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    @PutMapping
    public AjaxResult setAssetCode(@RequestBody List<AstCardStyleType> astCardStyleTypeList) {
        if (CollectionUtil.isNotEmpty(astCardStyleTypeList)) {
            QueryWrapper<AstCardStyleType> qw = null;
            List<AstCardStyleType> oldAstCardStyleTypeList = null;
            for (AstCardStyleType astCardStyleType : astCardStyleTypeList) {
                qw = new QueryWrapper<>();
                //根据样式id，单位id，资产类别code,年度查询
                qw.lambda().ne(AstCardStyleType::getCardStyleId, astCardStyleType.getCardStyleId())
                        .eq(AstCardStyleType::getUnitId, astCardStyleType.getUnitId())
                        .eq(AstCardStyleType::getAssetCode, astCardStyleType.getAssetCode())
                        .eq(AstCardStyleType::getFiscal, astCardStyleType.getFiscal());
                oldAstCardStyleTypeList = astCardStyleTypeService.list(qw);

                if (CollectionUtil.isNotEmpty(oldAstCardStyleTypeList)) {
                    throw new CustomException("一个资产类别只能关联一个样式");
                }
            }
            //根据样式id删除原有数据
            Map<String, Object> columnMap = new HashedMap<>();
            columnMap.put("CARD_STYLE_ID", astCardStyleTypeList.get(0).getCardStyleId());
            astCardStyleTypeService.removeByMap(columnMap);
            astCardStyleTypeService.saveBatch(astCardStyleTypeList);
        }
        return success();
    }


}