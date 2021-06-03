package com.cxnet.basic.expBasicConfig.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.basic.expBasicConfig.domain.ExpBasicConfig;
import com.cxnet.basic.expBasicConfig.domain.ExpBasicConfigCost;
import com.cxnet.basic.expBasicConfig.domain.ExpBasicConfigVo;
import com.cxnet.basic.expBasicConfig.mapper.ExpBasicConfigMapper;
import com.cxnet.basic.expBasicConfig.service.ExpBasicConfigCostService;
import com.cxnet.basic.expBasicConfig.service.ExpBasicConfigService;
import com.cxnet.basic.expModuleConfig.domain.ExpModuleConfig;
import com.cxnet.basic.expModuleConfig.service.ExpModuleConfigService;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.common.utils.TreeUtil;
import com.cxnet.common.utils.tree.Zone;
import com.cxnet.common.utils.tree.ZoneUtils;
import com.cxnet.flow.model.domain.SysModel;
import com.cxnet.flow.model.mapper.SysModelMapper;
import com.cxnet.flow.utils.BillUtils;
import com.cxnet.project.system.dict.mapper.SysDictDataMapper;
import com.cxnet.rpc.domain.per.PerConfigIndex;
import com.cxnet.rpc.service.per.PerConfigIndexServiceRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 报销单据配置(ExpBasicConfig)表服务实现类
 *
 * @author caixx
 * @since 2020-09-11 15:13:54
 */
@Service
public class ExpBasicConfigServiceImpl extends ServiceImpl<ExpBasicConfigMapper, ExpBasicConfig> implements ExpBasicConfigService {

    @Autowired
    private SysModelMapper sysModelMapper;

    @Autowired
    private ExpBasicConfigCostService expBasicConfigCostService;

    @Autowired
    private ExpModuleConfigService expModuleConfigService;

    @Autowired
    private SysDictDataMapper sysDictDataMapper;

    @Autowired(required = false)
    private PerConfigIndexServiceRpc perConfigIndexServiceRpc;

    /**
     * 报销前缀
     */
    public static final String EXP_PREFIX = "exp_";

    /**
     * 绩效前缀
     */
    public static final String PER_PREFIX = "per_";

    /**
     * 查询报销单据tree
     *
     * @param expBasicConfig
     * @return
     */
    @Override
    public List<Zone> selectTree(ExpBasicConfig expBasicConfig) {
        List<Zone> zoneList = baseMapper.selectZone(expBasicConfig);
        zoneList.forEach(v -> {
            if (v.getCode().startsWith(EXP_PREFIX)) {
                v.setType("1");
            }
        });
        return ZoneUtils.buildTreeByTopVal(zoneList, "10");
    }

    /**
     * 查询
     *
     * @param modelCode
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExpBasicConfigVo getExpBasicConfigByModelCode(String modelCode, String cardStyleId, String unitId, String defaultUnitId) {
        ExpBasicConfigVo expBasicConfigVo = new ExpBasicConfigVo();
        // 单据主表
        SysModel sysModel = sysModelMapper.selectSysModelByBillTypeCode(modelCode);
        if (sysModel == null) {
            throw new CustomException("未查询到所选的单据信息！");
        }
        expBasicConfigVo.setSysModel(sysModel);
        //exp报销 modelType类型（1模块 2单据
        if (StringUtils.isNotEmpty(modelCode) && sysModel.getModelCode().startsWith(EXP_PREFIX) && 2L == sysModel.getModelType()) {
            // 主表
            String tableMain = sysModel.getTableMain();
            if (StringUtils.isEmpty(tableMain)) {
                throw new CustomException("单据配置异常，请联系管理员！");
            }
            // 基本信息
            // 费用明细
            QueryWrapper<ExpBasicConfigCost> expBasicConfigCostQw = new QueryWrapper<>();
            expBasicConfigCostQw.lambda().eq(ExpBasicConfigCost::getModelCode, modelCode).eq(ExpBasicConfigCost::getUnitId, unitId);
            List<ExpBasicConfigCost> expBasicConfigCosts = expBasicConfigCostService.list(expBasicConfigCostQw);
            if (CollectionUtil.isEmpty(expBasicConfigCosts)) {
                QueryWrapper<ExpBasicConfigCost> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(ExpBasicConfigCost::getModelCode, modelCode).eq(ExpBasicConfigCost::getUnitId, "0");
                expBasicConfigCosts = expBasicConfigCostService.list(queryWrapper);
            }
            if (CollectionUtil.isNotEmpty(expBasicConfigCosts)) {
                expBasicConfigCosts.sort(Comparator.comparing(ExpBasicConfigCost::getSort));
            }
            expBasicConfigVo.setExpBasicConfigCosts(expBasicConfigCosts);
            // 附件类别
            expBasicConfigVo.setDictTypeCode(BillUtils.getDictTypeCode(modelCode));
        }
        //per绩效 modelType类型（1模块 2单据
        if (StringUtils.isNotEmpty(modelCode) && sysModel.getModelCode().startsWith(PER_PREFIX) && 2L == sysModel.getModelType()) {
            QueryWrapper<PerConfigIndex> perConfigIndexQueryWrapper = new QueryWrapper<>();
            perConfigIndexQueryWrapper.lambda().eq(PerConfigIndex::getModelCode, modelCode).eq(PerConfigIndex::getUnitId, unitId);
            List<PerConfigIndex> perConfigIndices = perConfigIndexServiceRpc.list(perConfigIndexQueryWrapper);
            if (CollectionUtil.isEmpty(perConfigIndices)) {
                QueryWrapper<PerConfigIndex> perConfigIndexQueryWrapper1 = new QueryWrapper<>();
                perConfigIndexQueryWrapper1.lambda().eq(PerConfigIndex::getModelCode, modelCode).eq(PerConfigIndex::getUnitId, "0");
                perConfigIndices = perConfigIndexServiceRpc.list(perConfigIndexQueryWrapper1);
            }
            TreeUtil treeUtil = new TreeUtil();
            JSONArray perEvaluationIndexListJSONArray = treeUtil.toTree(JSONArray.parseArray(JSON.toJSONString(perConfigIndices, SerializerFeature.WriteMapNullValue)), "indexCode", "parentCode");
            perConfigIndices = JSONArray.parseArray(JSON.toJSONString(perEvaluationIndexListJSONArray, SerializerFeature.WriteMapNullValue), PerConfigIndex.class);
            expBasicConfigVo.setPerConfigIndices(perConfigIndices);
        }
        String queryCardStyleId = (StringUtils.isBlank(cardStyleId)) ? "*" : cardStyleId;

        // new
        QueryWrapper<ExpModuleConfig> qw = new QueryWrapper<>();
        qw.lambda()
                .eq(ExpModuleConfig::getUnitId, unitId)
                .eq(ExpModuleConfig::getModelCode, modelCode)
                .eq(ExpModuleConfig::getCardStyleId, queryCardStyleId)
                .orderByAsc(ExpModuleConfig::getOrders);
        List<ExpModuleConfig> expModuleConfigs = expModuleConfigService.list(qw);
        if (CollectionUtil.isEmpty(expModuleConfigs)) {
            // 查询默认配置
            qw = new QueryWrapper<>();
            qw.lambda()
                    .eq(ExpModuleConfig::getUnitId, defaultUnitId)
                    .eq(ExpModuleConfig::getModelCode, modelCode)
                    .eq(ExpModuleConfig::getCardStyleId, "*")
                    .orderByAsc(ExpModuleConfig::getOrders);
            expModuleConfigs = expModuleConfigService.list(qw);
            if (CollectionUtil.isEmpty(expModuleConfigs)) {
                return expBasicConfigVo;
            }
            expModuleConfigs.forEach(v -> {
                v.setUnitId(unitId);
                v.setId(null);
                v.setCardStyleId(queryCardStyleId);
            });
            expModuleConfigService.saveBatch(expModuleConfigs);
        }
        List<ExpModuleConfig> blocks = new ArrayList<>();
        for (ExpModuleConfig moduleConfig : expModuleConfigs) {
            blocks.add(moduleConfig);
            String tableName = moduleConfig.getTableName();
            if (StringUtils.isNotEmpty(tableName)) {
                String dictJson = moduleConfig.getDictJson();
                if (StringUtils.isNotEmpty(dictJson)) {
                    getExpBasicConfig(moduleConfig, dictJson, queryCardStyleId, defaultUnitId);
                    continue;
                }
                moduleConfig.setDetails(getBasicConfig(moduleConfig, queryCardStyleId, defaultUnitId));
            }
        }
        expBasicConfigVo.setBlocks(blocks);
        return expBasicConfigVo;
    }

    /**
     * 查询BasicConfig
     *
     * @param moduleConfig
     */
    private List<ExpBasicConfig> getBasicConfig(ExpModuleConfig moduleConfig, String cardStyleId, String defaultUnitId) {
        QueryWrapper<ExpBasicConfig> qw = new QueryWrapper<>();
        qw.lambda().eq(ExpBasicConfig::getModuleId, moduleConfig.getId())
                .eq(ExpBasicConfig::getUnitId, moduleConfig.getUnitId())
                .orderByAsc(ExpBasicConfig::getOrders);
        List<ExpBasicConfig> expBasicConfigs = this.list(qw);
        if (CollectionUtil.isEmpty(expBasicConfigs)) {
            // 查询默认配置
            QueryWrapper<ExpModuleConfig> moduleConfigQueryWrapper = new QueryWrapper<>();
            moduleConfigQueryWrapper.lambda()
                    .eq(ExpModuleConfig::getUnitId, defaultUnitId)
                    .eq(ExpModuleConfig::getModelCode, moduleConfig.getModelCode())
                    .eq(ExpModuleConfig::getModuleCode, moduleConfig.getModuleCode())
                    .eq(ExpModuleConfig::getCardStyleId, "*");
            ExpModuleConfig expModuleConfig = expModuleConfigService.getOne(moduleConfigQueryWrapper);
            qw = new QueryWrapper<>();
            qw.lambda().eq(ExpBasicConfig::getModuleId, expModuleConfig.getId())
                    .eq(ExpBasicConfig::getCardStyleId, "*")
                    .eq(ExpBasicConfig::getUnitId, moduleConfig.getUnitId())
                    .orderByAsc(ExpBasicConfig::getOrders);
            List<ExpBasicConfig> expBasicConfigList = this.list(qw);
            if (CollectionUtil.isEmpty(expBasicConfigList)) {
                qw = new QueryWrapper<>();
                qw.lambda().eq(ExpBasicConfig::getModuleId, expModuleConfig.getId())
                        .eq(ExpBasicConfig::getUnitId, defaultUnitId)
                        .orderByAsc(ExpBasicConfig::getOrders);
                expBasicConfigList = this.list(qw);
            }
            expBasicConfigList.forEach(v -> {
                v.setUnitId(moduleConfig.getUnitId());
                v.setModuleId(moduleConfig.getId());
                v.setId(null);
                v.setCardStyleId(cardStyleId);
            });
            this.saveBatch(expBasicConfigList);
            expBasicConfigs = expBasicConfigList;
        }

        return expBasicConfigs.stream().filter(v -> "0".equals(v.getIsConfig())).collect(Collectors.toList());
    }

    /**
     * 查询有字典配置的ExpBasicConfig
     *
     * @param moduleConfig
     * @param dictJson
     */
    private void getExpBasicConfig(ExpModuleConfig moduleConfig, String dictJson, String cardStyleId, String defaultUnitId) {
        List<ExpBasicConfig> expBasicConfig = getBasicConfig(moduleConfig, cardStyleId, defaultUnitId);
        List<ExpModuleConfig> expModuleConfigs = JSONArray.parseArray(dictJson, ExpModuleConfig.class);
        for (ExpModuleConfig expModuleConfig : expModuleConfigs) {
            List<ExpBasicConfig> expBasicConfigList = expBasicConfig.stream().filter(v -> expModuleConfig.getDictKey().equals(v.getModuleDictKey())).collect(Collectors.toList());
            expModuleConfig.setDetails(expBasicConfigList);
        }
        moduleConfig.setDetails(expModuleConfigs);
    }


    /**
     * 修改
     *
     * @param expBasicConfigVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateByExpBasicConfigVo(ExpBasicConfigVo expBasicConfigVo) {
        SysModel sysModel = expBasicConfigVo.getSysModel();
        String modelCode = sysModel.getModelCode();
        // 修改费用明细
        List<ExpBasicConfigCost> expBasicConfigCosts = expBasicConfigVo.getExpBasicConfigCosts();
        List<ExpModuleConfig> blocks = expBasicConfigVo.getBlocks();
        List<ExpBasicConfig> expBasicConfigList = new ArrayList<>();
        if (modelCode.startsWith(EXP_PREFIX)) {
            // 修改基本信息
            QueryWrapper<ExpBasicConfigCost> basicConfigCostQw = new QueryWrapper<>();
            basicConfigCostQw.lambda().eq(ExpBasicConfigCost::getModelCode, sysModel.getModelCode()).eq(ExpBasicConfigCost::getUnitId, blocks.get(0).getUnitId());
            expBasicConfigCostService.remove(basicConfigCostQw);
            if (CollectionUtil.isNotEmpty(expBasicConfigCosts)) {
                expBasicConfigCosts.forEach(v -> {
                    v.setModelCode(modelCode);
                    v.setUnitId(blocks.get(0).getUnitId());
                    v.setId(null);
                });
                expBasicConfigCostService.saveOrUpdateBatch(expBasicConfigCosts);
            }
        }
        // 固化指标
        if (modelCode.startsWith(PER_PREFIX)) {
            buildPerIndex(expBasicConfigVo);
        }
        // new
        if (CollectionUtil.isNotEmpty(blocks)) {
            for (ExpModuleConfig expModuleConfig : blocks) {
                if (ObjectUtil.isNotNull(expModuleConfig.getDetails())) {
                    // 字典配置字段
                    if (StringUtils.isNotEmpty(expModuleConfig.getDictKey())) {
                        String detailsStr = JSONArray.toJSONString(expModuleConfig.getDetails());
                        List<ExpModuleConfig> expModuleConfigs = JSONArray.parseArray(detailsStr, ExpModuleConfig.class);
                        for (ExpModuleConfig moduleConfig : expModuleConfigs) {
                            String string = JSONArray.toJSONString(moduleConfig.getDetails());
                            expBasicConfigList.addAll(JSONArray.parseArray(string, ExpBasicConfig.class));
                            moduleConfig.setDetails("");
                        }
                        expModuleConfig.setDictJson(JSONArray.toJSONString(expModuleConfigs));
                    } else {
                        String detailsStr = JSONArray.toJSONString(expModuleConfig.getDetails());
                        expBasicConfigList.addAll(JSONArray.parseArray(detailsStr, ExpBasicConfig.class));
                    }
                }
            }
            QueryWrapper<ExpModuleConfig> moduleConfigQueryWrapper = new QueryWrapper<>();
            moduleConfigQueryWrapper.lambda().eq(ExpModuleConfig::getModelCode, sysModel.getModelCode()).eq(ExpModuleConfig::getUnitId, blocks.get(0).getUnitId());
            List<ExpModuleConfig> moduleConfigs = expModuleConfigService.list(moduleConfigQueryWrapper);
            if (CollectionUtil.isEmpty(moduleConfigs)) {
                for (ExpModuleConfig expModuleConfig : blocks) {
                    List<ExpBasicConfig> collect = expBasicConfigList.stream().filter(b -> b.getModuleId().equals(expModuleConfig.getId())).collect(Collectors.toList());
                    expModuleConfig.setId(null);
                    expModuleConfigService.save(expModuleConfig);
                    collect.forEach(v -> v.setModuleId(expModuleConfig.getId()));
                }
            } else {
                expModuleConfigService.saveOrUpdateBatch(blocks);
            }
            QueryWrapper<ExpBasicConfig> basicConfigQueryWrapper = new QueryWrapper<>();
            basicConfigQueryWrapper.lambda().eq(ExpBasicConfig::getModelCode, sysModel.getModelCode()).eq(ExpBasicConfig::getUnitId, blocks.get(0).getUnitId());
            List<ExpBasicConfig> list = this.list(basicConfigQueryWrapper);
            if (CollectionUtil.isEmpty(list)) {
                expBasicConfigList.forEach(v -> v.setId(null));
                this.saveBatch(expBasicConfigList);
            } else {
                this.saveOrUpdateBatch(expBasicConfigList);
            }
        }
        sysModelMapper.updateSysModel(sysModel);
        return "保存成功！";
    }

    /**
     * 构建保存固化指标
     *
     * @param expBasicConfigVo
     */
    private void buildPerIndex(ExpBasicConfigVo expBasicConfigVo) {
        List<PerConfigIndex> perConfigIndices = expBasicConfigVo.getPerConfigIndices();
        SysModel sysModel = expBasicConfigVo.getSysModel();
        String unitId = expBasicConfigVo.getUnitId();
        QueryWrapper<PerConfigIndex> perConfigIndexQueryWrapper = new QueryWrapper<>();
        perConfigIndexQueryWrapper.lambda().eq(PerConfigIndex::getModelCode, sysModel.getModelCode()).eq(PerConfigIndex::getUnitId, unitId);
        perConfigIndexServiceRpc.remove(perConfigIndexQueryWrapper);
        if (CollectionUtil.isNotEmpty(perConfigIndices)) {
            savePerEvaluationIndexList(perConfigIndices, sysModel, unitId);
        }
    }

    /**
     * 通过单据modeCode恢复默认配置
     *
     * @param modelCode
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExpBasicConfigVo reSet(String modelCode, String unitId, String defaultUnitId) {
        if (StringUtils.isEmpty(modelCode)) {
            return new ExpBasicConfigVo();
        }
        if (!"*".equals(unitId)) {
            QueryWrapper<ExpModuleConfig> qw = new QueryWrapper<>();
            qw.lambda()
                    .eq(ExpModuleConfig::getUnitId, unitId)
                    .eq(ExpModuleConfig::getModelCode, modelCode);
            List<ExpModuleConfig> expModuleConfigs = expModuleConfigService.list(qw);
            if (CollectionUtil.isNotEmpty(expModuleConfigs)) {
                List<String> ids = expModuleConfigs.stream().map(ExpModuleConfig::getId).collect(Collectors.toList());
                QueryWrapper<ExpBasicConfig> expBasicConfigQueryWrapper = new QueryWrapper<>();
                expBasicConfigQueryWrapper.lambda().in(ExpBasicConfig::getModuleId, ids);
                this.remove(expBasicConfigQueryWrapper);
                expModuleConfigService.removeByIds(ids);
                QueryWrapper<ExpBasicConfigCost> costQueryWrapper = new QueryWrapper<>();
                costQueryWrapper.lambda().eq(ExpBasicConfigCost::getModelCode, modelCode).eq(ExpBasicConfigCost::getUnitId, unitId);
                expBasicConfigCostService.remove(costQueryWrapper);
            }
            // 拷贝
        }
        return getExpBasicConfigByModelCode(modelCode, null, unitId, defaultUnitId);
    }

    /**
     * 保存绩效前评估指标说明
     *
     * @param perEvaluationIndexList
     * @param sysModel
     * @param unitId
     */
    private void savePerEvaluationIndexList(List<PerConfigIndex> perEvaluationIndexList, SysModel sysModel, String unitId) {
        List<PerConfigIndex> perConfigIndices = new ArrayList<>();
        for (PerConfigIndex evaluationIndexList : perEvaluationIndexList) {
            if (CollectionUtil.isNotEmpty(evaluationIndexList.getChildren())) {
                perConfigIndices.addAll(addPerEvaluationIndexList(evaluationIndexList.getChildren()));
            }
            perConfigIndices.add(evaluationIndexList);
        }
        perConfigIndices.forEach(v -> {
            v.setModelCode(sysModel.getModelCode());
            v.setUnitId(unitId);
            v.setIsSolidified("0");
            v.setId(null);
            if (v.getIndexLevel() == 1) {
                v.setParentName("0");
            }
        });
        perConfigIndexServiceRpc.saveOrUpdateBatch(perConfigIndices);
    }

    /**
     * 递归所有指标
     *
     * @param children
     * @return
     */
    private List<PerConfigIndex> addPerEvaluationIndexList(List<PerConfigIndex> children) {
        List<PerConfigIndex> perEvaluationIndexLists = new ArrayList<>();
        for (PerConfigIndex child : children) {
            if (CollectionUtil.isNotEmpty(child.getChildren())) {
                perEvaluationIndexLists.addAll(addPerEvaluationIndexList(child.getChildren()));
            }
            perEvaluationIndexLists.add(child);
        }
        return perEvaluationIndexLists;
    }

    /**
     * 删除目标单位的单据配置
     *
     * @param modelCode     单据code
     * @param unitId        源单位id
     * @param unitIds       目标单位id集合
     * @param defaultUnitId 默认
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExpBasicConfigVo syncBill(String modelCode, String unitId, String defaultUnitId, List<String> unitIds) {
        if (StringUtils.isEmpty(modelCode)) {
            return new ExpBasicConfigVo();
        }
        unitIds.forEach(v -> {
            QueryWrapper<ExpModuleConfig> qw = new QueryWrapper<>();
            qw.lambda()
                    .eq(ExpModuleConfig::getUnitId, v)
                    .eq(ExpModuleConfig::getModelCode, modelCode);
            List<ExpModuleConfig> expModuleConfigs = expModuleConfigService.list(qw);
            if (CollectionUtil.isNotEmpty(expModuleConfigs)) {
                List<String> ids = expModuleConfigs.stream().map(ExpModuleConfig::getId).collect(Collectors.toList());
                QueryWrapper<ExpBasicConfig> expBasicConfigQueryWrapper = new QueryWrapper<>();
                expBasicConfigQueryWrapper.lambda().in(ExpBasicConfig::getModuleId, ids);
                this.remove(expBasicConfigQueryWrapper);
                expModuleConfigService.removeByIds(ids);
                QueryWrapper<ExpBasicConfigCost> costQueryWrapper = new QueryWrapper<>();
                costQueryWrapper.lambda().eq(ExpBasicConfigCost::getModelCode, modelCode).eq(ExpBasicConfigCost::getUnitId, v);
                expBasicConfigCostService.remove(costQueryWrapper);
            }
        });
        return getExpBasicConfigByModelCode(modelCode, null, unitId, defaultUnitId, unitIds);
    }

    /**
     * 将一个单位的单据配置，同步到其他多个单位
     *
     * @param modelCode     单据code
     * @param cardStyleId   卡片类型id
     * @param unitId        源单位id
     * @param defaultUnitId 默认数据id
     * @param unitIds       目标单位id集合
     * @return 结果
     */
    private ExpBasicConfigVo getExpBasicConfigByModelCode(String modelCode, String cardStyleId, String unitId, String defaultUnitId, List<String> unitIds) {
        ExpBasicConfigVo expBasicConfigVo = new ExpBasicConfigVo();
        // 单据主表
        SysModel sysModel = sysModelMapper.selectSysModelByBillTypeCode(modelCode);
        if (sysModel == null) {
            throw new CustomException("未查询到所选的单据信息！");
        }
        for (String id : unitIds) {
            if (modelCode.startsWith(EXP_PREFIX)) {
                // 费用明细
                QueryWrapper<ExpBasicConfigCost> expBasicConfigCostQw = new QueryWrapper<>();
                expBasicConfigCostQw.lambda().eq(ExpBasicConfigCost::getModelCode, modelCode).eq(ExpBasicConfigCost::getUnitId, unitId);
                List<ExpBasicConfigCost> expBasicConfigCosts = expBasicConfigCostService.list(expBasicConfigCostQw);
                if (CollectionUtil.isNotEmpty(expBasicConfigCosts)) {
                    expBasicConfigCosts.forEach(v -> {
                        v.setModelCode(modelCode);
                        v.setUnitId(id);
                        v.setId(null);
                    });
                    expBasicConfigCostService.saveOrUpdateBatch(expBasicConfigCosts);
                }
            }

            String queryCardStyleId = (StringUtils.isBlank(cardStyleId)) ? "*" : cardStyleId;

            // 单据配置
            QueryWrapper<ExpModuleConfig> qw = new QueryWrapper<>();
            qw.lambda()
                    .eq(ExpModuleConfig::getUnitId, unitId)
                    .eq(ExpModuleConfig::getModelCode, modelCode)
                    .eq(ExpModuleConfig::getCardStyleId, queryCardStyleId)
                    .orderByAsc(ExpModuleConfig::getOrders);
            List<ExpModuleConfig> expModuleConfigs = expModuleConfigService.list(qw);
            if (CollectionUtil.isEmpty(expModuleConfigs)) {
                return expBasicConfigVo;
            }
            expModuleConfigs.forEach(v -> {
                v.setUnitId(id);
                v.setId(null);
                v.setCardStyleId(queryCardStyleId);
            });
            expModuleConfigService.saveBatch(expModuleConfigs);
            // 字段详情
            for (ExpModuleConfig moduleConfig : expModuleConfigs) {
                String tableName = moduleConfig.getTableName();
                if (StringUtils.isNotEmpty(tableName)) {
                    QueryWrapper<ExpBasicConfig> queryWrapper = new QueryWrapper<>();
                    queryWrapper.lambda().eq(ExpBasicConfig::getModuleId, moduleConfig.getId())
                            .eq(ExpBasicConfig::getCardStyleId, "*")
                            .eq(ExpBasicConfig::getUnitId, unitId)
                            .orderByAsc(ExpBasicConfig::getOrders);
                    List<ExpBasicConfig> expBasicConfigs = this.list(queryWrapper);
                    expBasicConfigs.forEach(v -> {
                        v.setUnitId(id);
                        v.setModuleId(moduleConfig.getId());
                        v.setId(null);
                        v.setCardStyleId(cardStyleId);
                    });
                    this.saveBatch(expBasicConfigs);

                }
            }

        }

        return expBasicConfigVo;
    }

}