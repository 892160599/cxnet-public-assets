package com.cxnet.asset.assetcardstyle.service;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.rpc.domain.asset.AstCardStyle;

/**
 * 单据样式配置表(AstCardStyle)表服务接口
 *
 * @author guks
 * @since 2021-03-24 18:28:40
 */
public interface AstCardStyleService extends IService<AstCardStyle> {

    /**
     * 根据单据类型和单位id重置默认值
     *
     * @param billType 单据类型
     * @param unitId   单据id
     * @return
     */
    int updateBathbyBillType(String billType, String unitId);


    /**
     * 获取单据下的默认样式
     *
     * @param modelCode 单据code
     * @param unitId    单据id
     * @return
     */
    Map<String, Object> getDefaultStyleInModel(String modelCode, String unitId);


}