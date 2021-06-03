package com.cxnet.asset.assetcardstyle.service;

import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.asset.assetcardstyle.mapper.AstCardStyleMapper;
import com.cxnet.common.constant.Constants;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.rpc.domain.asset.AstCardStyle;

/**
 * 单据样式配置表(AstCardStyle)表服务实现类
 *
 * @author guks
 * @since 2021-03-24 18:28:43
 */
@Service
public class AstCardStyleServiceImpl extends ServiceImpl<AstCardStyleMapper, AstCardStyle>
        implements AstCardStyleService {

    /**
     * 根据单据类型和单位id重置默认值
     *
     * @param billType 单据类型
     * @param unitId   单据id
     * @return
     */
    @Override
    public int updateBathbyBillType(String billType, String unitId) {
        return this.baseMapper.updateBathbyBillType(billType, unitId);
    }

    /**
     * 获取单据下的默认样式
     *
     * @param modelCode 单据code
     * @param unitId    单位编码
     * @return
     */
    @Override
    public Map<String, Object> getDefaultStyleInModel(String modelCode, String unitId) {
        Map<String, Object> resultMap = new HashedMap<>(2);
        //查询当前单位下的默认样式
        AstCardStyle astCardStyle = queryAstCardStyle(modelCode, unitId, null);

        if (null == astCardStyle) {
            //查询全部单位下的默认样式
            astCardStyle = queryAstCardStyle(modelCode, "0", null);
            //查询模板
            if (null == astCardStyle) {
                astCardStyle = queryAstCardStyle(modelCode, "0", Constants.YES);
            }
        }
        resultMap.put("cardStyleId", astCardStyle.getId());
        resultMap.put("unitId", astCardStyle.getUnitId());
        return resultMap;
    }

    /**
     * 根据条件查询卡片样式
     *
     * @param modelCode     单据code
     * @param unitId        单位id
     * @param cardStyleCode 单据样式code
     * @return
     */
    private AstCardStyle queryAstCardStyle(String modelCode, String unitId, String isPreset) {

        QueryWrapper<AstCardStyle> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isBlank(isPreset)) {
            // 查询单位下是否存在默认样式
            queryWrapper.lambda().eq(AstCardStyle::getIsDefault, "0").eq(AstCardStyle::getDelFlag, "0")
                    .eq(AstCardStyle::getUnitId, unitId).eq(AstCardStyle::getBillType, modelCode);
        } else {
            queryWrapper.lambda().eq(AstCardStyle::getDelFlag, "0").eq(AstCardStyle::getUnitId, "0")
                    .eq(AstCardStyle::getIsPreset, isPreset).eq(AstCardStyle::getBillType, modelCode);
        }
        return this.baseMapper.selectOne(queryWrapper);
    }

}