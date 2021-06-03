package com.cxnet.asset.assetcardstyle.rpc;

import static org.hamcrest.CoreMatchers.nullValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.asset.assetcardstyle.mapper.AstCardStyleMapper;
import com.cxnet.asset.astcardstyletype.domain.AstCardStyleType;
import com.cxnet.asset.astcardstyletype.service.AstCardStyleTypeService;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.rpc.domain.asset.AstCardStyle;
import com.cxnet.rpc.service.asset.AstCardStyleServiceRpc;

/**
 * 单据样式配置表(AstCardStyle)表rpc服务实现类
 *
 * @author guks
 * @since 2021-03-24 18:28:43
 */
@Service
public class AstCardStyleServiceRpcImpl extends ServiceImpl<AstCardStyleMapper, AstCardStyle> implements AstCardStyleServiceRpc {


    @Autowired(required = false)
    private AstCardStyleTypeService astCardStyleTypeService;

    /**
     * 根据单位id和资产id查询卡片样式
     *
     * @param unitId    单位id
     * @param assetCode 资产类别编码
     * @param assetType 资产类型
     * @return 单据样式对象
     */
    @Override
    public AstCardStyle getAstCardStyleByUnitIdAndAssetCode(String unitId, String assetCode, String assetType) {
        AstCardStyle astCardStyle = null;
        AstCardStyleType astCardStyleType = null;

        if (StringUtils.isNotBlank(assetCode)) {
            //根据单位id和资产code获取卡片id
            QueryWrapper<AstCardStyleType> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(AstCardStyleType::getAssetCode, assetCode).eq(AstCardStyleType::getUnitId, unitId);
            astCardStyleType = astCardStyleTypeService.getOne(queryWrapper);
            //获取资产卡片样式对象
            if (null != astCardStyleType) {
                QueryWrapper<AstCardStyle> astCardStyleQuery = new QueryWrapper<>();
                astCardStyleQuery.lambda().eq(AstCardStyle::getUnitId, unitId)
                        .eq(AstCardStyle::getDelFlag, "0")
                        .eq(AstCardStyle::getId, astCardStyleType.getCardStyleId());
                astCardStyle = this.getOne(astCardStyleQuery);
            }
        } else {
            //查询模板样式
            QueryWrapper<AstCardStyle> astCardStyleQuery = new QueryWrapper<>();
            astCardStyleQuery.lambda().eq(AstCardStyle::getCardStyleCode, "10001")
                    .eq(AstCardStyle::getIsPreset, "0")
                    .eq(AstCardStyle::getAssetType, assetType)
                    .eq(AstCardStyle::getUnitId, unitId)
                    .eq(AstCardStyle::getDelFlag, "0");
            astCardStyle = this.getOne(astCardStyleQuery);
        }

        return astCardStyle;
    }

}
