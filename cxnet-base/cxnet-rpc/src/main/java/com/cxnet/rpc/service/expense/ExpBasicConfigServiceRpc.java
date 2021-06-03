package com.cxnet.rpc.service.expense;

public interface ExpBasicConfigServiceRpc {


    /**
     * 根据单据配置id和单位拷贝卡片样式
     *
     * @param cardStyleId            单据样式id
     * @param unitId                 单位id
     * @param defaultUnit            默认单位id
     * @param templateAstCardStyleId 模板id
     * @return true 表示复制成功
     */
    void copyByCardStyleIdCodeAndUnitId(String cardStyleId, String unitId, String defaultUnit, String templateAstCardStyleId);


}
