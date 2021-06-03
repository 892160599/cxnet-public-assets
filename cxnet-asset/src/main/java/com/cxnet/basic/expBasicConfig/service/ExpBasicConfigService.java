package com.cxnet.basic.expBasicConfig.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.basic.expBasicConfig.domain.ExpBasicConfig;
import com.cxnet.basic.expBasicConfig.domain.ExpBasicConfigVo;
import com.cxnet.common.utils.tree.Zone;

import java.util.List;

/**
 * 报销单据配置(ExpBasicConfig)表服务接口
 *
 * @author caixx
 * @since 2020-09-11 15:13:53
 */
public interface ExpBasicConfigService extends IService<ExpBasicConfig> {

    /**
     * 查询报销单据tree
     *
     * @param expBasicConfig
     * @return
     */
    List<Zone> selectTree(ExpBasicConfig expBasicConfig);

    /**
     * 通过单据modelId查询所有配置信息
     *
     * @param id
     * @return
     */
    ExpBasicConfigVo getExpBasicConfigByModelCode(String id, String cardStyleId, String unitId, String defaultUnitId);

    String updateByExpBasicConfigVo(ExpBasicConfigVo expBasicConfigVo);

    /**
     * 通过单据modeCode恢复默认配置
     *
     * @param modelCode
     * @return
     */
    ExpBasicConfigVo reSet(String modelCode, String unitId, String defaultUnitId);

    ExpBasicConfigVo syncBill(String modelCode, String unitId, String defaultUnitId, List<String> unitIds);

}