package com.cxnet.asset.depr.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.asset.astchange.domain.AstAstchangeList;
import com.cxnet.asset.astchange.service.AstAstchangeBillService;
import com.cxnet.asset.astchange.service.AstAstchangeListService;
import com.cxnet.asset.businessSet.domain.AstConfig;
import com.cxnet.asset.businessSet.service.AstConfigService;
import com.cxnet.asset.businessSet.service.AstDeprMethodService;
import com.cxnet.asset.card.domain.AstCard;
import com.cxnet.asset.card.service.AstCardService;
import com.cxnet.asset.depr.domain.AstDeprList;
import com.cxnet.asset.depr.domain.AstDeprRepairBill;
import com.cxnet.asset.depr.domain.vo.AstDeprMoVo;
import com.cxnet.asset.depr.mapper.AstDeprBillMapper;
import com.cxnet.asset.depr.domain.AstDeprBill;
import com.cxnet.asset.depr.service.AstDeprBillService;
import com.cxnet.asset.empchange.domain.AstEmpchangeList;
import com.cxnet.asset.empchange.service.AstEmpchangeListService;
import com.cxnet.baseData.assetType.domain.BdAssetType;
import com.cxnet.baseData.assetType.service.BdAssetTypeService;
import com.cxnet.common.constant.Constants;
import com.cxnet.common.constant.ProcessConstant;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.project.system.dept.domain.SysDept;
import com.cxnet.project.system.dept.service.SysDeptServiceI;
import com.cxnet.project.system.dict.service.SysDictDataServiceI;
import com.cxnet.project.system.user.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 资产折旧主表(AstDeprBill)表服务实现类
 *
 * @author caixx
 * @since 2021-04-08 16:03:11
 */
@Service
public class AstDeprBillServiceImpl extends ServiceImpl<AstDeprBillMapper, AstDeprBill> implements AstDeprBillService {

    @Autowired(required = false)
    private AstConfigService astConfigService;

    @Autowired(required = false)
    private AstDeprListService astDeprListService;

    @Autowired(required = false)
    private AstCardService astCardService;

    @Autowired(required = false)
    private AstDeprRepairBillService astDeprRepairBillService;

    @Resource
    private AstAstchangeBillService astAstchangeBillService;

    @Resource
    private AstAstchangeListService astAstchangeListService;

    @Autowired(required = false)
    private BdAssetTypeService bdAssetTypeService;

    @Autowired(required = false)
    private SysDictDataServiceI sysDictDataServiceI;

    @Autowired(required = false)
    private AstEmpchangeListService astEmpchangeListService;

    /**
     * 查询折旧年限
     *
     * @return
     */
    @Override
    public List<Integer> getDeprFiscal() {
        String deptId = SecurityUtils.getLoginUser().getUser().getDeptId();
        AstConfig astConfig = astConfigService.selectByUnitId(deptId);
        Date deprStartDate = astConfig.getDeprStartDate();
        int configYear = DateUtil.year(deprStartDate);
        int year = DateUtil.thisYear();
        List<Integer> years = new ArrayList<>();
        for (int i = configYear; i <= year; i++) {
            years.add(i);
        }
        return years;
    }

    /**
     * 根据折旧年限查询折旧月份
     *
     * @param deprFiscal
     * @return
     */
    @Override
    public List<AstDeprMoVo> getDeprMoByDeprFiscal(Integer deprFiscal) {
        String deptId = SecurityUtils.getLoginUser().getUser().getDeptId();
        List<AstDeprMoVo> astDeprMoVos = new ArrayList<>();
        AstConfig astConfig = astConfigService.selectByUnitId(deptId);
        Date deprStartDate = astConfig.getDeprStartDate();
        Date deprNewestDate = astConfig.getDeprNewestDate();
        // 折旧启用期间
        int configYear = DateUtil.year(deprStartDate);
        int configMonth = DateUtil.month(deprStartDate) + 1;
        // 最新折旧期间
        int deprNewestYear = DateUtil.year(deprNewestDate);
        int deprNewestMonth = DateUtil.month(deprNewestDate) + 1;
        AstDeprMoVo astDeprMoVo = new AstDeprMoVo();
        astDeprMoVo.setDeprMo(1);
        // 查询补计提记录
        QueryWrapper<AstDeprRepairBill> qw = new QueryWrapper<>();
        qw.lambda().eq(AstDeprRepairBill::getUnitId, deptId)
                .eq(AstDeprRepairBill::getFiscal, deprFiscal);
        List<AstDeprRepairBill> list = astDeprRepairBillService.list(qw);
        Map<Integer, String> deprMoMap = list.stream().collect(Collectors.toMap(AstDeprRepairBill::getDeprMo, AstDeprRepairBill::getStatus));
        // 选中年度等于折旧启用年度 折旧月份从折旧启用月份开始
        if (deprFiscal == configYear) {
            astDeprMoVo.setDeprMo(configMonth);
        }
        if (deprFiscal <= deprNewestYear && astDeprMoVo.getDeprMo() <= deprNewestMonth) {
            astDeprMoVo.setIsDepr(Constants.YES);
        }
        String status = deprMoMap.get(astDeprMoVo.getDeprMo());
        if (StringUtils.isNotEmpty(status) && "2".equals(status)) {
            astDeprMoVo.setIsSupplementaryProvision(Constants.YES);
        }
        astDeprMoVos.add(astDeprMoVo);
        // 设置月份是否折旧，是否补计提折旧
        for (int i = astDeprMoVo.getDeprMo() + 1; i <= 12; i++) {
            if (deprFiscal == DateUtil.thisYear() && i > DateUtil.thisMonth() + 1) {
                break;
            }
            astDeprMoVo = new AstDeprMoVo();
            astDeprMoVo.setDeprMo(i);
            // 是否折旧
            if (deprFiscal <= deprNewestYear && i <= deprNewestMonth) {
                astDeprMoVo.setIsDepr(Constants.YES);
            }
            // 是否补计提折旧
            status = deprMoMap.get(i);
            if (StringUtils.isNotEmpty(status) && "2".equals(status)) {
                astDeprMoVo.setIsSupplementaryProvision(Constants.YES);
            }
            astDeprMoVos.add(astDeprMoVo);
        }
        return astDeprMoVos;
    }

    /**
     * 查询主表详情
     *
     * @param astDeprBill
     * @return
     */
    @Override
    public AstDeprBill selectAstDeprByAstDeprBill(AstDeprBill astDeprBill) {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        SysDept dept = user.getDept();
        String deptId = dept.getDeptId();
        Integer fiscal = astDeprBill.getFiscal();
        Integer deprMo = astDeprBill.getDeprMo();
        if (ObjectUtil.isNull(fiscal)) {
            throw new CustomException("请选择年度！");
        }
        if (ObjectUtil.isNull(deprMo)) {
            throw new CustomException("请选择月份！");
        }
        QueryWrapper<AstDeprBill> qw = new QueryWrapper<>();
        qw.lambda().eq(AstDeprBill::getUnitId, deptId)
                .eq(AstDeprBill::getFiscal, fiscal)
                .eq(AstDeprBill::getDeprMo, deprMo);
        AstDeprBill one = this.getOne(qw);
        if (ObjectUtil.isNull(one)) {
            one = new AstDeprBill();
            one.setStatus(Constants.YES);
            one.setFiscal(fiscal);
            one.setDeprMo(deprMo);
            one.setUnitId(deptId);
            one.setUnitCode(dept.getDeptCode());
            one.setUnitName(dept.getDeptName());
            one.setDelFlag(Constants.YES);
            one.setCreateBy(user.getUserName());
            one.setCreateName(user.getNickName());
            one.setCreateTime(DateUtil.date());
            one.setBillNo("ZJ_".concat(dept.getDeptCode()).concat("_").concat(fiscal.toString()).concat("_").concat(deprMo < 10 ? "0" + deprMo : deprMo.toString()));
        }
        return one;
    }

    /**
     * 计算操作
     *
     * @param astDeprBill
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String calculation(AstDeprBill astDeprBill) {
        astDeprBill = selectAstDeprByAstDeprBill(astDeprBill);
        Integer fiscal = astDeprBill.getFiscal();
        Integer deprMo = astDeprBill.getDeprMo();
        String deptId = SecurityUtils.getLoginUser().getUser().getDeptId();
        AstConfig astConfig = astConfigService.selectByUnitId(deptId);
        Date deprNewestDate = astConfig.getDeprNewestDate();
        // 最新折旧期间
        Integer deprNewestYear = DateUtil.year(deprNewestDate);
        Integer deprNewestMonth = DateUtil.month(deprNewestDate) + 1;
        Date dateTime = DateUtil.offsetMonth(DateUtil.parse(fiscal + "-" + deprMo, "yyyy-MM"), -1);
        // 校验所选月份是否可折旧
        if (DateUtil.year(dateTime) != deprNewestYear || (DateUtil.month(dateTime) + 1) != deprNewestMonth) {
            DateTime newDateTime = DateUtil.offsetMonth(DateUtil.parse(deprNewestYear + "-" + deprNewestMonth, "yyyy-MM"), 1);
            throw new CustomException("最新可折旧期间为" + DateUtil.year(newDateTime) + "年" + (DateUtil.month(newDateTime) + 1) + "月,请确认后重试！");
        }
        // 删除原计算记录
        astDeprBill = this.selectAstDeprByAstDeprBill(astDeprBill);
        if (StringUtils.isNotEmpty(astDeprBill.getId())) {
            astDeprBill.setCost(BigDecimal.ZERO);
            astDeprBill.setDepTotal(BigDecimal.ZERO);
            astDeprBill.setNetValue(BigDecimal.ZERO);
            astDeprBill.setQty(0);
            astDeprBill.setThisValue(BigDecimal.ZERO);
            QueryWrapper<AstDeprList> qw = new QueryWrapper<>();
            qw.lambda().eq(AstDeprList::getAstDeprId, astDeprBill.getId());
            astDeprListService.remove(qw);
        } else {
            this.saveOrUpdate(astDeprBill);
        }
        // 查询需要折旧的卡片
        QueryWrapper<AstCard> qw = new QueryWrapper<>();
        qw.lambda().eq(AstCard::getAstStatus, "1")
                .eq(AstCard::getUnitId, deptId)
                .le(AstCard::getStartUsedate, dateTime)
                .gt(AstCard::getDepreciationMo, 0)
                .gt(AstCard::getNetValue, new BigDecimal(Constants.YES))
                .apply("deped_mo < use_life");
        List<AstCard> list = astCardService.list(qw);
        if (CollectionUtil.isNotEmpty(list)) {
            // 参数设置处理
            astConfig(list, astConfig, fiscal, deprMo);
            // 要素校验
            for (AstCard astCard : list) {
                String astCode = astCard.getAstCode();
                if ("0".equals(astCard.getInitialState())) {
                    throw new CustomException("资产卡片【".concat(astCode).concat("】存在未完成业务，请确认后重试！"));
                }
                if (astCard.getUseLife() == null) {
                    throw new CustomException("资产卡片【".concat(astCode).concat("】未指定预计使用月份，请确认后重试！"));
                }
                if (StringUtils.isEmpty(astCard.getCategoryId()) || StringUtils.isEmpty(astCard.getCategoryCode())) {
                    throw new CustomException("资产卡片【".concat(astCode).concat("】未指定资产类别，请确认后重试！"));
                }
                if (astCard.getUseLife() == null) {
                    throw new CustomException("资产卡片【".concat(astCode).concat("】未定义折旧方法，请确认后重试！"));
                }
            }
            // 计算折旧
            depreciation(astDeprBill, list, fiscal, deprMo, astConfig);
        }
        astDeprBill.setStatus("1");
        this.updateById(astDeprBill);
        return "计算成功！";
    }

    /**
     * 参数设置相关操作
     *
     * @param astCards
     * @param astConfig
     * @param fiscal
     * @param deprMo
     */
    private void astConfig(List<AstCard> astCards, AstConfig astConfig, Integer fiscal, Integer deprMo) {
        List<String> astCardIds = astCards.stream().map(AstCard::getId).collect(Collectors.toList());
        // 使用状态变更当期生效 默认是   来自资产处置
        String usageStatusChange = astConfig.getUsageStatusChange();
        // 折旧方法变更当期生效 默认是  目前只有年限平均法
        String deprMethodChange = astConfig.getDeprMethodChange();
        // 预计使用月限变更当期生效 默认是
        String useLifeChange = astConfig.getUseLifeChange();
        // 资产类别变更当期折旧归属 默认调整前的资产类别
        String assetTypeChange = astConfig.getAssetTypeChange();
        // 使用部门变更当期折旧归属 默认调整前的使用部门
        String departmentChange = astConfig.getDepartmentChange();
        // 资产变动集合
        Map<String, AstAstchangeList> astAstchangeListMap = null;
        // 资产使用人变动集合
        Map<String, AstEmpchangeList> astEmpchangeListMap = null;
        //资产类别变更当期折旧归属  资产变动
        if ("1".equals(assetTypeChange)) {
            // 查询当月变更记录
            List<AstAstchangeList> astAstchangeLists = astAstchangeListService.getThisMoAstchange(fiscal + "-" + deprMo, astCardIds);
            // 查询本单位资产类别
            BdAssetType bdAssetType = new BdAssetType();
            bdAssetType.setUnitId(SecurityUtils.getLoginUser().getUser().getDeptId());
            List<BdAssetType> bdAssetTypes = bdAssetTypeService.selectAssetTypeList(bdAssetType);
            Map<String, BdAssetType> assetTypeMap = bdAssetTypes.stream().collect(Collectors.toMap(BdAssetType::getAssetCode, Function.identity()));
            if (CollectionUtil.isNotEmpty(astAstchangeLists) && CollectionUtil.isNotEmpty(assetTypeMap)) {
                astAstchangeListMap = astAstchangeLists.stream().collect(Collectors.toMap(AstAstchangeList::getAstId, Function.identity()));
                // 资产大类值集
                Map<String, String> astClassMap = sysDictDataServiceI.selectDictDataMapByType("ast_type");
                for (AstCard astCard : astCards) {
                    AstAstchangeList astAstchangeList = astAstchangeListMap.get(astCard.getId());
                    if (astAstchangeList != null) {
                        BdAssetType beforeBdAssetType = assetTypeMap.get(astAstchangeList.getCategoryBeforeCode());
                        if (beforeBdAssetType != null) {
                            // 设置资产大类、资产类别为变更前的
                            astCard.setCategoryId(beforeBdAssetType.getAssetId());
                            astCard.setCategoryCode(beforeBdAssetType.getAssetCode());
                            astCard.setCategoryName(beforeBdAssetType.getAssetName());
                            astCard.setClassCode(beforeBdAssetType.getClassification());
                            astCard.setClassName(astClassMap.get(beforeBdAssetType.getClassification()));
                        }
                    }
                }
            }
        }
        //预计使用月限变更当期生效
        if ("2".equals(useLifeChange) && CollectionUtil.isNotEmpty(astAstchangeListMap)) {
            // 调整前的使用月限
            for (AstCard v : astCards) {
                AstAstchangeList astAstchangeList = astAstchangeListMap.get(v.getId());
                if (astAstchangeList != null) {
                    // 设置预计使用月份为变动前的预计使用月份
                    v.setUseLife(astAstchangeList.getUseBeforeLife());
                }
            }
        }

        //使用部门变更当期折旧归属   使用人变动
        if ("1".equals(departmentChange)) {
            // 查询当月资产使用人变动记录
            List<AstEmpchangeList> astEmpchangeLists = astEmpchangeListService.getThisMoAstchange(fiscal + "-" + deprMo, astCardIds);
            if (CollectionUtil.isNotEmpty(astEmpchangeLists)) {
                astEmpchangeListMap = astEmpchangeLists.stream().collect(Collectors.toMap(AstEmpchangeList::getAstId, Function.identity()));
                for (AstCard v : astCards) {
                    AstEmpchangeList astEmpchangeList = astEmpchangeListMap.get(v.getId());
                    if (astEmpchangeList != null) {
                        // 设置使用部门为变动前的使用部门
                        v.setDepartmentId(astEmpchangeList.getDepartmentBeforeId());
                        v.setDepartmentCode(astEmpchangeList.getDepartmentBeforeCode());
                        v.setDepartmentName(astEmpchangeList.getDepartmentBeforeName());
                    }
                }
            }
        }

    }


    /**
     * 折旧计算
     *
     * @param astDeprBill
     * @param list
     * @param fiscal
     * @param deprMo
     * @param astConfig
     */
    private void depreciation(AstDeprBill astDeprBill, List<AstCard> list, Integer fiscal, Integer deprMo, AstConfig astConfig) {
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        List<AstDeprList> astDeprLists = new ArrayList<>();
        for (AstCard astCard : list) {
            String astCode = astCard.getAstCode();
            String depMethod = StringUtils.isEmpty(astCard.getDepMethod()) ? "" : astCard.getDepMethod();
            switch (depMethod) {
                // 平均年限法
                case "01":
                    AstDeprList astDeprList = straightLine(astDeprBill, astCard, fiscal, deprMo, astConfig);
                    if (ObjectUtil.isNotNull(astDeprList)) {
                        astDeprLists.add(astDeprList);
                    }
                    break;
                // 不计提
                case "04":
                    continue;
                    // 工作量法
                case "02":
                    // 一次摊销法
                case "03":
                default:
                    throw new CustomException("资产卡片【".concat(astCode).concat("】未定义折旧方法，请确认后重试！"));
            }
        }
        Map<String, AstCard> astCardMap = list.stream().collect(Collectors.toMap(AstCard::getId, Function.identity()));
        for (AstDeprList astDeprList : astDeprLists) {
            AstCard astCard = astCardMap.get(astDeprList.getAstId());
            // 保存原卡片折旧数据
            astDeprList.setCardDepedMo(astCard.getDepedMo());
            astDeprList.setCardDepTotal(astCard.getDepTotal());
            astDeprList.setCardDepreciationMo(astCard.getDepreciationMo());
            astDeprList.setCardDepMoValue(astCard.getDepMoValue());
            astDeprList.setCardNetValue(astCard.getNetValue());
            // 合计
            astDeprBill.setCost(astDeprBill.getCost().add(astDeprList.getCost()));
            astDeprBill.setDepTotal(astDeprBill.getDepTotal().add(astDeprList.getDepTotal()));
            astDeprBill.setNetValue(astDeprBill.getNetValue().add(astDeprList.getNetValue()));
            astDeprBill.setQty(astDeprBill.getQty() + astDeprList.getQty());
            astDeprBill.setThisValue(astDeprBill.getThisValue().add(astDeprList.getThisValue()));
        }
        astDeprListService.saveBatch(astDeprLists);
    }

    /**
     * 平均年限法
     *
     * @param astDeprBill
     * @param astCard
     * @param fiscal
     * @param deprMo
     * @param astConfig
     */
    private AstDeprList straightLine(AstDeprBill astDeprBill, AstCard astCard, Integer fiscal, Integer deprMo, AstConfig astConfig) {
        AstDeprList astDeprList = new AstDeprList();
        BeanUtil.copyProperties(astCard, astDeprList);
        astDeprList.setAstDeprId(astDeprBill.getId());
        astDeprList.setAstId(astCard.getId());
        astDeprList.setFiscal(astDeprBill.getFiscal());
        astDeprList.setDeprMo(astDeprBill.getDeprMo());
        astDeprList.setId(UUID.randomUUID().toString());
        Date deprStartDate = astConfig.getDeprStartDate();
        // 已折旧月份
        BigDecimal depedMo = astCard.getDepedMo() == null ? BigDecimal.ZERO : new BigDecimal(astCard.getDepedMo());
        // 本次折旧日期
        Date thisDeprStartDate = DateUtil.parse(fiscal + "-" + deprMo, "yyyy-MM");
        BigDecimal betweenMonth = new BigDecimal(DateUtil.betweenMonth(thisDeprStartDate, astCard.getStartUsedate(), true));
        // 本次折旧月份 = 本次折旧期间、折旧开始期间月份差 - 已折旧月份
        BigDecimal thisDeprStartMonth = betweenMonth.subtract(depedMo);
        if (thisDeprStartMonth.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }
        // 原值
        BigDecimal cost = astCard.getCost();
        // 预计使用月份
        BigDecimal useLife = BigDecimal.valueOf(astCard.getUseLife());
        // 累计折旧
        BigDecimal depTotal = astCard.getDepTotal() == null ? BigDecimal.ZERO : astCard.getDepTotal();
        // 净值
        BigDecimal netValue;
        // 月折旧额
        BigDecimal depMoValue;
        // 本次折旧额
        BigDecimal thisValue;
        if (depedMo.compareTo(BigDecimal.ZERO) == 0) {
            // 首次折旧 原值/使用年限（月）=月折旧额
            depMoValue = cost.divide(useLife, 2, BigDecimal.ROUND_HALF_UP);
        } else {
            // 新月折旧额=（调整后的原值-累计折旧）/（调整后的折旧年限-已提折旧月数）
            depMoValue = cost.subtract(depTotal).divide(useLife.subtract(depedMo), 2, BigDecimal.ROUND_HALF_UP);
        }
        thisValue = depMoValue.multiply(thisDeprStartMonth);
        // 净值 = 原值-累计折旧-本次折旧
        netValue = cost.subtract(depTotal).subtract(thisValue);
        // 尾差操作: 最后一个月将未提完的金额一次提完
        if (netValue.compareTo(BigDecimal.ZERO) < 0) {
            // 净值归零
            netValue = BigDecimal.ZERO;
            // 本次折旧 = 卡片净值
            thisValue = astCard.getNetValue();
            // 本次折旧月份 = 预计使用月份 - 已使用月份
            thisDeprStartMonth = useLife.subtract(depedMo);
        }
        // 净值
        astDeprList.setNetValue(netValue);
        // 已折旧月份 = 已使用月份 + 本次折旧月份
        astDeprList.setDepedMo(depedMo.add(thisDeprStartMonth).intValue());
        // 待折旧月份 = 预计使用月份 - 已折旧月份
        astDeprList.setDepreciationMo(useLife.intValue() - astDeprList.getDepedMo());
        // 月折旧额
        astDeprList.setDepMoValue(depMoValue);
        // 本次折旧额
        astDeprList.setThisValue(thisValue);
        // 本次折月份
        astDeprList.setThisMo(thisDeprStartMonth.intValue());
        // 累计折旧
        astDeprList.setDepTotal(astDeprList.getDepTotal().add(astDeprList.getThisValue()));
        return astDeprList;
    }

    /**
     * 确认
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String confirm(String id) {
        AstDeprBill astDeprBill = this.getById(id);
        if (!"1".equals(astDeprBill.getStatus())) {
            throw new CustomException("当前单据状态不可确认！");
        }
        Integer fiscal = astDeprBill.getFiscal();
        Integer deprMo = astDeprBill.getDeprMo();
        // 校验折旧日期
        String deptId = SecurityUtils.getLoginUser().getUser().getDeptId();
        AstConfig astConfig = astConfigService.selectByUnitId(deptId);
        Date deprNewestDate = astConfig.getDeprNewestDate();
        // 最新折旧期间
        Integer deprNewestYear = DateUtil.year(deprNewestDate);
        Integer deprNewestMonth = DateUtil.month(deprNewestDate) + 1;
        DateTime dateTime = DateUtil.offsetMonth(DateUtil.parse(fiscal + "-" + (deprMo < 10 ? "0" + deprMo : deprMo), "yyyy-MM"), -1);
        // 校验所选月份是否可折旧
        if (DateUtil.year(dateTime) != deprNewestYear || (DateUtil.month(dateTime) + 1) != deprNewestMonth) {
            DateTime newDateTime = DateUtil.offsetMonth(DateUtil.parse(deprNewestYear + "-" + (deprNewestMonth < 10 ? "0" + deprNewestMonth : deprNewestMonth), "yyyy-MM"), 1);
            throw new CustomException("最新可确认折旧期间为" + DateUtil.year(newDateTime) + "年" + (DateUtil.month(newDateTime) + 1) + "月,请确认后重试！");
        }
        // 重新计算，校验最新计算结果和已经计算结果是否吻合
        calculation(astDeprBill);
        AstDeprBill newAstDeprBill = this.getById(astDeprBill.getId());
        if (!astDeprBill.equalsAstDeprBill(newAstDeprBill)) {
            throw new CustomException("最新计算结果发生变化，请重新计算后再进行确认操作！");
        }
        // 查询计算结果
        List<AstDeprList> astDeprLists = astDeprListService.getByDeptId(id);
        // 更新资产卡片
        if (CollectionUtil.isNotEmpty(astDeprLists)) {
            Map<String, AstDeprList> astDeprListMap = astDeprLists.stream().collect(Collectors.toMap(AstDeprList::getAstId, Function.identity()));
            List<AstCard> astCards = astCardService.listByIds(astDeprListMap.keySet());
            for (AstCard astCard : astCards) {
                AstDeprList astDeprList = astDeprListMap.get(astCard.getId());
                astCard.setDepedMo(astDeprList.getDepedMo());
                astCard.setDepTotal(astDeprList.getDepTotal());
                astCard.setDepreciationMo(astDeprList.getDepreciationMo());
                astCard.setDepMoValue(astDeprList.getDepMoValue());
                astCard.setNetValue(astDeprList.getNetValue());
            }
            astCardService.updateBatchById(astCards);
        }
        // 更新最新折旧期间
        deprNewestDate = DateUtil.offsetMonth(deprNewestDate, 1);
        astConfig.setDeprNewestDate(deprNewestDate);
        astConfigService.updateById(astConfig);
        // 更新折旧单状态
        astDeprBill.setStatus("2");
        astDeprBill.setConfirmedId(SecurityUtils.getUserId());
        astDeprBill.setConfirmedId(SecurityUtils.getLoginUser().getUser().getNickName());
        astDeprBill.setConfirmedDate(DateUtil.date());
        this.updateById(astDeprBill);
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
        AstDeprBill byId = this.getById(id);
        if (!"2".equals(byId.getStatus())) {
            throw new CustomException("当前单据状态不可取消确认！");
        }
        byId.setStatus("0");
        byId.setConfirmedDate(null);
        Integer fiscal = byId.getFiscal();
        Integer deprMo = byId.getDeprMo();
        // 校验折旧日期
        String deptId = SecurityUtils.getLoginUser().getUser().getDeptId();
        AstConfig astConfig = astConfigService.selectByUnitId(deptId);
        Date deprNewestDate = astConfig.getDeprNewestDate();
        // 校验是否存在补计提折旧
        AstDeprRepairBill astDeprRepairBill = astDeprRepairBillService.getAstDeprRepairBill(fiscal, deprMo);
        if (astDeprRepairBill != null && StringUtils.isNotEmpty(astDeprRepairBill.getId()) && ProcessConstant.STATUS_2.equals(astDeprRepairBill.getStatus())) {
            throw new CustomException("所选折旧期间存在补计提折旧，请取消补计提折旧后再进行本次操作！");
        }
        // 最新折旧期间
        Integer deprNewestYear = DateUtil.year(deprNewestDate);
        Integer deprNewestMonth = DateUtil.month(deprNewestDate) + 1;
        DateTime dateTime = DateUtil.parse(fiscal + "-" + (deprMo < 10 ? "0" + deprMo : deprMo), "yyyy-MM");
        // 校验所选月份是否可折旧
        if (DateUtil.year(dateTime) != deprNewestYear || (DateUtil.month(dateTime) + 1) != deprNewestMonth) {
            throw new CustomException("最新可取消折旧期间为" + deprNewestYear + "年" + deprNewestMonth + "月,请确认后重试！");
        }
        // 还原资产卡片
        List<AstDeprList> astDeprLists = astDeprListService.getByDeptId(id);
        if (CollectionUtil.isNotEmpty(astDeprLists)) {
            Map<String, AstDeprList> astDeprListMap = astDeprLists.stream().collect(Collectors.toMap(AstDeprList::getAstId, Function.identity()));
            List<AstCard> astCards = astCardService.listByIds(astDeprListMap.keySet());
            for (AstCard astCard : astCards) {
                AstDeprList astDeprList = astDeprListMap.get(astCard.getId());
                astCard.setDepedMo(astDeprList.getCardDepedMo());
                astCard.setDepTotal(astDeprList.getCardDepTotal());
                astCard.setDepreciationMo(astDeprList.getCardDepreciationMo());
                astCard.setDepMoValue(astDeprList.getCardDepMoValue());
                astCard.setNetValue(astDeprList.getCardNetValue());
            }
            astCardService.updateBatchById(astCards);
        }
        // 更新最新折旧期间
        astConfig.setDeprNewestDate(DateUtil.offsetMonth(deprNewestDate, -1));
        astConfigService.updateById(astConfig);
        // 更新折旧单状态
        this.updateById(byId);
        // 删除折旧记录
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
        AstDeprBill astDeprBill = this.getById(id);
        if (astDeprBill == null) {
            throw new CustomException("单据不存在或已经被删除，请刷新后重试！");
        }
        String deptId = SecurityUtils.getLoginUser().getUser().getDeptId();
        AstConfig astConfig = astConfigService.selectByUnitId(deptId);
        Date deprNewestDate = astConfig.getDeprNewestDate();
        // 最新折旧期间
        Integer deprNewestYear = DateUtil.year(deprNewestDate);
        Integer deprNewestMonth = DateUtil.month(deprNewestDate) + 1;
        Integer fiscal = astDeprBill.getFiscal();
        Integer deprMo = astDeprBill.getDeprMo();
        DateTime dateTime = DateUtil.offsetMonth(DateUtil.parse(fiscal + "-" + deprMo, "yyyy-MM"), -1);
        if (DateUtil.year(dateTime) != deprNewestYear || (DateUtil.month(dateTime) + 1) != deprNewestMonth) {
            throw new CustomException("请按折旧顺序倒序删除折旧记录！");
        }
        QueryWrapper<AstDeprList> qw = new QueryWrapper<>();
        qw.lambda().eq(AstDeprList::getAstDeprId, id);
        astDeprListService.remove(qw);
        this.removeById(id);
        return "删除成功！";
    }

    /**
     * 查询卡片折旧记录
     *
     * @param id
     * @return
     */
    @Override
    public List<Map<String, Object>> record(String id) {
        return baseMapper.record(id);
    }

}