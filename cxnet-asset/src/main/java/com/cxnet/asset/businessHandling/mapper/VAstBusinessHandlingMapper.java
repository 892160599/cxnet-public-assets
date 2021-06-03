package com.cxnet.asset.businessHandling.mapper;

import com.cxnet.asset.businessHandling.domain.VAstBusinessHandling;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 资产业务办理(VAstBusinessHandling)表数据库访问层
 *
 * @author caixx
 * @since 2021-03-31 16:04:18
 */
public interface VAstBusinessHandlingMapper extends BaseMapper<VAstBusinessHandling> {

    List<VAstBusinessHandling> selectAssetMatter(VAstBusinessHandling vAstBusinessHandling);

    /**
     * 分页查询所有数据(业务审批)
     *
     * @param vAstBusinessHandling
     * @return
     */
    List<VAstBusinessHandling> selectAssetAudit(VAstBusinessHandling vAstBusinessHandling);
}