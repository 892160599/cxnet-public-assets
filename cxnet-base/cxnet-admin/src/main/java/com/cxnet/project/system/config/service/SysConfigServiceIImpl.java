package com.cxnet.project.system.config.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cxnet.common.constant.UserConstants;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.project.system.config.domain.SysConfig;
import com.cxnet.project.system.config.mapper.SysConfigMapper;

/**
 * 参数配置 服务层实现
 *
 * @author cxnet
 */
@Service
public class SysConfigServiceIImpl implements SysConfigServiceI {
    @Autowired(required = false)
    private SysConfigMapper configMapper;

    /**
     * 查询参数配置信息
     *
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    @Override
    public SysConfig selectConfigById(String configId) {
        SysConfig config = new SysConfig();
        config.setConfigId(configId);
        return configMapper.selectConfig(config);
    }

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数key
     * @return 参数键值
     */
    @Override
    public Object selectConfigByKey(String configKey) {
        SysConfig config = new SysConfig();
        config.setConfigKey(configKey);
        SysConfig retConfig = configMapper.selectConfig(config);
        return StringUtils.isNotNull(retConfig) ? retConfig.getConfigValue() : "";
    }

    /**
     * 查询参数配置列表
     *
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    @Override
    public List<SysConfig> selectConfigList(SysConfig config) {
        return configMapper.selectConfigList(config);
    }

    /**
     * 新增参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int insertConfig(SysConfig config) {
        return configMapper.insertConfig(config);
    }

    /**
     * 修改参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int updateConfig(SysConfig config) {
        return configMapper.updateConfig(config);
    }

    /**
     * 删除参数配置信息
     *
     * @param configId 参数ID
     * @return 结果
     */
    @Override
    public int deleteConfigById(String configId) {
        return configMapper.deleteConfigById(configId);
    }

    /**
     * 批量删除参数信息
     *
     * @param configIds 需要删除的参数ID
     * @return 结果
     */
    @Override
    public int deleteConfigByIds(String[] configIds) {
        return configMapper.deleteConfigByIds(configIds);
    }

    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public String checkConfigKeyUnique(SysConfig config) {
        String configId = StringUtils.isNull(config.getConfigId()) ? "-1" : config.getConfigId();
        SysConfig info = configMapper.checkConfigKeyUnique(config.getConfigKey());
        if (StringUtils.isNotNull(info) && !info.getConfigId().equals(configId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}
