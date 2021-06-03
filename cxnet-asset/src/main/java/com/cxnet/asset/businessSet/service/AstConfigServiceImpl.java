package com.cxnet.asset.businessSet.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.asset.businessSet.mapper.AstConfigMapper;
import com.cxnet.asset.businessSet.domain.AstConfig;
import com.cxnet.asset.businessSet.service.AstConfigService;
import com.cxnet.asset.card.service.AstCardService;
import com.cxnet.asset.depr.service.AstDeprBillService;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.project.system.dept.domain.SysDept;
import com.cxnet.project.system.dept.service.SysDeptServiceI;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 资产参数设置(AstConfig)表服务实现类
 *
 * @author caixx
 * @since 2021-04-06 09:41:42
 */
@Service
public class AstConfigServiceImpl extends ServiceImpl<AstConfigMapper, AstConfig> implements AstConfigService {

    @Resource
    private SysDeptServiceI sysDeptServiceI;

    @Resource
    private AstCardService astCardService;

    @Resource
    private AstDeprBillService astDeprBillService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AstConfig selectByUnitId(String unitId) {
        if (StringUtils.isEmpty(unitId)) {
            throw new CustomException("请指定单位！");
        }
        SysDept sysDept = sysDeptServiceI.selectDeptById(unitId);
        if (ObjectUtil.isNull(sysDept)) {
            throw new CustomException("单位不存在，请确认后重试！");
        }
        QueryWrapper<AstConfig> qw = new QueryWrapper<>();
        qw.lambda().eq(AstConfig::getUnitId, unitId);
        AstConfig one = this.getOne(qw);
        if (ObjectUtil.isNull(one)) {
            // 查询通用配置
            QueryWrapper<AstConfig> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(AstConfig::getUnitId, "*");
            AstConfig astConfig = this.getOne(queryWrapper);
            if (astConfig == null) {
                throw new CustomException("资产配置错误，请联系管理员！");
            }
            one = astConfig;
            if (astConfig.getDeprStartDate() == null) {
                astConfig.setDeprStartDate(DateUtil.parseDate("1970-01-01"));
            }
            one.setDeprStartDate(astConfig.getDeprStartDate());
            one.setDeprNewestDate(DateUtil.offsetMonth(astConfig.getDeprStartDate(), -1));
            one.setUnitId(unitId);
            one.setId(null);
            this.save(one);
            return this.getOne(qw);
        }
        return one;
    }

    @Override
    public AstConfig update(AstConfig astConfig) {
        String unitId = astConfig.getUnitId();
        Date deprStartDate = astConfig.getDeprStartDate();
        if (deprStartDate == null) {
            throw new CustomException("请指定折旧启用期间！");
        }
        QueryWrapper<AstConfig> qw = new QueryWrapper<>();
        qw.lambda().eq(AstConfig::getUnitId, unitId);
//        AstConfig one = this.getOne(qw);
//        if (deprStartDate.compareTo(one.getDeprStartDate()) != 0) {
//            int astCardCount = astCardService.count();
        int astDeprBillCount = astDeprBillService.count();
        if (/*astCardCount > 0 ||*/ astDeprBillCount > 0) {
            throw new CustomException("系统中已存在折旧数据，若要继续操作，请取消所有折旧后再进行设置！");
        }
//        }
        astConfig.setDeprNewestDate(DateUtil.offsetMonth(astConfig.getDeprStartDate(), -1));
        this.updateById(astConfig);
        return astConfig;
    }

}