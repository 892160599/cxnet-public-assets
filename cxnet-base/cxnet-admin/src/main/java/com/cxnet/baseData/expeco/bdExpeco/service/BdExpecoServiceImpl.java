package com.cxnet.baseData.expeco.bdExpeco.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.cxnet.baseData.expeco.bdExpeco.domain.BdExpeco;
import com.cxnet.baseData.expeco.bdExpeco.domain.SelectionBdExpeco;
import com.cxnet.baseData.expeco.bdExpeco.mapper.BdExpecoMapper;
import com.cxnet.baseData.govexpeco.bdGovexpeco.domain.BdGovGovexpeco;
import com.cxnet.baseData.govexpeco.bdGovexpeco.mapper.BdGovGovexpecoMapper;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.CarryForwardUtils;
import com.cxnet.common.utils.DateUtils;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.common.utils.tree.Zone;
import com.cxnet.common.utils.tree.ZoneUtils;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.project.system.dept.domain.SysDept;
import com.cxnet.project.system.dept.mapper.SysDeptMapper;
import com.cxnet.project.system.dict.domain.SysDictData;
import com.cxnet.project.system.dict.mapper.SysDictDataMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 部门预算经济分类Service业务层处理
 *
 * @author cxnet
 * @date 2020-08-17
 */
@Service
public class BdExpecoServiceImpl implements BdExpecoServiceI {
    @Autowired(required = false)
    private BdExpecoMapper bdExpecoMapper;
    @Autowired(required = false)
    private BdGovGovexpecoMapper bdGovGovexpecoMapper;
    @Autowired(required = false)
    private SysDeptMapper sysDeptMapper;
    @Autowired(required = false)
    private SysDictDataMapper dictDataMapper;

    /**
     * 查询部门预算经济分类
     *
     * @param expecoId 部门预算经济分类ID
     * @return 部门预算经济分类
     */
    @Override
    public BdExpeco selectBdExpecoById(String expecoId) {
        SysDept dept = sysDeptMapper.selectDeptById(SecurityUtils.getLoginUser().getUser().getDeptId());
        BdExpeco bdExpeco = bdExpecoMapper.selectBdExpecoById(expecoId);
        List<BdGovGovexpeco> bdGovGovexpecos = bdGovGovexpecoMapper.selectBdGovGovexpecoByYearExpecoCode(bdExpeco.getYear(), bdExpeco.getExpecoCode(), dept.getUnitQuality());
        bdExpeco.setBdGovGovexpecos(bdGovGovexpecos);
        return bdExpeco;
    }

    /**
     * 查询部门预算经济分类集合
     *
     * @param bdExpeco 部门预算经济分类
     * @return 部门预算经济分类
     */
    @Override
    public List<BdExpeco> selectBdExpecoList(BdExpeco bdExpeco) {
        if (bdExpeco.getYear() == null) {
            throw new CustomException("缺少业务年度！");
        }
        bdExpeco.setDelFlag("0");
        return bdExpecoMapper.selectBdExpecoList(bdExpeco);
    }

    /**
     * 查询部门预算经济分类tree
     *
     * @param bdExpeco
     * @return
     */
    @Override
    public List<Zone> selectBdExpecoListTree(BdExpeco bdExpeco) {
        if (bdExpeco.getYear() == null) {
            throw new CustomException("缺少业务年度！");
        }
        if (StringUtils.isEmpty(bdExpeco.getUnitId())) {
            bdExpeco.setUnitId("*");
        }
        bdExpeco.setDelFlag("0");
        List<Zone> zoneList = bdExpecoMapper.selectAssetTypeListTree(bdExpeco);
        return ZoneUtils.buildTreeBypcode(zoneList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Zone> insertBatchSelectionBdExpeco(SelectionBdExpeco selectionBdExpeco) {
        String[] expecoCodes = selectionBdExpeco.getExpecoCodes();
        Long year = selectionBdExpeco.getYear();
        String unitId = selectionBdExpeco.getUnitId();
        if (/*ArrayUtil.isEmpty(expecoCodes) || */ObjectUtil.isNull(year) || StringUtils.isEmpty(unitId)) {
            throw new CustomException("参数异常！");
        }
        List<BdExpeco> bdExpecoInList = new ArrayList<>();
        List<BdExpeco> bdExpecos = new ArrayList<>();
        if (ArrayUtil.isNotEmpty(expecoCodes)) {
            bdExpecos = bdExpecoMapper.selectBdExpecoByIds(expecoCodes);
        }
        BdExpeco bdExpfunc = new BdExpeco(year, unitId);
        // 已绑定list
        List<BdExpeco> bdExpecoList = bdExpecoMapper.selectBdExpecoList(bdExpfunc);
        List<String> funcCodes = bdExpecoList.stream().map(BdExpeco::getExpecoCode).collect(Collectors.toList());
        bdExpecos.forEach(v -> {
            String expecoCode = v.getExpecoCode();
            ///   if(!funcCodes.contains(expecoCode)){
            // 遍历出所有上级
            List<BdExpeco> bdExpfuncUpList = bdExpecoMapper.selectBdExpfuncUpList(year, expecoCode);
            if (CollectionUtils.isNotEmpty(bdExpfuncUpList)) {
                bdExpfuncUpList.forEach(bdExpfuncUp -> {
                    ///if(!funcCodes.contains(bdExpfuncUp.getExpecoCode())){
                    BdExpeco newBdExpeco = new BdExpeco();
                    BeanUtil.copyProperties(bdExpfuncUp, newBdExpeco);
                    newBdExpeco.setUnitId(unitId);
                    newBdExpeco.setExpecoId(null);
                    newBdExpeco.setParentId(null);
                    bdExpecoInList.add(newBdExpeco);
                    /// }
                });
            }
            ///  }
        });
        List<BdExpeco> collect = bdExpecoInList.stream().distinct().collect(Collectors.toList());
        bdExpecoMapper.deleteBdExpecoByYear(unitId, year);
        if (CollectionUtils.isNotEmpty(collect)) {
            bdExpecoMapper.insertBatchBdExpeco(collect);
        }
        return selectBdExpecoListTree(bdExpfunc);
    }

    /**
     * 部门经济分类结转年度计算
     *
     * @return 结果
     */
    @Override
    public Map<String, Integer> carryForwardYear() {
        // 获取当前数据最新年度
        int year = bdExpecoMapper.selectLatestYear();
        // 判断是否可以结转或重新结转
        return CarryForwardUtils.carryForward(year);
    }

    /**
     * 部门经济分类结转
     *
     * @param year 结转年度
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void carryForwardData(String year) {
        if (StringUtils.isEmpty(year) || "0".equals(year)) {
            return;
        }
        // 删除结转目标年度数据
        bdExpecoMapper.deleteBdExpecoByYear(null, Long.valueOf(year));
        // 重新结转数据 (目标年度，被结转年度)
        bdExpecoMapper.carryForwardData(Integer.valueOf(year), Integer.valueOf(year) - 1);
        // 新增年度字典
        SysDictData dictData = new SysDictData();
        dictData.setDictValue(year);
        dictData.setDictType("bd_pur_year");
        String bdPurYear = dictDataMapper.selectDictLabel(dictData);
        if (StringUtils.isEmpty(bdPurYear)) {
            dictData.setDictLabel(year);
            dictData.setDictSort(0L);
            dictData.setParentId("0");
            dictData.setStatus("0");
            dictDataMapper.insertDictData(dictData);
        }
    }

    /**
     * 新增部门预算经济分类
     *
     * @param bdExpeco 部门预算经济分类
     * @return 结果
     */
    @Override
    public int insertBdExpeco(BdExpeco bdExpeco) {
        init(bdExpeco);
        if (checkBdExpeco(bdExpeco)) {
            throw new CustomException("编码重复！");
        }
        return bdExpecoMapper.insertBdExpeco(bdExpeco);
    }

    /**
     * 校验编码是否存在
     *
     * @param bdExpeco
     * @return
     */
    private boolean checkBdExpeco(BdExpeco bdExpeco) {
        return bdExpecoMapper.checkCode(bdExpeco) > 0;
    }

    /**
     * 新增初始数据
     *
     * @param bdExpeco
     */
    private void init(BdExpeco bdExpeco) {
        if (StringUtils.isEmpty(bdExpeco.getUnitId())) {
            bdExpeco.setUnitId("*");
        }
        bdExpeco.setDelFlag("0");
        bdExpeco.setStatus("0");
        bdExpeco.setCreateBy(SecurityUtils.getUsername());
        bdExpeco.setCreateTime(DateUtils.getNowDate());
    }

    /**
     * 批量新增部门预算经济分类
     *
     * @param bdExpecos 部门预算经济分类
     * @return 结果
     */
    @Override
    public int insertBatchBdExpeco(List<BdExpeco> bdExpecos, Long year) {
        bdExpecos.forEach(v -> {
            init(v);
            v.setYear(year);
        });
        checkBdExpecos(bdExpecos, year);
        return bdExpecoMapper.insertBatchBdExpeco(bdExpecos);
    }

    private void checkBdExpecos(List<BdExpeco> bdExpecos, Long year) {
        List<String> codes = bdExpecos.stream().map(BdExpeco::getExpecoCode).collect(Collectors.toList());
        List<BdExpeco> oldList = bdExpecoMapper.selectBdExpecoByCodes(codes, year, "*");
        List<String> oldCodes = oldList.stream().map(BdExpeco::getExpecoCode).collect(Collectors.toList());
        StringBuilder sb = new StringBuilder("导入失败，第");
        boolean flag = false;
        for (int i = 0; i < bdExpecos.size(); i++) {
            String expecoCode = bdExpecos.get(i).getExpecoCode();
            String expecoName = bdExpecos.get(i).getExpecoName();
            if (oldCodes.contains(expecoCode)) {
                flag = true;
                sb.append(i + 4).append("行[").append(expecoCode).append("]").append(expecoName).append("、");
            }
        }
        if (flag) {
            sb.deleteCharAt(sb.length() - 1);
            sb.append("编码重复！");
            throw new CustomException(sb.toString());
        }
    }

    /**
     * 修改部门预算经济分类
     *
     * @param bdExpeco 部门预算经济分类
     * @return 结果
     */
    @Override
    public int updateBdExpeco(BdExpeco bdExpeco) {
        if (checkBdExpeco(bdExpeco)) {
            throw new CustomException("编码重复！");
        }
        bdExpeco.setUpdateBy(SecurityUtils.getUsername());
        bdExpeco.setUpdateTime(DateUtils.getNowDate());
        return bdExpecoMapper.updateBdExpeco(bdExpeco);
    }

    /**
     * 批量修改部门预算经济分类
     *
     * @param bdExpecos 部门预算经济分类
     * @return 结果
     */
    @Override
    public int updateBatchBdExpeco(List<BdExpeco> bdExpecos) {
        return bdExpecoMapper.updateBatchBdExpeco(bdExpecos);
    }

    /**
     * 批量删除部门预算经济分类
     *
     * @param expecoIds 需要删除的部门预算经济分类ID
     * @return 结果
     */
    @Override
    public int deleteBdExpecoByIds(String[] expecoIds) {
        return bdExpecoMapper.deleteBdExpecoByIds(expecoIds);
    }

    /**
     * 删除部门预算经济分类信息
     *
     * @param expecoId 部门预算经济分类ID
     * @return 结果
     */
    @Override
    public int deleteBdExpecoById(String expecoId) {
        BdExpeco bdExpeco = bdExpecoMapper.selectBdExpecoById(expecoId);
        if (bdExpeco == null) {
            throw new CustomException("未查询到要删除的部门预算经济分类信息！");
        }
        Integer i = bdExpecoMapper.existChildren(bdExpeco.getYear(), bdExpeco.getUnitId(), bdExpeco.getExpecoCode());
        if (i != 0) {
            throw new CustomException("存在下级预算经济分类，无法删除！");
        }
        return bdExpecoMapper.deleteBdExpecoById(expecoId);
    }


}
