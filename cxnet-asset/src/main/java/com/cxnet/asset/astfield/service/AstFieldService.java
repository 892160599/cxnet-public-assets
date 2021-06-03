package com.cxnet.asset.astfield.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.asset.astfield.domain.AstField;

/**
 * 卡片字段表(AstField)表服务接口
 *
 * @author guks
 * @since 2021-04-25 14:31:54
 */
public interface AstFieldService extends IService<AstField> {


    /**
     * 根据数据库配置获取动态类
     *
     * @param exclude 排除状态
     * @param unitId  单位
     * @return
     */
    Class<?> getDynamicClass(String exclude, String unitId);
}