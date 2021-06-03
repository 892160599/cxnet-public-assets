package com.cxnet.asset.businessSet.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.asset.businessSet.mapper.AstMapConfigMapper;
import com.cxnet.asset.businessSet.domain.AstMapConfig;
import com.cxnet.common.exception.CustomException;
import com.cxnet.framework.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 资产地图配置(AstMapConfig)表服务实现类
 *
 * @author caixx
 * @since 2021-04-25 14:06:24
 */
@Service
public class AstMapConfigServiceImpl extends ServiceImpl<AstMapConfigMapper, AstMapConfig> implements AstMapConfigService {

    /**
     * 查询当前单位地图配置信息
     *
     * @return
     */
    @Override
    public AstMapConfig getThisAstMapConfig() {
        String deptId = SecurityUtils.getLoginUser().getUser().getDept().getDeptId();
        QueryWrapper<AstMapConfig> qw = new QueryWrapper<>();
        qw.lambda().eq(AstMapConfig::getUnitId, deptId);
        AstMapConfig astMapConfig = this.getOne(qw);
        if (astMapConfig == null) {
            // 查询默认配置
            qw = new QueryWrapper<>();
            qw.lambda().eq(AstMapConfig::getUnitId, "*");
            astMapConfig = this.getOne(qw);
            if (astMapConfig == null) {
                // 生成默认配置
                astMapConfig = new AstMapConfig();
                astMapConfig.setUnitId("*");
                astMapConfig.setDefaultLevel(10);
                astMapConfig.setMaxLevel(17);
                astMapConfig.setDefaultLong(new BigDecimal("118.796539"));
                astMapConfig.setDefaultLat(new BigDecimal("32.058441"));
                this.save(astMapConfig);
            }
            // 新增当前单位配置
            astMapConfig.setUnitId(deptId);
            astMapConfig.setId(null);
            this.save(astMapConfig);
        }
        return astMapConfig;
    }
}