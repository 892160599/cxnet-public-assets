package com.cxnet.asset.depr.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.asset.businessSet.domain.AstDeprMethod;
import com.cxnet.asset.businessSet.service.AstDeprMethodService;
import com.cxnet.asset.depr.domain.AstDeprBill;
import com.cxnet.asset.depr.domain.vo.AstDeprListVo;
import com.cxnet.asset.depr.mapper.AstDeprListMapper;
import com.cxnet.asset.depr.domain.AstDeprList;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.project.system.dict.domain.SysDictData;
import com.cxnet.project.system.dict.service.SysDictDataServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 资产折旧明细表(AstDeprList)表服务实现类
 *
 * @author caixx
 * @since 2021-04-08 16:03:13
 */
@Service
public class AstDeprListServiceImpl extends ServiceImpl<AstDeprListMapper, AstDeprList> implements AstDeprListService {

    @Autowired(required = false)
    private SysDictDataServiceI sysDictDataServiceI;

    @Resource
    private AstDeprMethodService astDeprMethodService;

    /**
     * 无管理部门，部门名称
     */
    private static final String DEFAULT_DEPT_NAME = "无管理部门";

    /**
     * 无资产大类，大类名称
     */
    private static final String DEFAULT_CLASS_CODE = "其他";

    /**
     * 查询汇总明细
     *
     * @param id
     * @return
     */
    @Override
    public AstDeprListVo astSummaryList(String id) {
        AstDeprListVo astDeprListVo = new AstDeprListVo();
        List<AstDeprList> classCodeList = new ArrayList<>();
        List<AstDeprList> deptList = new ArrayList<>();
        QueryWrapper<AstDeprList> qw = new QueryWrapper<>();
        qw.lambda().eq(AstDeprList::getAstDeprId, id)
                .orderByAsc(AstDeprList::getAstCode);
        List<AstDeprList> list = this.list(qw);
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(v -> {
                if (StringUtils.isEmpty(v.getDepartmentId())) {
                    v.setDepartmentId(DEFAULT_DEPT_NAME);
                    v.setDepartmentCode(DEFAULT_DEPT_NAME);
                    v.setDepartmentName(DEFAULT_DEPT_NAME);
                }
                if (StringUtils.isEmpty(v.getClassCode())) {
                    v.setClassCode(DEFAULT_CLASS_CODE);
                    v.setClassName(DEFAULT_CLASS_CODE);
                }
            });
            // 资产大类汇总
            Map<String, List<AstDeprList>> astDeprListGroup = list.stream().collect(Collectors.groupingBy(AstDeprList::getClassCode));
            List<SysDictData> dictData = sysDictDataServiceI.selectDictDataByType("ast_type");
            dictData.forEach(v -> {
                AstDeprList astDeprList = new AstDeprList();
                astDeprList.setClassCode(v.getDictValue());
                astDeprList.setClassName(v.getDictLabel());
                astDeprList.setId(UUID.randomUUID().toString());
                astDeprList.setParentCode("0");
                // 折旧信息汇总
                List<AstDeprList> astDeprLists = astDeprListGroup.get(v.getDictValue());
                astDeprListSum(astDeprList, astDeprLists);
                classCodeList.add(astDeprList);
            });
            // 根据部门大类汇总
            Map<String, List<AstDeprList>> collect = list.stream().collect(Collectors.groupingBy(AstDeprList::getDepartmentId));
            List<String> depts = collect.keySet().stream().collect(Collectors.toList());
            depts.forEach(d -> {
                AstDeprList astDeprList = new AstDeprList();
                astDeprList.setDepartmentId(d);
                astDeprList.setId(UUID.randomUUID().toString());
                List<AstDeprList> astDeprLists = collect.get(d);
                if (CollectionUtil.isNotEmpty(astDeprLists)) {
                    Map<String, List<AstDeprList>> astDeprListGroupChildren = astDeprLists.stream().collect(Collectors.groupingBy(AstDeprList::getClassCode));
                    List<AstDeprList> children = new ArrayList<>();
                    dictData.forEach(dd -> {
                        AstDeprList astDeprs = new AstDeprList();
                        astDeprs.setClassCode(dd.getDictValue());
                        astDeprs.setClassName(dd.getDictLabel());
                        astDeprs.setParentCode("0");
                        astDeprs.setId(UUID.randomUUID().toString());
                        List<AstDeprList> astDeprListList = astDeprListGroupChildren.get(dd.getDictValue());
                        astDeprListSum(astDeprs, astDeprListList);
                        if (CollectionUtil.isNotEmpty(astDeprListList)) {
                            astDeprList.setDepartmentName(astDeprListList.get(0).getDepartmentName());
                        }
                        children.add(astDeprs);
                    });
                    astDeprList.setChildren(children);
                    astDeprListSum(astDeprList, children);
                    deptList.add(astDeprList);
                }
            });
        }
        astDeprListVo.setClassCodeList(classCodeList);
        astDeprListVo.setDeptList(deptList);
        return astDeprListVo;
    }

    /**
     * 汇总求和
     *
     * @param astDeprList
     * @param astDeprLists
     */
    private void astDeprListSum(AstDeprList astDeprList, List<AstDeprList> astDeprLists) {
        if (CollectionUtil.isEmpty(astDeprLists) || astDeprList == null) {
            return;
        }
        for (AstDeprList deprList : astDeprLists) {
            astDeprList.setCost(astDeprList.getCost().add(deprList.getCost()));
            astDeprList.setDepTotal(astDeprList.getDepTotal().add(deprList.getDepTotal()));
            astDeprList.setNetValue(astDeprList.getNetValue().add(deprList.getNetValue()));
            astDeprList.setQty(astDeprList.getQty() + deprList.getQty());
            astDeprList.setThisValue(astDeprList.getThisValue().add(deprList.getThisValue()));
        }
    }

    /**
     * 根据主表id查询
     *
     * @param id
     * @return
     */
    @Override
    public List<AstDeprList> getByDeptId(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new CustomException("缺少参数！");
        }
        QueryWrapper<AstDeprList> qw = new QueryWrapper<>();
        qw.lambda().eq(AstDeprList::getAstDeprId, id);
        return this.list(qw);
    }

    /**
     * 分页查询折旧明细
     *
     * @param astDeprBill
     * @return
     */
    @Override
    public List<AstDeprList> selectAll(AstDeprBill astDeprBill) {
        List<AstDeprList> list = new ArrayList<>();
        String id = astDeprBill.getId();
        List<AstDeprMethod> astDeprMethods = astDeprMethodService.list();
        Map<String, String> deprMethodMap = astDeprMethods.stream().filter(v -> "0".equals(v.getDelFlag())).collect(Collectors.toMap(AstDeprMethod::getDeprMethodCode, AstDeprMethod::getDeprMethodName));
        if (StringUtils.isNotEmpty(id)) {
            QueryWrapper<AstDeprList> qw = new QueryWrapper<>();
            qw.lambda().eq(AstDeprList::getAstDeprId, id)
                    .orderByAsc(AstDeprList::getAstCode);
            list = this.list(qw);
            list.forEach(v -> v.setDepMethod(deprMethodMap.get(v.getDepMethod())));
        }
        return list;
    }

}