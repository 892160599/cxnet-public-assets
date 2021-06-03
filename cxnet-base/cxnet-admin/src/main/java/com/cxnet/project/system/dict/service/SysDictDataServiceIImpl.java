package com.cxnet.project.system.dict.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollectionUtil;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.common.utils.tree.Zone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cxnet.project.system.dict.domain.SysDictData;
import com.cxnet.project.system.dict.mapper.SysDictDataMapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * 字典 业务层处理
 *
 * @author cxnet
 */
@Service
public class SysDictDataServiceIImpl implements SysDictDataServiceI {
    @Autowired(required = false)
    private SysDictDataMapper dictDataMapper;

    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    @Override
    public List<SysDictData> selectDictDataList(SysDictData dictData) {
        return dictDataMapper.selectDictDataList(dictData);
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @Override
    public List<SysDictData> selectDictDataByType(String dictType) {
        return dictDataMapper.selectDictDataByType(dictType);
    }

    /**
     * 根据字典类型查询字典MAP
     *
     * @param dictType 字典类型
     * @return
     */
    @Override
    public Map<String, String> selectDictDataMapByType(String dictType) {
        List<SysDictData> sysDictDatas = selectDictDataByType(dictType);
        if (CollectionUtil.isNotEmpty(sysDictDatas)) {
            return sysDictDatas.stream().collect(Collectors.toMap(SysDictData::getDictValue, SysDictData::getDictLabel));
        }
        return new HashMap<>(1);
    }

    /**
     * 根据字典类型查询字典MAP
     *
     * @param dictType 字典类型
     * @return
     */
    @Override
    public Map<String, String> selectMapByType(String dictType) {
        List<SysDictData> sysDictDatas = selectDictDataByType(dictType);
        if (CollectionUtil.isNotEmpty(sysDictDatas)) {
            return sysDictDatas.stream().collect(Collectors.toMap(SysDictData::getDictLabel, SysDictData::getDictValue));
        }
        return new HashMap<>(1);
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    @Override
    public String selectDictLabel(String dictType, String dictValue) {
        SysDictData dictData = new SysDictData();
        dictData.setDictType(dictType);
        dictData.setDictValue(dictValue);
        return dictDataMapper.selectDictLabel(dictData);
    }

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    @Override
    public SysDictData selectDictDataById(String dictCode) {
        return dictDataMapper.selectDictDataById(dictCode);
    }

    /**
     * 通过字典ID删除字典数据信息
     *
     * @param dictCode 字典数据ID
     * @return 结果
     */
    @Override
    public int deleteDictDataById(String dictCode) {
        return dictDataMapper.deleteDictDataById(dictCode);
    }

    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteDictDataByIds(String[] dictCodes) {
        int res = dictDataMapper.selectDictDataByPids(dictCodes);
        if (res > 0) {
            throw new CustomException("该字典存在下级，请逐级删除！");
        }
        return dictDataMapper.deleteDictDataByIds(dictCodes);
    }

    /**
     * 新增保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    @Override
    public int insertDictData(SysDictData dictData) {
        String result = dictDataMapper.selectDictLabel(dictData);
        if (StringUtils.isNotEmpty(result)) {
            throw new CustomException("字典键值重复");
        }
        return dictDataMapper.insertDictData(dictData);
    }

    /**
     * 修改保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    @Override
    public int updateDictData(SysDictData dictData) {
        String result = dictDataMapper.selectDictLabel(dictData);
        if (StringUtils.isNotEmpty(result)) {
            throw new CustomException("字典键值重复");
        }
        return dictDataMapper.updateDictData(dictData);
    }

    /**
     * 根据字典类型查询字典数据信息
     *
     * @param dictType 字典类型字典类型
     * @return 树形结构的信息
     */
    @Override
    public List<SysDictData> selectDictDataListTree(String dictType) {
        return dictDataMapper.selectDictDataListTree(dictType);
    }

    @Override
    public List<SysDictData> selectDictDataStatus(String dictType) {
        return dictDataMapper.selectDictDataStatus(dictType);
    }

    /**
     * 重置默认值
     */
    @Override
    public void updateDefault(String dictType) {
        dictDataMapper.updateDefault(dictType);
    }

    @Override
    public List<SysDictData> getDictDataAllByType(String dictType, String searchValue) {
        return dictDataMapper.getDictDataAllByType(dictType, searchValue);
    }
}
