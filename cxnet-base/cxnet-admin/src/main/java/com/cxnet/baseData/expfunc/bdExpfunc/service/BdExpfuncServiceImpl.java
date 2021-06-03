package com.cxnet.baseData.expfunc.bdExpfunc.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.cxnet.baseData.expfunc.bdExpfunc.domain.BdExpfunc;
import com.cxnet.baseData.expfunc.bdExpfunc.domain.SelectionBdExpfunc;
import com.cxnet.baseData.expfunc.bdExpfunc.mapper.BdExpfuncMapper;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.CarryForwardUtils;
import com.cxnet.common.utils.DateUtils;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.common.utils.tree.Zone;
import com.cxnet.common.utils.tree.ZoneUtils;
import com.cxnet.framework.redis.RedisCache;
import com.cxnet.framework.redis.RedisUtil;
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
 * 支出功能分类Service业务层处理
 *
 * @author cxnet
 * @date 2020-08-17
 */
@Service
public class BdExpfuncServiceImpl implements BdExpfuncServiceI {
    @Autowired(required = false)
    private BdExpfuncMapper bdExpfuncMapper;
    @Autowired(required = false)
    private SysDeptMapper sysDeptMapper;
    @Autowired(required = false)
    private SysDictDataMapper dictDataMapper;
    @Autowired(required = false)
    private RedisCache redisCache;
    @Autowired(required = false)
    private RedisUtil redisUtil;

    private final String BDEXPFUNC_ = "BdExpfunc_";

    /**
     * 查询支出功能分类
     *
     * @param funcId 支出功能分类ID
     * @return 支出功能分类
     */
    @Override
    public BdExpfunc selectBdExpfuncById(String funcId) {
        return bdExpfuncMapper.selectBdExpfuncById(funcId);
    }

    /**
     * 查询支出功能分类集合
     *
     * @param bdExpfunc 支出功能分类
     * @return 支出功能分类
     */
    @Override
    public List<BdExpfunc> selectBdExpfuncList(BdExpfunc bdExpfunc) {
        if (bdExpfunc.getYear() == null) {
            throw new CustomException("缺少业务年度！");
        }
        bdExpfunc.setDelFlag("0");
        return bdExpfuncMapper.selectBdExpfuncList(bdExpfunc);
    }

    /**
     * 查询支出功能分类tree
     *
     * @param bdExpfunc 支出功能分类
     * @return
     */
    @Override
    public List<Zone> selectBdExpfuncTree(BdExpfunc bdExpfunc) {
        if (bdExpfunc.getYear() == null) {
            throw new CustomException("缺少业务年度！");
        }
        if (StringUtils.isEmpty(bdExpfunc.getUnitId())) {
            bdExpfunc.setUnitId("*");
            bdExpfunc.setUnitCode("*");
        }
        String k = BDEXPFUNC_ + bdExpfunc.getYear() + "_" + bdExpfunc.getUnitId();
        List<Zone> cacheList = (List<Zone>) redisUtil.lGet(k, 0, -1);
        if (CollectionUtils.isNotEmpty(cacheList) && CollectionUtils.isNotEmpty((List<Zone>) cacheList.get(0))) {
            return (List<Zone>) cacheList.get(0);
        }

        bdExpfunc.setDelFlag("0");
        List<Zone> zoneList = bdExpfuncMapper.selectBdExpfuncTree(bdExpfunc);
        List<Zone> zones = ZoneUtils.buildTreeBypcode(zoneList);
        redisUtil.lSet(k, zones, 60 * 60 * 24 * 30);
        return zones;
    }

    /**
     * 新增支出功能分类
     *
     * @param bdExpfunc 支出功能分类
     * @return 结果
     */
    @Override
    public int insertBdExpfunc(BdExpfunc bdExpfunc) {
        init(bdExpfunc);
        if (checkBdExpfunc(bdExpfunc)) {
            throw new CustomException("已存在功能分类编码");
        }
        if (StringUtils.isEmpty(bdExpfunc.getParentId())) {
            bdExpfunc.setParentId("0");
        }
        if (StringUtils.isEmpty(bdExpfunc.getParentCode())) {
            bdExpfunc.setParentCode("0");
        }
        int result = bdExpfuncMapper.insertBdExpfunc(bdExpfunc);

        String k = BDEXPFUNC_ + bdExpfunc.getYear() + "_" + bdExpfunc.getUnitId();
        List<Zone> cacheList = redisUtil.lGet(k, 0, -1);
        if (CollectionUtils.isNotEmpty(cacheList) && CollectionUtils.isNotEmpty((List<Zone>) cacheList.get(0))) {
            redisUtil.del(k);
        }
        return result;
    }

    /**
     * 校验编码是否存在
     *
     * @param bdExpfunc
     * @return
     */
    private boolean checkBdExpfunc(BdExpfunc bdExpfunc) {
        if (StringUtils.isEmpty(bdExpfunc.getUnitCode())) {
            bdExpfunc.setUnitCode("*");
        }
        return bdExpfuncMapper.checkCode(bdExpfunc) > 0;
    }

    /**
     * 初始数据
     *
     * @param bdExpfunc
     */
    private void init(BdExpfunc bdExpfunc) {
        if (StringUtils.isEmpty(bdExpfunc.getUnitId())) {
            bdExpfunc.setUnitId("*");
            bdExpfunc.setUnitCode("*");
            bdExpfunc.setUnitName("*");
        }
        bdExpfunc.setCreateBy(SecurityUtils.getUsername());
        bdExpfunc.setCreateTime(DateUtils.getNowDate());
        bdExpfunc.setDelFlag("0");
    }

    /**
     * 批量新增支出功能分类
     *
     * @param bdExpfuncs 支出功能分类
     * @return 结果
     */
    @Override
    public int insertBatchBdExpfunc(List<BdExpfunc> bdExpfuncs) {
        return bdExpfuncMapper.insertBatchBdExpfunc(bdExpfuncs);
    }

    /**
     * 修改支出功能分类
     *
     * @param bdExpfunc 支出功能分类
     * @return 结果
     */
    @Override
    public int updateBdExpfunc(BdExpfunc bdExpfunc) {
        checkBdExpfunc(bdExpfunc);
        bdExpfunc.setUpdateBy(SecurityUtils.getUsername());
        bdExpfunc.setUpdateTime(DateUtils.getNowDate());
        int result = bdExpfuncMapper.updateBdExpfunc(bdExpfunc);

        String k = BDEXPFUNC_ + bdExpfunc.getYear() + "_" + bdExpfunc.getUnitId();
        List<Zone> cacheList = redisUtil.lGet(k, 0, -1);
        if (CollectionUtils.isNotEmpty(cacheList) && CollectionUtils.isNotEmpty((List<Zone>) cacheList.get(0))) {
            redisUtil.del(k);
        }
        return result;
    }

    /**
     * 批量修改支出功能分类
     *
     * @param bdExpfuncs 支出功能分类
     * @return 结果
     */
    @Override
    public int updateBatchBdExpfunc(List<BdExpfunc> bdExpfuncs) {
        return bdExpfuncMapper.updateBatchBdExpfunc(bdExpfuncs);
    }

    /**
     * 批量删除支出功能分类
     *
     * @param funcIds 需要删除的支出功能分类ID
     * @return 结果
     */
    @Override
    public int deleteBdExpfuncByIds(String[] funcIds) {
        return bdExpfuncMapper.deleteExpfuncByIds(funcIds);
    }

    /**
     * 删除支出功能分类信息
     *
     * @param funcId 支出功能分类ID
     * @return 结果
     */
    @Override
    public int deleteBdExpfuncById(String funcId) {
        BdExpfunc bdExpfunc = bdExpfuncMapper.selectBdExpfuncById(funcId);
        if (bdExpfunc == null) {
            throw new CustomException("未查询到要删除的支出功能分类信息！");
        }
        Integer i = bdExpfuncMapper.existChildren(bdExpfunc.getYear(), bdExpfunc.getUnitId(), bdExpfunc.getFuncCode());
        if (i != 0) {
            throw new CustomException("存在下级支出功能分类信息，无法删除！");
        }
        int result = bdExpfuncMapper.deleteBdExpfuncById(funcId);

        String k = BDEXPFUNC_ + bdExpfunc.getYear() + "_" + bdExpfunc.getUnitId();
        List<Zone> cacheList = redisUtil.lGet(k, 0, -1);
        if (CollectionUtils.isNotEmpty(cacheList) && CollectionUtils.isNotEmpty((List<Zone>) cacheList.get(0))) {
            redisUtil.del(k);
        }

        return result;
    }

    /**
     * 单位选用功能分类
     *
     * @param selectionBdExpfunc
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Zone> insertBatchSelectionBdExpfunc(SelectionBdExpfunc selectionBdExpfunc) {
        String[] code = selectionBdExpfunc.getFuncCode();
        Long year = selectionBdExpfunc.getYear();
        String unitId = selectionBdExpfunc.getUnitId();
        if (/*ArrayUtil.isEmpty(code) ||*/ ObjectUtil.isNull(year) || StringUtils.isEmpty(unitId)) {
            throw new CustomException("参数异常！");
        }
        SysDept sysDept = sysDeptMapper.selectDeptById(unitId);
        List<BdExpfunc> bdExpfuncInList = new ArrayList<>();
        List<BdExpfunc> bdExpfuncs = new ArrayList<>();
        if (ArrayUtil.isNotEmpty(code)) {
            bdExpfuncs = bdExpfuncMapper.selectBdExpfuncByIds(code);
        }
        BdExpfunc bdExpfunc = new BdExpfunc(year, unitId);
        // 已绑定list
        List<BdExpfunc> bdExpfuncList = bdExpfuncMapper.selectBdExpfuncList(bdExpfunc);
        List<String> funcCodes = bdExpfuncList.stream().map(BdExpfunc::getFuncCode).collect(Collectors.toList());
        bdExpfuncs.forEach(v -> {
            String funcCode = v.getFuncCode();
            ///  if(!funcCodes.contains(funcCode)){
            // 遍历出所有上级
            List<BdExpfunc> bdExpfuncUpList = bdExpfuncMapper.selectBdExpfuncUpList(year, funcCode);
            if (CollectionUtils.isNotEmpty(bdExpfuncUpList)) {
                bdExpfuncUpList.forEach(bdExpfuncUp -> {
                    /// if(!funcCodes.contains(bdExpfuncUp.getFuncCode())){
                    BdExpfunc newBdExpfunc = new BdExpfunc();
                    BeanUtil.copyProperties(bdExpfuncUp, newBdExpfunc);
                    newBdExpfunc.setUnitId(unitId);
                    newBdExpfunc.setUnitCode(sysDept.getDeptCode());
                    newBdExpfunc.setUnitName(sysDept.getDeptName());
                    newBdExpfunc.setFuncId(null);
                    newBdExpfunc.setParentId(null);
                    bdExpfuncInList.add(newBdExpfunc);
                    ///}
                });
            }
            ///}
        });
        List<BdExpfunc> collect = bdExpfuncInList.stream().distinct().collect(Collectors.toList());
        bdExpfuncMapper.deleteBdExpfuncByYear(unitId, year);
        if (CollectionUtils.isNotEmpty(collect)) {
            bdExpfuncMapper.insertBatchBdExpfunc(collect);
        }

        String k = BDEXPFUNC_ + bdExpfunc.getYear() + "_" + bdExpfunc.getUnitId();
        List<Zone> cacheList = redisUtil.lGet(k, 0, -1);
        if (CollectionUtils.isNotEmpty(cacheList) && CollectionUtils.isNotEmpty((List<Zone>) cacheList.get(0))) {
            redisUtil.del(k);
        }
        return selectBdExpfuncTree(bdExpfunc);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertBatchAssetType(List<BdExpfunc> bdExpfuncs, Long year) {
        bdExpfuncs.forEach(v -> {
            init(v);
            v.setYear(year);
        });
        checkBdExpfuncs(bdExpfuncs, year);
        int result = bdExpfuncMapper.insertBatchBdExpfunc(bdExpfuncs);

        String k = BDEXPFUNC_ + String.valueOf(year) + "_*";
        List<Zone> cacheList = redisUtil.lGet(k, 0, -1);
        if (CollectionUtils.isNotEmpty(cacheList) && CollectionUtils.isNotEmpty((List<Zone>) cacheList.get(0))) {
            redisUtil.del(k);
        }
        return result;
    }

    /**
     * 支出功能分类结转年度计算
     * oldYear：当前最新数据年度
     * newYear：需要结转至XX年份
     * info：
     * 1 xx年结转至xx年
     * 2 xx年重新结转至xx年
     * 0 提示：12月和1月才能进行结转
     *
     * @return 结果
     */
    @Override
    public Map<String, Integer> carryForwardYear() {
        // 获取当前数据最新年度
        int year = bdExpfuncMapper.selectLatestYear();
        // 判断是否可以结转或重新结转
        return CarryForwardUtils.carryForward(year);
    }

    /**
     * 支出功能分类年度结转
     *
     * @param year 目标年度
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void carryForwardData(String year) {
        if (StringUtils.isEmpty(year) || "0".equals(year)) {
            return;
        }
        // 删除结转目标年度数据
        bdExpfuncMapper.deleteBdExpfuncByYear(null, Long.valueOf(year));
        // 重新结转数据 (目标年度，被结转年度)
        bdExpfuncMapper.carryForwardData(Integer.valueOf(year), Integer.valueOf(year) - 1);
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


    private void checkBdExpfuncs(List<BdExpfunc> bdExpfuncs, Long year) {
        List<String> codes = bdExpfuncs.stream().map(BdExpfunc::getFuncCode).collect(Collectors.toList());
        List<BdExpfunc> oldList = bdExpfuncMapper.selectBdExpfuncByCodes(codes, year, "*");
        List<String> oldCodes = oldList.stream().map(BdExpfunc::getFuncCode).collect(Collectors.toList());
        StringBuilder sb = new StringBuilder("导入失败，第");
        boolean flag = false;
        for (int i = 0; i < bdExpfuncs.size(); i++) {
            String funcCode = bdExpfuncs.get(i).getFuncCode();
            String funcName = bdExpfuncs.get(i).getFuncName();
            if (oldCodes.contains(funcCode)) {
                flag = true;
                sb.append(i + 4).append("行[").append(funcCode).append("]").append(funcName).append("、");
            }
        }
        if (flag) {
            sb.deleteCharAt(sb.length() - 1);
            sb.append("编码重复！");
            throw new CustomException(sb.toString());
        }
    }

}
