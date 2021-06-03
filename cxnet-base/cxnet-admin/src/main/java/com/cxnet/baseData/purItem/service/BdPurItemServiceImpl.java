package com.cxnet.baseData.purItem.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.cxnet.baseData.assetType.domain.BdAssetType;
import com.cxnet.baseData.assetType.mapper.BdAssetTypeMapper;
import com.cxnet.baseData.purItem.domain.BdItemAsset;
import com.cxnet.baseData.purItem.domain.BdPurItem;
import com.cxnet.baseData.purItem.domain.SelectionPurItem;
import com.cxnet.baseData.purItem.domain.vo.BdPurItemVo;
import com.cxnet.baseData.purItem.mapper.BdItemAssetMapper;
import com.cxnet.baseData.purItem.mapper.BdPurItemMapper;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.CarryForwardUtils;
import com.cxnet.common.utils.DateUtils;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.common.utils.tree.Zone;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.project.system.dept.domain.SysDept;
import com.cxnet.project.system.dept.mapper.SysDeptMapper;
import com.cxnet.project.system.dict.domain.SysDictData;
import com.cxnet.project.system.dict.mapper.SysDictDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 采购品目Service业务层处理
 *
 * @author caixx
 * @date 2020-07-21
 */
@Service
public class BdPurItemServiceImpl implements BdPurItemServiceI {
    @Autowired(required = false)
    private BdPurItemMapper purItemMapper;
    @Autowired(required = false)
    private BdAssetTypeMapper assetTypeMapper;
    @Autowired(required = false)
    private BdItemAssetMapper itemAssetMapper;
    @Autowired(required = false)
    private SysDeptMapper sysDeptMapper;
    @Autowired(required = false)
    private SysDictDataMapper dictDataMapper;


    /**
     * 查询采购品目
     *
     * @param itemId 采购品目ID
     * @return 采购品目
     */
    @Override
    public BdPurItem selectPurItemById(String itemId) {
        return purItemMapper.selectPurItemById(itemId);
    }

    /**
     * 查询采购品目vo
     *
     * @param itemId 采购品目ID
     * @return 采购品目
     */
    @Override
    public BdPurItemVo selectPurItemAndAssetTypeById(String itemId) {
        BdPurItemVo purItemVo = new BdPurItemVo();
        // 主表
        BdPurItem purItem = this.selectPurItemById(itemId);
        List<BdAssetType> assetTypeList = new ArrayList<>();
        if (purItem != null) {
            // 子表
            assetTypeList = assetTypeMapper.selectAssetTypeListById(purItem.getItemCode());
            if (CollectionUtils.isNotEmpty(assetTypeList)) {
                purItemVo.setAssetTypeList(assetTypeList);
            }
            purItemVo.setPurItem(purItem);
        }
        return purItemVo;
    }

    /**
     * 查询采购品目集合
     *
     * @param purItem 采购品目
     * @return 采购品目
     */
    @Override
    public List<BdPurItem> selectPurItemList(BdPurItem purItem) {
        return purItemMapper.selectPurItemList(purItem);
    }

    /**
     * 新增采购品目
     *
     * @param purItem 采购品目
     * @return 结果
     */
    @Override
    public int insertPurItem(BdPurItem purItem) {
        purItem.setCreateBy(SecurityUtils.getUsername());
        purItem.setCreateTime(DateUtils.getNowDate());
        return purItemMapper.insertPurItem(purItem);
    }

    /**
     * 批量新增采购品目
     *
     * @param purItems 采购品目
     * @return 结果
     */
    @Override
    public int insertBatchPurItem(List<BdPurItem> purItems) {
        return purItemMapper.insertBatchPurItem(purItems);
    }

    /**
     * 修改采购品目
     *
     * @param purItem 采购品目
     * @return 结果
     */
    @Override
    public int updatePurItem(BdPurItem purItem) {
        purItem.setUpdateBy(SecurityUtils.getUsername());
        purItem.setUpdateTime(DateUtils.getNowDate());
        return purItemMapper.updatePurItem(purItem);
    }

    /**
     * 修改采购品目和子表
     *
     * @param purItemVo 采购品目vo
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updatePurItemAndAssetType(BdPurItemVo purItemVo) {
        BdPurItem purItem = purItemVo.getPurItem();
        List<BdAssetType> assetTypeList = purItemVo.getAssetTypeList();
        // 修改主表
        int i = this.updatePurItem(purItem);
        String itemCode = purItem.getItemCode();
        itemAssetMapper.deleteBdItemAssetByCode(itemCode);
        if (i > 0 && CollectionUtil.isNotEmpty(assetTypeList)) {
            // 修改子表
            List<BdItemAsset> bdItemAssets = new ArrayList<>();
            assetTypeList.forEach(v -> bdItemAssets.add(new BdItemAsset(itemCode, v.getAssetId())));
            itemAssetMapper.insertBatchBdItemAsset(bdItemAssets);
        }
        return i;
    }


    /**
     * 批量修改采购品目
     *
     * @param purItems 采购品目
     * @return 结果
     */
    @Override
    public int updateBatchPurItem(List<BdPurItem> purItems) {
        return purItemMapper.updateBatchPurItem(purItems);
    }

    /**
     * 批量删除采购品目
     *
     * @param itemIds 需要删除的采购品目ID
     * @return 结果
     */
    @Override
    public int deletePurItemByIds(String[] itemIds) {
        return purItemMapper.deletePurItemByIds(itemIds);
    }

    /**
     * 删除采购品目信息
     *
     * @param itemId 采购品目ID
     * @return 结果
     */
    @Override
    public int deletePurItemById(String itemId) {
        return purItemMapper.deletePurItemById(itemId);
    }

    /**
     * 获取采购品目下拉树列表
     */
    @Override
    public List<Map> treeselectList() {
        return purItemMapper.treeselectList();
    }

    /**
     * 获取采购品目下拉树列表
     *
     * @param bdPurItem 业务年度
     * @return
     */
    @Override
    public List<Zone> selectPurItemListTree(BdPurItem bdPurItem) {
        if (bdPurItem.getPurYear() == null) {
            throw new CustomException("缺少采购品目年度！");
        }
        bdPurItem.setDelFlag("0");
        if (StringUtils.isEmpty(bdPurItem.getUnitId())) {
            bdPurItem.setUnitId("*");
            bdPurItem.setUnitCode("*");
            bdPurItem.setUnitName("*");
        }
        return purItemMapper.selectPurItemListTree(bdPurItem);
    }

    /**
     * 校验编码
     *
     * @param itemCode
     * @return
     */
    @Override
    public boolean checkItemCode(String itemCode) {
        return purItemMapper.checkItemCode(itemCode) > 0;
    }

    @Override
    public boolean checkItemCodeUpdate(BdPurItemVo purItemVo) {
        BdPurItem bdPurItem = purItemMapper.selectPurItemById(purItemVo.getPurItem().getItemId());
        boolean flag = false;
        if (!purItemVo.getPurItem().getItemCode().equals(bdPurItem.getItemCode())) {
            int result = purItemMapper.checkItemCode(purItemVo.getPurItem().getItemCode());
            flag = result >= 1;
        }
        return flag;
    }

    /**
     * 新增品目信息和子表信息
     *
     * @param purItemVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertPurItemAndAssetType(BdPurItemVo purItemVo) {
        BdPurItem purItem = purItemVo.getPurItem();
        List<BdAssetType> assetTypeList = purItemVo.getAssetTypeList();
        // 保存主表
        int i = this.insertPurItem(purItem);
        if (i > 0 && CollectionUtil.isNotEmpty(assetTypeList)) {
            // 保存子表
            String itemCode = purItem.getItemCode();
            List<BdItemAsset> bdItemAssets = new ArrayList<>();
            assetTypeList.forEach(v -> bdItemAssets.add(new BdItemAsset(itemCode, v.getAssetId())));
            itemAssetMapper.insertBatchBdItemAsset(bdItemAssets);
        }
        return i;
    }


    @Override
    public void checkImportItemCode(List<BdPurItem> purItemList) {
        List<String> codes = purItemList.stream().map(BdPurItem::getItemCode).collect(Collectors.toList());
        List<BdPurItem> oldItem = purItemMapper.selectItemByCode(codes);
        List<String> oldCodes = oldItem.stream().map(BdPurItem::getItemCode).collect(Collectors.toList());
        StringBuilder sb = new StringBuilder("导入失败，第");
        boolean flag = false;
        for (int i = 0; i < codes.size(); i++) {
            if (oldCodes.contains(codes.get(i))) {
                flag = true;
                sb.append(i + 4).append("行、");
            }
        }
        if (flag) {
            sb.deleteCharAt(sb.length() - 1);
            sb.append("编码重复！");
            throw new CustomException(sb.toString());
        }
    }

    @Override
    public boolean hasChildByItemCode(String itemId) {
        return purItemMapper.hasChildByItemCode(itemId) > 0;
    }

    /**
     * 单位选用采购品目
     *
     * @param selectionPurItem
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Zone> insertBatchSelectionPurItem(SelectionPurItem selectionPurItem) {
        String[] ids = selectionPurItem.getId();
        Long purYear = selectionPurItem.getPurYear();
        String unitId = selectionPurItem.getUnitId();

        BdPurItem purItem = new BdPurItem();
        purItem.setPurYear(purYear);
        purItem.setUnitId(unitId);
        List<BdPurItem> purItems = purItemMapper.selectPurItemList(purItem);
        List<String> oldIds = new ArrayList<>();
        List<String> newIds = new ArrayList<>(Arrays.asList(ids));
        List<BdPurItem> itemCodeList = purItems.stream().filter(d1 -> newIds.stream().noneMatch(d2 -> Objects.equals(d1.getItemCode(), d2))).collect(Collectors.toList());
        if (itemCodeList.size() > 0) {
            itemCodeList.forEach(v -> oldIds.add(v.getItemId()));
            purItemMapper.deletePurItemByIdList(oldIds);
        }

        SysDept sysDept = sysDeptMapper.selectDeptById(unitId);
        List<BdPurItem> bdPurItems = new ArrayList<>();
        List<BdPurItem> bdPurItemList = purItemMapper.selectPurItemByIds(ids);
        BdPurItem bdPurItem = new BdPurItem(purYear, unitId);
        // 已绑定list
        List<BdPurItem> bdPurItemList1 = purItemMapper.selectPurItemList(bdPurItem);
        List<String> itemCodes = bdPurItemList1.stream().map(BdPurItem::getItemCode).collect(Collectors.toList());
        bdPurItemList.forEach(v -> {
            String itemCode = v.getItemCode();
            if (!itemCodes.contains(itemCode)) {
                // 遍历出所有上级
                List<BdPurItem> bdPurItemList2 = purItemMapper.selectBdItemUpList(purYear, itemCode);
                if (CollectionUtils.isNotEmpty(bdPurItemList2)) {
                    bdPurItemList2.forEach(bdPurItemUp -> {
                        if (!itemCodes.contains(bdPurItemUp.getItemCode())) {
                            BdPurItem newBdPurItem = new BdPurItem();
                            BeanUtil.copyProperties(bdPurItemUp, newBdPurItem);
                            newBdPurItem.setUnitId(unitId);
                            newBdPurItem.setItemId(null);
                            newBdPurItem.setParentId(null);
                            bdPurItems.add(newBdPurItem);
                        }
                    });
                }
            }
        });
        List<BdPurItem> collect = bdPurItems.stream().distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)) {
            purItemMapper.insertBatchPurItem(collect);
        }
        return selectPurItemListTree(bdPurItem);
    }

    @Override
    public Map<String, Integer> carryForwardYear() {
        // 获取当前数据最新年度
        int year = purItemMapper.selectLatestYear();
        // 判断是否可以结转或重新结转
        return CarryForwardUtils.carryForward(year);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void carryForwardData(String year) {
        if (StringUtils.isEmpty(year) || "0".equals(year)) {
            return;
        }
        // 删除结转目标年度数据
        purItemMapper.deletePurItemByYear(null, Long.valueOf(year));
        // 重新结转数据 (目标年度，被结转年度)
        purItemMapper.carryForwardData(Integer.valueOf(year), Integer.valueOf(year) - 1);
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

}
