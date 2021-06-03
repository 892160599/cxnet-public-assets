package com.cxnet.asset.businessHandling.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.asset.businessHandling.domain.VAstBusinessHandling;
import com.cxnet.flow.model.domain.SysModel;

import java.util.List;

/**
 * 资产业务办理(VAstBusinessHandling)表服务接口
 *
 * @author caixx
 * @since 2021-03-31 16:04:17
 */
public interface VAstBusinessHandlingService extends IService<VAstBusinessHandling> {

    /**
     * 查询单据下拉列表
     *
     * @return
     */
    List<SysModel> getAstModel();

    /**
     * 分页查询所有数据(我的事项)
     *
     * @param vAstBusinessHandling
     * @return
     */
    List<VAstBusinessHandling> selectAssetMatter(VAstBusinessHandling vAstBusinessHandling);

    /**
     * @param vAstBusinessHandling
     * @return
     */
    List<VAstBusinessHandling> selectAssetAudit(VAstBusinessHandling vAstBusinessHandling);
}