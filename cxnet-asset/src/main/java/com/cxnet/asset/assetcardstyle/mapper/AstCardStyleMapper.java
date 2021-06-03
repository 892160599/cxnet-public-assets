package com.cxnet.asset.assetcardstyle.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cxnet.rpc.domain.asset.AstCardStyle;

/**
 * 单据样式配置表(AstCardStyle)表数据库访问层
 *
 * @author guks
 * @since
 */
public interface AstCardStyleMapper extends BaseMapper<AstCardStyle> {

    /**
     * 根据单据类型和单位id重置默认值
     *
     * @param billType 单据类型
     * @param unitId   单位id
     * @return
     */
    int updateBathbyBillType(@Param("billType") String billType, @Param("unitId") String unitId);

}