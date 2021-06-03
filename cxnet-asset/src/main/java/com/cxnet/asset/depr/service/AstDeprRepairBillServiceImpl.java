package com.cxnet.asset.depr.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.asset.businessSet.domain.AstConfig;
import com.cxnet.asset.businessSet.service.AstConfigService;
import com.cxnet.asset.card.domain.AstCard;
import com.cxnet.asset.card.service.AstCardService;
import com.cxnet.asset.depr.domain.AstDeprBill;
import com.cxnet.asset.depr.domain.AstDeprList;
import com.cxnet.asset.depr.domain.AstDeprRepairList;
import com.cxnet.asset.depr.domain.vo.AstDeprRepairVo;
import com.cxnet.asset.depr.mapper.AstDeprRepairBillMapper;
import com.cxnet.asset.depr.domain.AstDeprRepairBill;
import com.cxnet.asset.depr.service.AstDeprRepairBillService;
import com.cxnet.common.constant.Constants;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.project.system.dept.domain.SysDept;
import com.cxnet.project.system.user.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 资产补计提折旧主表(AstDeprRepairBill)表服务实现类
 *
 * @author caixx
 * @since 2021-04-16 18:07:51
 */
@Service
public class AstDeprRepairBillServiceImpl extends ServiceImpl<AstDeprRepairBillMapper, AstDeprRepairBill> implements AstDeprRepairBillService {

    @Autowired(required = false)
    private AstConfigService astConfigService;

    @Autowired(required = false)
    private AstDeprBillService astDeprBillService;

    @Autowired(required = false)
    private AstDeprListService astDeprListService;

    @Autowired(required = false)
    private AstDeprRepairListService astDeprRepairListService;

    @Autowired(required = false)
    private AstCardService astCardService;


    /**
     * 查询主表详情
     *
     * @param fiscal
     * @param deprMo
     * @return
     */
    @Override
    public AstDeprRepairBill getAstDeprRepairBill(Integer fiscal, Integer deprMo) {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        SysDept dept = user.getDept();
        String deptId = dept.getDeptId();
        if (ObjectUtil.isNull(fiscal)) {
            throw new CustomException("请选择年度！");
        }
        if (ObjectUtil.isNull(deprMo)) {
            throw new CustomException("请选择月份！");
        }
        QueryWrapper<AstDeprRepairBill> qw = new QueryWrapper<>();
        qw.lambda().eq(AstDeprRepairBill::getUnitId, deptId)
                .eq(AstDeprRepairBill::getFiscal, fiscal)
                .eq(AstDeprRepairBill::getDeprMo, deprMo);
        AstDeprRepairBill one = this.getOne(qw);
        if (ObjectUtil.isNull(one)) {
            one = new AstDeprRepairBill();
            one.setStatus("0");
            one.setFiscal(fiscal);
            one.setDeprMo(deprMo);
            one.setUnitId(deptId);
            one.setUnitCode(dept.getDeptCode());
            one.setUnitName(dept.getDeptName());
            one.setDelFlag("0");
            one.setCreateBy(user.getUserName());
            one.setCreateName(user.getNickName());
            one.setCreateTime(DateUtil.date());
        }
        // 折旧日期
        AstConfig astConfig = astConfigService.selectByUnitId(deptId);
        one.setDeprNewestDate(astConfig.getDeprNewestDate());
        return one;
    }

    /**
     * 选择资产明细
     *
     * @param astDeprRepairBill
     * @return
     */
    @Override
    public List<AstDeprRepairList> selectAstDetails(AstDeprRepairBill astDeprRepairBill) {
        Integer fiscal = astDeprRepairBill.getFiscal();
        Integer deprMo = astDeprRepairBill.getDeprMo();
        List<AstDeprRepairList> astDeprRepairLists = new ArrayList<>();
        if (ObjectUtil.isNull(fiscal)) {
            throw new CustomException("请选择年度！");
        }
        if (ObjectUtil.isNull(deprMo)) {
            throw new CustomException("请选择月份！");
        }
        // 校验折旧日期
        check(fiscal, deprMo);
        AstDeprBill astDeprBill = new AstDeprBill();
        astDeprBill.setFiscal(fiscal);
        astDeprBill.setDeprMo(deprMo);
        astDeprBill = astDeprBillService.selectAstDeprByAstDeprBill(astDeprBill);
        if (StringUtils.isNotEmpty(astDeprBill.getId())) {
            QueryWrapper<AstDeprList> qw = new QueryWrapper<>();
            qw.lambda().eq(AstDeprList::getAstDeprId, astDeprBill.getId());
            List<AstDeprList> list = astDeprListService.list(qw);
            list.forEach(v -> {
                AstDeprRepairList astDeprRepairList = new AstDeprRepairList();
                BeanUtil.copyProperties(v, astDeprRepairList);
                astDeprRepairLists.add(astDeprRepairList);
            });
        }
        return astDeprRepairLists;
    }

    /**
     * 确认
     *
     * @param astDeprRepairVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String confirm(AstDeprRepairVo astDeprRepairVo) {
        AstDeprRepairBill astDeprRepairBill = astDeprRepairVo.getAstDeprRepairBill();
        if (StringUtils.isNotEmpty(astDeprRepairBill.getId())) {
            AstDeprRepairBill byId = this.getById(astDeprRepairBill.getId());
            if ("2".equals(byId.getStatus())) {
                throw new CustomException("单据已确认！");
            }
        }
        Integer fiscal = astDeprRepairBill.getFiscal();
        Integer deprMo = astDeprRepairBill.getDeprMo();
        // 校验折旧日期
        check(fiscal, deprMo);
        // 删除原记录
        if (StringUtils.isNotEmpty(astDeprRepairBill.getId())) {
            QueryWrapper<AstDeprRepairList> qw = new QueryWrapper<>();
            qw.lambda().eq(AstDeprRepairList::getAstDeprId, astDeprRepairBill.getId());
            astDeprRepairListService.remove(qw);
        }
        astDeprRepairBill.setStatus("2");
        astDeprRepairBill.setConfirmedId(SecurityUtils.getLoginUser().getUser().getUserId());
        astDeprRepairBill.setConfirmedName(SecurityUtils.getLoginUser().getUser().getNickName());
        astDeprRepairBill.setConfirmedDate(DateUtil.date());
        this.saveOrUpdate(astDeprRepairBill);
        List<AstDeprRepairList> astDeprRepairLists = astDeprRepairVo.getAstDeprRepairLists();
        if (CollectionUtil.isNotEmpty(astDeprRepairLists)) {
            List<String> astIds = astDeprRepairLists.stream().map(AstDeprRepairList::getAstId).distinct().collect(Collectors.toList());
            List<AstCard> astCards = astCardService.listByIds(astIds);
            List<AstCard> astCardsFilter = astCards.stream().filter(v -> "1".equals(v.getInitialState()) && Constants.YES.equals(v.getDelFlag())).collect(Collectors.toList());
            if (astIds.size() != astDeprRepairLists.size()) {
                throw new CustomException("请勿重复选择卡片！");
            }
            if (astCards.size() != astCardsFilter.size()) {
                throw new CustomException("补计提明细存在业务未完成卡片，请确认后重试！");
            }
            astDeprRepairLists.forEach(v -> v.setAstDeprId(astDeprRepairBill.getId()));
            // 更新卡片累计折旧和净值
            Map<String, BigDecimal> astDeprRepairValue = astDeprRepairLists.stream().collect(Collectors.toMap(AstDeprRepairList::getAstId, AstDeprRepairList::getDeprRepairValue));
            astCardsFilter.forEach(v -> {
                BigDecimal deprRepairValue = astDeprRepairValue.get(v.getId());
                v.setDepTotal(v.getDepTotal().add(deprRepairValue));
                v.setNetValue(v.getNetValue().add(deprRepairValue.negate()));
            });
            astCardService.saveOrUpdateBatch(astCardsFilter);
            // 保存明细表
            astDeprRepairListService.saveOrUpdateBatch(astDeprRepairLists);
        }
        return "确认成功！";
    }

    /**
     * 取消确认
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String cancelConfirm(String id) {
        AstDeprRepairBill byId = this.getById(id);
        if (byId == null) {
            throw new CustomException("单据不存在！");
        }
        // 校验折旧日期
        check(byId.getFiscal(), byId.getDeprMo());
        if ("0".equals(byId.getStatus())) {
            throw new CustomException("当前单据状态不可取消确认，请刷新后重试！");
        }
        byId.setStatus("0");
        byId.setConfirmedDate(null);
        this.saveOrUpdate(byId);
        QueryWrapper<AstDeprRepairList> qw = new QueryWrapper<>();
        qw.lambda().eq(AstDeprRepairList::getAstDeprId, byId.getId());
        List<AstDeprRepairList> astDeprRepairLists = astDeprRepairListService.list(qw);
        // 还原卡片表金额
        if (CollectionUtil.isNotEmpty(astDeprRepairLists)) {
            List<String> astIds = astDeprRepairLists.stream().map(AstDeprRepairList::getAstId).distinct().collect(Collectors.toList());
            List<AstCard> astCards = astCardService.listByIds(astIds);
            List<AstCard> astCardsFilter = astCards.stream().filter(v -> "1".equals(v.getInitialState()) && Constants.YES.equals(v.getDelFlag())).collect(Collectors.toList());
            if (astCards.size() != astCardsFilter.size()) {
                throw new CustomException("补计提明细存在业务未完成卡片，请确认后重试！");
            }
            // 更新卡片累计折旧和净值
            Map<String, BigDecimal> astDeprRepairValue = astDeprRepairLists.stream().collect(Collectors.toMap(AstDeprRepairList::getAstId, AstDeprRepairList::getDeprRepairValue));
            astCardsFilter.forEach(v -> {
                BigDecimal deprRepairValue = astDeprRepairValue.get(v.getId());
                v.setDepTotal(v.getDepTotal().add(deprRepairValue.negate()));
                v.setNetValue(v.getNetValue().add(deprRepairValue));
            });
            astCardService.saveOrUpdateBatch(astCardsFilter);
        }
        // 删除记录
        deleteById(byId.getId());
        return "取消确认成功！";
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteById(String id) {
        AstDeprRepairBill byId = this.getById(id);
        // 校验折旧日期
        check(byId.getFiscal(), byId.getDeprMo());
        if (!"0".equals(byId.getStatus())) {
            throw new CustomException("当前单据状态不可删除，请刷新后重试！");
        }
        QueryWrapper<AstDeprRepairList> qw = new QueryWrapper<>();
        qw.lambda().eq(AstDeprRepairList::getAstDeprId, id);
        astDeprRepairListService.remove(qw);
        this.removeById(id);
        return "删除成功！";
    }

    /**
     * 校验折旧日期
     *
     * @param fiscal
     * @param deprMo
     */
    private void check(Integer fiscal, Integer deprMo) {
        if (ObjectUtil.isNull(fiscal) || ObjectUtil.isNull(deprMo)) {
            throw new CustomException("缺少参数！");
        }
        // 校验折旧日期
        String deptId = SecurityUtils.getLoginUser().getUser().getDeptId();
        AstConfig astConfig = astConfigService.selectByUnitId(deptId);
        Date deprNewestDate = astConfig.getDeprNewestDate();
        // 最新折旧期间
        Integer deprNewestYear = DateUtil.year(deprNewestDate);
        Integer deprNewestMonth = DateUtil.month(deprNewestDate) + 1;
        DateTime dateTime = DateUtil.parse(fiscal + "-" + (deprMo < 10 ? "0" + deprMo : deprMo), "yyyy-MM");
        // 校验所选月份是否可补计提折旧
        if (DateUtil.year(dateTime) != deprNewestYear || (DateUtil.month(dateTime) + 1) != deprNewestMonth) {
            throw new CustomException("最新可补计提折旧期间为" + deprNewestYear + "年" + deprNewestMonth + "月,请确认后重试！");
        }
    }

}