package com.cxnet.asset.card.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.asset.annex.domain.AstAnnex;
import com.cxnet.asset.annex.service.AstAnnexService;
import com.cxnet.asset.astcardacsequip.domain.AstCardAcsequip;
import com.cxnet.asset.astcardacsequip.service.AstCardAcsequipService;
import com.cxnet.asset.astfield.domain.AstField;
import com.cxnet.asset.astfield.service.AstFieldService;
import com.cxnet.asset.businessSet.service.AstDeptUserService;
import com.cxnet.asset.card.domain.AstCard;
import com.cxnet.asset.card.domain.dto.AstCardDTO;
import com.cxnet.asset.card.domain.vo.AstCardVO;
import com.cxnet.asset.card.mapper.AstCardMapper;
import com.cxnet.common.constant.BillTypeCodeConstant;
import com.cxnet.common.enums.AnnexType;
import com.cxnet.common.utils.DateUtils;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.common.utils.sql.SqlUtil;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.framework.web.page.PageDomain;
import com.cxnet.framework.web.page.TableSupport;
import com.cxnet.project.system.dept.domain.SysDept;
import com.cxnet.project.system.dept.domain.SysDeptVO;
import com.cxnet.project.system.dept.service.SysDeptServiceI;
import com.cxnet.project.system.rule.service.RuleServiceI;
import com.github.pagehelper.PageHelper;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 资产主表(AstCard)表服务实现类
 *
 * @author guks
 * @since 2021-03-29 10:23:01
 */
@Service
@Slf4j
public class AstCardServiceImpl extends ServiceImpl<AstCardMapper, AstCard> implements AstCardService {

    /**
     * 注入编号器服务
     */
    @Autowired(required = false)
    private RuleServiceI ruleServiceI;

    @Autowired(required = false)
    private AstCardAcsequipService astCardAcsequipService;

    @Autowired(required = false)
    private AstAnnexService astAnnexService;

    @Autowired(required = false)
    private AstDeptUserService astDeptUserService;

    @Autowired(required = false)
    private SysDeptServiceI sysDeptServiceI;

    @Autowired(required = false)
    private AstFieldService astFieldService;

    /**
     * 保存资产对象
     *
     * @param astCardVO 资产对象
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveAstCardVo(AstCardVO astCardVO) {

        AstCard astCard = astCardVO.getAstCard();
        List<AstCardAcsequip> astCardAcsequips = astCardVO.getAstCardAcsequips();
        List<AstAnnex> astAnnexes = astCardVO.getAstAnnexes();

        astCard.setId(null);
        //设置卡片状态
        astCard.setAstStatus("0");
        //设置初始化状态
        astCard.setInitialState("1");
        astCard.setDelFlag("0");
        astCard.setIsInitImport("2");
        astCard.setCreateTime(DateUtil.date());
        // 设置单据号
        astCard.setAstCode(ruleServiceI.nextNumberByModelCode(BillTypeCodeConstant.AST_REGISTER, astCard));

        // 保存资产主表
        this.save(astCard);
        String id = astCard.getId();
        // 保存附属设备
        saveAstCardAcsequip(astCardAcsequips, id);
        // 保存资产附件
        saveAstAnnexes(astAnnexes, id);
        return id;
    }


    /**
     * 更新资产信息
     *
     * @param astCardVO
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String updateAstCardVo(AstCardVO astCardVO) {
        AstCard astCard = astCardVO.getAstCard();
        List<AstCardAcsequip> astCardAcsequips = astCardVO.getAstCardAcsequips();
        List<AstAnnex> astAnnexes = astCardVO.getAstAnnexes();
        String id = astCard.getId();
        astCard.setDelFlag("0");
        astCard.setUpdateBy(SecurityUtils.getUsername());
        astCard.setUpdateTime(DateUtils.getNowDate());
        // 保存资产主表
        this.saveOrUpdate(astCard);

        Map<String, Object> columnMap = new HashedMap<>();
        columnMap.put("AST_ID", id);
        // 删除附属设备
        astCardAcsequipService.removeByMap(columnMap);
        // 保存附属设备
        saveAstCardAcsequip(astCardAcsequips, id);

        // 删除资产附件
        astAnnexService.removeByMap(columnMap);
        // 保存资产附件
        saveAstAnnexes(astAnnexes, id);
        return id;
    }


    /**
     * 根据id查询资产信息
     *
     * @param id 主键id
     * @return
     */
    @Override
    public AstCardVO getAstCardVO(String id) {

        AstCardVO astCardVO = new AstCardVO();

        Map<String, Object> columnMap = new HashedMap<>();
        columnMap.put("AST_ID", id);
        //查询主表信息
        AstCard astCard = this.getById(id);
        //查询附属设备信息
        List<AstCardAcsequip> astCardAcsequips = astCardAcsequipService.listByMap(columnMap);

        //查询资产附件
        List<AstAnnex> astAnnexes = astAnnexService.listByAstIdAndAnnexType(id, "0");

        astCardVO.setAstCard(astCard);
        astCardVO.setAstCardAcsequips(astCardAcsequips);
        astCardVO.setAstAnnexes(astAnnexes);
        return astCardVO;
    }

    /**
     * 保存资产附件
     *
     * @param astAnnexes 附件列表对象
     * @param id         资产主表id
     */
    private void saveAstAnnexes(List<AstAnnex> astAnnexes, String id) {
        if (CollectionUtil.isNotEmpty(astAnnexes)) {
            astAnnexes.forEach(v -> {
                v.setAstId(id);
                v.setAnnexType(AnnexType.REGISTER.getValue());
            });
            astAnnexService.saveOrUpdateBatch(astAnnexes);
        }
    }

    /**
     * 保存附属设备
     *
     * @param astCardAcsequips 附属设备对象
     * @param id               资产主表id
     */
    private void saveAstCardAcsequip(List<AstCardAcsequip> astCardAcsequips, String id) {
        if (CollectionUtil.isNotEmpty(astCardAcsequips)) {
            astCardAcsequips.forEach(v -> {
                v.setAstId(id);
            });
            astCardAcsequipService.saveOrUpdateBatch(astCardAcsequips);
        }
    }


    /**
     * 获取部门
     */
    @Override
    public List<SysDept> getDepartmentList() {
        List<SysDept> sysDeptList = new ArrayList<>();
        Map<String, List<String>> resultMap = astDeptUserService.getMap();
        //获取单位id
        List<String> unitIds = resultMap.get("unit");
        //获取部门id
        List<String> deptIdList = resultMap.get("dept");

        //如果当前用户是单位管理员的话，那么获取当前单位下的部门列表
        if (CollectionUtil.isNotEmpty(unitIds) && CollectionUtil.isEmpty(deptIdList)) {
            List<SysDeptVO> sysDeptVOList = sysDeptServiceI.getThisDept(unitIds.get(0));
            if (CollectionUtil.isNotEmpty(sysDeptVOList)) {
                SysDept sysDept = null;
                for (SysDeptVO sysDeptVO : sysDeptVOList) {
                    sysDept = new SysDept();
                    BeanUtil.copyProperties(sysDeptVO, sysDept);
                    sysDeptList.add(sysDept);
                }
            }
        }

        //如果当前用户是部门管理员的话，那么获取他所管理的部门
        if (CollectionUtil.isEmpty(unitIds) && CollectionUtil.isNotEmpty(deptIdList) && CollectionUtil.isEmpty(sysDeptList)) {
            String[] deptIds = deptIdList.toArray(new String[deptIdList.size()]);
            sysDeptList = sysDeptServiceI.selectDeptListByIds(deptIds);
        }

        return sysDeptList;
    }


    /**
     * 获取自定义头部
     *
     * @param unitId   单位id
     * @param isImport 是否导出
     * @return
     */
    private List<List<String>> head(String unitId, boolean isImport) {
        List<List<String>> headlist = new ArrayList<List<String>>();
        List<String> head = null;

        QueryWrapper<AstField> qw = new QueryWrapper<>();
        qw.lambda().eq(AstField::getIsEnable, "0").eq(AstField::getUnitId, unitId);
        //判断是否是导入列
        if (isImport) {
            qw.lambda().eq(AstField::getIsImport, "0");
        }
        qw.orderByAsc("orders");
        //获取导出字段列表
        List<AstField> astFieldList = astFieldService.list(qw);
        if (CollectionUtil.isNotEmpty(astFieldList)) {
            for (AstField astField : astFieldList) {
                if (null != astField && StringUtils.isNotBlank(astField.getImportTitle().trim())) {
                    head = new ArrayList();
                    head.add("资产卡片导入模板");
                    //设置表头数组
                    head.add(astField.getImportTitle().trim());
                    headlist.add(head);
                }
            }
        }

        return headlist;
    }

    @Override
    public List<AstCard> getAstCardList(AstCardDTO astCardDTO) {

        QueryWrapper<AstCard> qw = new QueryWrapper<>();

        //单位查询
        if (StringUtils.isNotBlank(astCardDTO.getUnitId())) {
            qw.eq("UNIT_ID", astCardDTO.getUnitId());
        }

        // 卡片状态
        if (StringUtils.isNotBlank(astCardDTO.getAstStatus())) {
            qw.eq("AST_STATUS", astCardDTO.getAstStatus());
        }

        // 部门id
        if (StringUtils.isNotBlank(astCardDTO.getDeptId())) {
            qw.eq("DEPT_ID", astCardDTO.getDeptId());
        }

        // 使用人id
        if (StringUtils.isNotBlank(astCardDTO.getEmpId())) {
            qw.eq("EMP_ID", astCardDTO.getEmpId());
        }

        // 购置时间
        if (null != astCardDTO.getAcquisitionStartDate() && null != astCardDTO.getAcquisitionEndDate()) {
            qw.ge("ACQUISITION_DATE", astCardDTO.getAcquisitionStartDate());
            qw.le("ACQUISITION_DATE", astCardDTO.getAcquisitionEndDate());
        }

        //资产名称
        if (StringUtils.isNotBlank(astCardDTO.getAssetName())) {
            qw.like("ASSET_NAME", astCardDTO.getAssetName());
        }

        //制单时间
        if (null != astCardDTO.getCreatStartTime() && null != astCardDTO.getCreatEndTime()) {
            qw.ge("CREATE_TIME", astCardDTO.getCreatStartTime());
            qw.le("CREATE_TIME", astCardDTO.getCreatEndTime());
        }

        //使用部门id
        if (StringUtils.isNotBlank(astCardDTO.getDepartmentId())) {
            qw.eq("DEPARTMENT_ID", astCardDTO.getDepartmentId());
        } else {
            //获取部门列表
            List<SysDept> sysDeptList = this.getDepartmentList();
            List<String> deptIds = sysDeptList.stream().map(SysDept::getDeptId).collect(Collectors.toList());

            Map<String, List<String>> resultMap = astDeptUserService.getMap();
            //获取单位id
            List<String> unitIds = resultMap.get("unit");
            //获取部门id
            List<String> deptIdList = resultMap.get("dept");

            //如果当前用户是单位管理员的话
            if (CollectionUtil.isNotEmpty(unitIds) && CollectionUtil.isEmpty(deptIdList)) {
                qw.and(wrapper -> wrapper.in("DEPARTMENT_ID", deptIds).or().isNull("DEPARTMENT_ID"));
            }

            //如果当前用户是部门管理员的话
            if (CollectionUtil.isEmpty(unitIds) && CollectionUtil.isNotEmpty(deptIdList)) {
                qw.and(wrapper -> wrapper.in("DEPARTMENT_ID", deptIds));
            }
        }

        //管理部门id
        if (StringUtils.isNotBlank(astCardDTO.getAdminDepartmentId())) {
            qw.eq("ADMIN_DEPARTMENT_ID", astCardDTO.getAdminDepartmentId());
        }

        //存放地点id
        if (StringUtils.isNotBlank(astCardDTO.getPlaceId())) {
            qw.eq("PLACE_ID", astCardDTO.getPlaceId());
        }

        //价值开始
        if (StringUtils.isNotBlank(astCardDTO.getCosStart())) {
            qw.ge("COST", new BigDecimal(astCardDTO.getCosStart()));
        }

        //价值结束
        if (StringUtils.isNotBlank(astCardDTO.getCosEnd())) {
            qw.le("COST", new BigDecimal(astCardDTO.getCosEnd()));
        }

        qw.eq("DEL_FLAG", "0");
        //按照制单时间逆序排
        qw.orderByDesc("CREATE_TIME");

        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
        return this.list(qw);
    }


}
