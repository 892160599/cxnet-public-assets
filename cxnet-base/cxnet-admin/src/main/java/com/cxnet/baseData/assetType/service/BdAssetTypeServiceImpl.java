package com.cxnet.baseData.assetType.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.baseData.assetType.domain.BdAnnex;
import com.cxnet.baseData.assetType.domain.BdAssetType;
import com.cxnet.baseData.assetType.domain.SelectionBdAssetType;
import com.cxnet.baseData.assetType.domain.vo.BdAssetTypeVo;
import com.cxnet.baseData.assetType.mapper.BdAssetTypeMapper;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.DateUtils;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.common.utils.tree.Zone;
import com.cxnet.common.utils.tree.ZoneUtils;
import com.cxnet.framework.security.SecurityUtils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * 资产类别Service业务层处理
 *
 * @author caixx
 * @date 2020-07-20
 */
@Service
public class BdAssetTypeServiceImpl extends ServiceImpl<BdAssetTypeMapper, BdAssetType> implements BdAssetTypeService {

    @Autowired(required = false)
    private BdAssetTypeMapper assetTypeMapper;
    @Autowired(required = false)
    private BdAnnexService bdAnnexService;
    @Autowired(required = false)
    private BdAssetTypeService bdAssetTypeService;

    /**
     * 查询资产类别
     *
     * @param assetId 资产类别ID
     * @return 资产类别
     */
    @Override
    public BdAssetType selectAssetTypeById(String assetId) {
        return assetTypeMapper.selectAssetTypeById(assetId);
    }

    @Override
    public BdAssetTypeVo selectOne(String assetId) {
        BdAssetTypeVo bdAssetTypeVo = new BdAssetTypeVo();
        QueryWrapper<BdAssetType> typeQw = new QueryWrapper<>();
        typeQw.lambda().eq(BdAssetType::getAssetId, assetId).eq(BdAssetType::getStatus, "0").eq(BdAssetType::getDelFlag, "0");
        List<BdAssetType> typeList = bdAssetTypeService.list(typeQw);
        if (typeList.size() == 0) {
            throw new CustomException("单据不存在或已被删除，请刷新后重试！");
        }
        QueryWrapper<BdAnnex> annexQw = new QueryWrapper<>();
        annexQw.lambda().eq(BdAnnex::getAstId, typeList.get(0).getAssetId());
        List<BdAnnex> annexList = bdAnnexService.list(annexQw);
        bdAssetTypeVo.setBdAssetType(typeList.get(0));
        bdAssetTypeVo.setBdAnnexes(annexList);
        return bdAssetTypeVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String update(BdAssetTypeVo assetTypeVo) {
        BdAssetType bdAssetType = assetTypeVo.getBdAssetType();
        if (ObjectUtil.isNull(bdAssetType) || "2".equals(bdAssetType.getDelFlag())) {
            throw new CustomException("单据不存在或已被删除，请刷新后重试！");
        }
        List<BdAnnex> bdAnnexes = assetTypeVo.getBdAnnexes();
        //得到计量单位集合，并转换成字符串
        List<String> measurement = assetTypeVo.getMeasurement();
        String ureMent = "";
        for (String ment : measurement) {
            ureMent += ment + ",";
        }
        ureMent = ureMent.substring(0, ureMent.length() - 1);
        bdAssetType.setMeasurement(ureMent);
        //保存主表
        QueryWrapper<BdAssetType> typeQw = new QueryWrapper<>();
        typeQw.lambda().eq(BdAssetType::getAssetId, bdAssetType.getAssetId());
        bdAssetTypeService.update(bdAssetType, typeQw);
        // 修改附件表
        QueryWrapper<BdAnnex> Wrapper = new QueryWrapper<>();
        Wrapper.lambda().eq(BdAnnex::getAstId, bdAssetType.getAssetId());
        bdAnnexService.remove(Wrapper);
        bdAnnexes.forEach(v -> v.setAstId(bdAssetType.getAssetId()));
        bdAnnexService.saveBatch(bdAnnexes);
        return bdAssetType.getAssetId();
    }

    /**
     * 查询资产类别集合
     *
     * @param assetType 资产类别
     * @return 资产类别
     */
    @Override
    public List<BdAssetType> selectAssetTypeList(BdAssetType assetType) {
        assetType.setDelFlag("0");
        return assetTypeMapper.selectAssetTypeList(assetType);
    }

    /**
     * 查询资产类别集合tree
     *
     * @return 资产类别
     */
    @Override
    public List<Zone> selectAssetTypeListTree(BdAssetType bdAssetType) {
        if (StringUtils.isEmpty(bdAssetType.getUnitId())) {
            bdAssetType.setUnitId("*");
        }
        bdAssetType.setDelFlag("0");
        List<Zone> zoneList = assetTypeMapper.selectAssetTypeListTree(bdAssetType);
        return ZoneUtils.buildTreeBypcode(zoneList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Zone> insertBatchSelectAssetType(SelectionBdAssetType assetType) {
        String[] assetCodes = assetType.getAssetCodes();
        String unitId = assetType.getUnitId();
        if (StringUtils.isEmpty(unitId)) {
            throw new CustomException("参数异常！");
        }
        List<BdAssetType> bdAssetTypeList = new ArrayList<>();
        List<BdAssetType> typeList = new ArrayList<>();
        if (ArrayUtil.isNotEmpty(assetCodes)) {
            typeList = assetTypeMapper.selectBdAssetType(assetCodes);
        }
        BdAssetType bdAssetType = new BdAssetType(unitId);
        // 已绑定list
        List<BdAssetType> assetTypeList = assetTypeMapper.selectAssetTypeList(bdAssetType);
        List<String> funcCodes = assetTypeList.stream().map(BdAssetType::getAssetId).collect(Collectors.toList());
        typeList.forEach(v -> {
            String assetCode = v.getAssetCode();
            ///   if(!funcCodes.contains(expecoCode)){
            // 遍历出所有上级
            List<BdAssetType> types = assetTypeMapper.selectBdAssetTypeList(assetCode);
            if (CollectionUtils.isNotEmpty(types)) {
                types.forEach(bdExpfuncUp -> {
                    ///if(!funcCodes.contains(bdExpfuncUp.getExpecoCode())){
                    BdAssetType newbdAssetType = new BdAssetType();
                    BeanUtil.copyProperties(bdExpfuncUp, newbdAssetType);
                    newbdAssetType.setUnitId(unitId);
                    newbdAssetType.setAssetId(null);
                    newbdAssetType.setParentId(null);
                    newbdAssetType.setMeasurement("1");
                    newbdAssetType.setAssetUselife(1);
                    newbdAssetType.setDepreciationMethod("01");
                    bdAssetTypeList.add(newbdAssetType);
                });
            }
        });
        List<BdAssetType> collect = bdAssetTypeList.stream().distinct().collect(Collectors.toList());
        assetTypeMapper.deleteBdAssetType(unitId);
        if (CollectionUtils.isNotEmpty(collect)) {
            this.saveBatch(collect);
        }
        for (BdAssetType entity : collect) {
            if (StringUtils.isNotEmpty(entity.getParentCode())) {
                QueryWrapper<BdAssetType> wrapper = new QueryWrapper<>();
                wrapper.lambda().eq(BdAssetType::getAssetCode, entity.getParentCode()).eq(BdAssetType::getUnitId, entity.getUnitId());
                BdAssetType parentBd = assetTypeMapper.selectOne(wrapper);
                entity.setParentId(parentBd == null ? null : parentBd.getAssetId());
                this.updateAssetUnid(entity);
            }
        }
        return selectAssetTypeListTree(bdAssetType);
    }


    /**
     * 新增资产类别
     *
     * @param assetType 资产类别
     * @return 结果
     */
    @Override
    public int insertAssetType(BdAssetType assetType) {
        if (checkAssetCode(assetType.getAssetCode())) {
            throw new CustomException("编码重复！");
        }
        init(assetType);
        return assetTypeMapper.insertAssetType(assetType);
    }

    /**
     * 批量新增资产类别
     *
     * @param assetTypes 资产类别
     * @return 结果
     */
    @Override
    public int insertBatchAssetType(List<BdAssetType> assetTypes) {
        assetTypes.forEach(this::init);

        return assetTypeMapper.insertBatchAssetType(assetTypes);
    }

    private void init(BdAssetType assetType) {
        assetType.setDelFlag("0");
        assetType.setStatus("0");
        assetType.setUnitId("*");
        assetType.setCreateBy(SecurityUtils.getUsername());
        assetType.setCreateTime(DateUtils.getNowDate());
    }

    /**
     * 修改资产类别
     *
     * @param assetType 资产类别
     * @return 结果
     */
    @Override
    public int updateAssetType(BdAssetType assetType) {
        if (assetTypeMapper.checkAssetCode(assetType.getAssetCode(), assetType.getAssetId()) > 0) {
            throw new CustomException("编码重复！");
        }
        assetType.setUpdateBy(SecurityUtils.getUsername());
        assetType.setUpdateTime(DateUtils.getNowDate());
        return assetTypeMapper.updateAssetType(assetType);
    }

    @Override
    public int updateAssetUnid(BdAssetType assetType) {
//        if (assetTypeMapper.updateAssetUnid(assetType.getAssetCode(), assetType.getAssetId(),assetType.getUnitId()) > 0) {
//            throw new CustomException("编码重复！");
//        }
        assetType.setUpdateBy(SecurityUtils.getUsername());
        assetType.setUpdateTime(DateUtils.getNowDate());
        return assetTypeMapper.updateAssetType(assetType);
    }

    /**
     * 批量修改资产类别
     *
     * @param assetTypes 资产类别
     * @return 结果
     */
    @Override
    public int updateBatchAssetType(List<BdAssetType> assetTypes) {
        return assetTypeMapper.updateBatchAssetType(assetTypes);
    }

    /**
     * 批量删除资产类别
     *
     * @param assetIds 需要删除的资产类别ID
     * @return 结果
     */
    @Override
    public int deleteAssetTypeByIds(String[] assetIds) {
        int i = assetTypeMapper.existChildren(assetIds);
        if (i > 0) {
            throw new CustomException("存在下级资产类别，无法删除！");
        }
        return assetTypeMapper.deleteAssetTypeByIds(assetIds);
    }

    /**
     * 删除资产类别信息
     *
     * @param assetId 资产类别ID
     * @return 结果
     */
    @Override
    public int deleteAssetTypeById(String assetId) {
        return assetTypeMapper.deleteAssetTypeById(assetId);
    }

    /**
     * 校验编码是否存在
     *
     * @param assetCode
     * @return
     */
    @Override
    public boolean checkAssetCode(String assetCode) {
        return assetTypeMapper.checkAssetCode(assetCode, null) > 0;
    }

    /**
     * 编码校验
     *
     * @param assetTypes
     */
    @Override
    public void checkAssetTypes(List<BdAssetType> assetTypes) {
        List<String> codes = assetTypes.stream().map(BdAssetType::getAssetCode).collect(Collectors.toList());
        List<BdAssetType> oldAssetTypes = assetTypeMapper.selectAssetTypeByCode(codes);
        List<String> oldCodes = oldAssetTypes.stream().map(BdAssetType::getAssetCode).collect(Collectors.toList());
        StringBuilder sb = new StringBuilder("导入失败，第");
        boolean flag = false;
        for (int i = 0; i < assetTypes.size(); i++) {
            String assetCode = assetTypes.get(i).getAssetCode();
            String assetName = assetTypes.get(i).getAssetName();
            if (oldCodes.contains(assetCode)) {
                flag = true;
                sb.append(i + 4).append("行[").append(assetCode).append("]").append(assetName).append("、");
            }
        }
        if (flag) {
            sb.deleteCharAt(sb.length() - 1);
            sb.append("编码重复！");
            throw new CustomException(sb.toString());
        }
    }


}
