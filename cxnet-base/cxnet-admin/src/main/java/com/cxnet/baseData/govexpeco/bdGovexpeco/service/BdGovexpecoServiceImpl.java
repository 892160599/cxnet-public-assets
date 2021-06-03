package com.cxnet.baseData.govexpeco.bdGovexpeco.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.cxnet.baseData.govexpeco.bdGovexpeco.domain.BdGovGovexpeco;
import com.cxnet.baseData.govexpeco.bdGovexpeco.domain.BdGovexpeco;
import com.cxnet.baseData.govexpeco.bdGovexpeco.mapper.BdGovGovexpecoMapper;
import com.cxnet.baseData.govexpeco.bdGovexpeco.mapper.BdGovexpecoMapper;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.CarryForwardUtils;
import com.cxnet.common.utils.DateUtils;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.common.utils.tree.Zone;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.project.system.dict.domain.SysDictData;
import com.cxnet.project.system.dict.mapper.SysDictDataMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 政府预算经济分类Service业务层处理
 *
 * @author cxnet
 * @date 2020-08-17
 */
@Service
public class BdGovexpecoServiceImpl implements BdGovexpecoServiceI {
    @Autowired(required = false)
    private BdGovexpecoMapper bdGovexpecoMapper;
    @Autowired(required = false)
    private BdGovGovexpecoMapper bdGovGovexpecoMapper;
    @Autowired(required = false)
    private SysDictDataMapper dictDataMapper;

    /**
     * 查询政府预算经济分类
     *
     * @param govId 政府预算经济分类ID
     * @return 政府预算经济分类
     */
    @Override
    public BdGovexpeco selectBdGovexpecoById(String govId) {
        BdGovexpeco bdGovexpeco = bdGovexpecoMapper.selectBdGovexpecoById(govId);
        if (ObjectUtil.isNotNull(bdGovexpeco)) {
            List<BdGovGovexpeco> bdGovGovexpecos = bdGovGovexpecoMapper.selectBdGovGovexpecoByYearGovCode(bdGovexpeco.getYear(), bdGovexpeco.getGovCode());
            if (CollectionUtil.isNotEmpty(bdGovGovexpecos)) {
                bdGovexpeco.setBdGovGovexpecos(bdGovGovexpecos);
            }
        }
        return bdGovexpeco;
    }

    /**
     * 查询政府预算经济分类集合
     *
     * @param bdGovexpeco 政府预算经济分类
     * @return 政府预算经济分类
     */
    @Override
    public List<BdGovexpeco> selectBdGovexpecoList(BdGovexpeco bdGovexpeco) {
        if (bdGovexpeco.getYear() == null) {
            throw new CustomException("缺少业务年度！");
        }
        bdGovexpeco.setDelFlag("0");
        return bdGovexpecoMapper.selectBdGovexpecoList(bdGovexpeco);
    }

    /**
     * 查询政府预算经济分类Tree
     *
     * @param bdGovexpeco
     * @return
     */
    @Override
    public List<Zone> selectBdGovexpecoListTree(BdGovexpeco bdGovexpeco) {
        if (bdGovexpeco.getYear() == null) {
            throw new CustomException("缺少业务年度！");
        }
        if (StringUtils.isEmpty(bdGovexpeco.getUnitId())) {
            bdGovexpeco.setUnitId("*");
        }
        bdGovexpeco.setDelFlag("0");
        return bdGovexpecoMapper.selectBdGovexpecoListTree(bdGovexpeco);
    }

    /**
     * 政府经济分类结转年度计算
     *
     * @return 结果
     */
    @Override
    public Map<String, Integer> carryForwardYear() {
        // 获取当前数据最新年度
        int year = bdGovexpecoMapper.selectLatestYear();
        // 判断是否可以结转或重新结转
        return CarryForwardUtils.carryForward(year);
    }

    /**
     * 政府经济分类结转
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
        bdGovexpecoMapper.deleteBdGovexpecoByYear(null, Long.valueOf(year));
        // 重新结转数据 (目标年度，被结转年度)
        bdGovexpecoMapper.carryForwardData(Integer.valueOf(year), Integer.valueOf(year) - 1);
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
     * 新增政府预算经济分类
     *
     * @param bdGovexpeco 政府预算经济分类
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertBdGovexpeco(BdGovexpeco bdGovexpeco) {
        init(bdGovexpeco);
        if (checkBdGovexpeco(bdGovexpeco)) {
            throw new CustomException("编码重复！");
        }
        List<BdGovGovexpeco> bdGovGovexpecos = bdGovexpeco.getBdGovGovexpecos();
        if (CollectionUtils.isNotEmpty(bdGovGovexpecos)) {
            bdGovGovexpecos.forEach(v -> v.setUnitId("*"));
            bdGovGovexpecoMapper.insertBatchBdGovExpeco(bdGovGovexpecos);
        }
        return bdGovexpecoMapper.insertBdGovexpeco(bdGovexpeco);
    }

    private void init(BdGovexpeco bdGovexpeco) {
        if (StringUtils.isEmpty(bdGovexpeco.getUnitId())) {
            bdGovexpeco.setUnitId("*");
        }
        bdGovexpeco.setDelFlag("0");
        bdGovexpeco.setStatus("0");
        bdGovexpeco.setCreateBy(SecurityUtils.getUsername());
        bdGovexpeco.setCreateTime(DateUtils.getNowDate());
    }

    /**
     * 批量新增政府预算经济分类
     *
     * @param bdGovexpecos 政府预算经济分类
     * @return 结果
     */
    @Override
    public int insertBatchBdGovexpeco(List<BdGovexpeco> bdGovexpecos, Long year) {
        bdGovexpecos.forEach(v -> {
            init(v);
            v.setYear(year);
        });
        checkBdGovexpecos(bdGovexpecos, year);
        return bdGovexpecoMapper.insertBatchBdGovexpeco(bdGovexpecos);
    }

    private void checkBdGovexpecos(List<BdGovexpeco> bdGovexpecos, Long year) {
        List<String> codes = bdGovexpecos.stream().map(BdGovexpeco::getGovCode).collect(Collectors.toList());
        List<BdGovexpeco> oldList = bdGovexpecoMapper.selectBdGovexpecoByCodes(codes, year, "*");
        List<String> oldCodes = oldList.stream().map(BdGovexpeco::getGovCode).collect(Collectors.toList());
        StringBuilder sb = new StringBuilder("导入失败，第");
        boolean flag = false;
        for (int i = 0; i < bdGovexpecos.size(); i++) {
            String govCode = bdGovexpecos.get(i).getGovCode();
            String govName = bdGovexpecos.get(i).getGovName();
            if (oldCodes.contains(govCode)) {
                flag = true;
                sb.append(i + 4).append("行[").append(govCode).append("]").append(govName).append("、");
            }
        }
        if (flag) {
            sb.deleteCharAt(sb.length() - 1);
            sb.append("编码重复！");
            throw new CustomException(sb.toString());
        }
    }

    /**
     * 修改政府预算经济分类
     *
     * @param bdGovexpeco 政府预算经济分类
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateBdGovexpeco(BdGovexpeco bdGovexpeco) {
        if (checkBdGovexpeco(bdGovexpeco)) {
            throw new CustomException("编码重复！");
        }
        bdGovexpeco.setUpdateBy(SecurityUtils.getUsername());
        bdGovexpeco.setUpdateTime(DateUtils.getNowDate());
        List<BdGovGovexpeco> bdGovGovexpecos = bdGovexpeco.getBdGovGovexpecos();
        bdGovGovexpecoMapper.deleteBdGovExpeco(bdGovexpeco.getYear(), bdGovexpeco.getGovCode());
        if (CollectionUtils.isNotEmpty(bdGovGovexpecos)) {
            bdGovGovexpecos.forEach(v -> v.setUnitId("*"));
            bdGovGovexpecoMapper.insertBatchBdGovExpeco(bdGovGovexpecos);
        }
        return bdGovexpecoMapper.updateBdGovexpeco(bdGovexpeco);
    }

    private boolean checkBdGovexpeco(BdGovexpeco bdGovexpeco) {
        return bdGovexpecoMapper.checkCode(bdGovexpeco) > 0;
    }

    /**
     * 批量修改政府预算经济分类
     *
     * @param bdGovexpecos 政府预算经济分类
     * @return 结果
     */
    @Override
    public int updateBatchBdGovexpeco(List<BdGovexpeco> bdGovexpecos) {
        return bdGovexpecoMapper.updateBatchBdGovexpeco(bdGovexpecos);
    }

    /**
     * 批量删除政府预算经济分类
     *
     * @param govIds 需要删除的政府预算经济分类ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteBdGovexpecoByIds(String[] govIds) {
        return bdGovexpecoMapper.deleteBdGovexpecoByIds(govIds);
    }

    /**
     * 删除政府预算经济分类信息
     *
     * @param govId 政府预算经济分类ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteBdGovexpecoById(String govId) {
        BdGovexpeco bdGovexpeco = bdGovexpecoMapper.selectBdGovexpecoById(govId);
        if (bdGovexpeco == null) {
            throw new CustomException("未查询到要删除的政府预算经济分类信息！");
        }
        Integer i = bdGovexpecoMapper.existChildren(bdGovexpeco.getYear(), bdGovexpeco.getUnitId(), bdGovexpeco.getGovCode());
        if (i != 0) {
            throw new CustomException("存在下级政府预算经济分类信息，无法删除！");
        }
        bdGovexpecoMapper.deleteBdGovexpecoById(govId);
        bdGovGovexpecoMapper.deleteBdGovExpeco(bdGovexpeco.getYear(), bdGovexpeco.getGovCode());
        return 1;
    }


}
