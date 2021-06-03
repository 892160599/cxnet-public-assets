package com.cxnet.basic.expBasicConfig.rpc;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxnet.basic.expBasicConfig.domain.ExpBasicConfig;
import com.cxnet.basic.expBasicConfig.service.ExpBasicConfigService;
import com.cxnet.basic.expModuleConfig.domain.ExpModuleConfig;
import com.cxnet.basic.expModuleConfig.service.ExpModuleConfigService;
import com.cxnet.rpc.service.expense.ExpBasicConfigServiceRpc;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExpBasicConfigServiceRpcImpl implements ExpBasicConfigServiceRpc {

    @Autowired
    private ExpBasicConfigService expBasicConfigService;

    @Autowired
    private ExpModuleConfigService expModuleConfigService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void copyByCardStyleIdCodeAndUnitId(String cardStyleId, String unitId, String defaultUnit, String templateAstCardStyleId) {

        // 查询全部单位下的模版配置
        QueryWrapper<ExpModuleConfig> qw = new QueryWrapper<>();
        qw.lambda().eq(ExpModuleConfig::getUnitId, defaultUnit).eq(ExpModuleConfig::getCardStyleId, templateAstCardStyleId);
        List<ExpModuleConfig> expModuleConfigs = expModuleConfigService.list(qw);

        if (CollectionUtil.isEmpty(expModuleConfigs)) {
            //查询默认配置
            qw = new QueryWrapper<>();
            qw.lambda().eq(ExpModuleConfig::getUnitId, "*").eq(ExpModuleConfig::getCardStyleId, "*");
            expModuleConfigs = expModuleConfigService.list(qw);
        }

        // 获取模块编码和id对应关系
        Map<String, String> oldmoduleCodeMap = expModuleConfigs.stream().collect(
                Collectors.toMap(ExpModuleConfig::getModuleCode, ExpModuleConfig::getId, (key1, key2) -> key2));

        expModuleConfigs.forEach(v -> {
            v.setUnitId(unitId);
            v.setId(null);
            v.setCardStyleId(cardStyleId);
        });


        expModuleConfigService.saveOrUpdateBatch(expModuleConfigs);

        // 获取修改之后和id对应关系
        Map<String, String> moduleCodeMap = expModuleConfigs.stream().collect(
                Collectors.toMap(ExpModuleConfig::getModuleCode, ExpModuleConfig::getId, (key1, key2) -> key2));

        Map columnMap = new HashedMap<>(2);
        List<ExpBasicConfig> expBasicConfigList = null;

        for (Map.Entry<String, String> entry : oldmoduleCodeMap.entrySet()) {
            columnMap.put("MODULE_ID", entry.getValue());
            columnMap.put("UNIT_ID", defaultUnit);
            expBasicConfigList = expBasicConfigService.listByMap(columnMap);

            if (CollectionUtil.isEmpty(expBasicConfigList)) {
                columnMap.put("UNIT_ID", "*");
                expBasicConfigList = expBasicConfigService.listByMap(columnMap);
            }


            if (CollectionUtil.isNotEmpty(expBasicConfigList)) {
                expBasicConfigList.forEach(v -> {
                    v.setUnitId(unitId);
                    v.setId(null);
                    v.setCardStyleId(cardStyleId);
                    v.setModuleId(moduleCodeMap.get(entry.getKey()));
                });

                expBasicConfigService.saveOrUpdateBatch(expBasicConfigList);
            }
        }

    }

}
