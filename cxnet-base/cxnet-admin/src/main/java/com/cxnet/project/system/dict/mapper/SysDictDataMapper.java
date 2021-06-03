package com.cxnet.project.system.dict.mapper;

import java.util.List;

import com.cxnet.common.utils.tree.Zone;
import com.cxnet.project.system.dict.domain.SysDictType;
import org.apache.ibatis.annotations.Param;
import com.cxnet.project.system.dict.domain.SysDictData;

/**
 * 字典表 数据层
 *
 * @author cxnet
 */
public interface SysDictDataMapper {
    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    public List<SysDictData> selectDictDataList(SysDictData dictData);

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    public List<SysDictData> selectDictDataByType(String dictType);

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictData 字典数据
     * @return 字段数据
     */
    public String selectDictLabel(SysDictData dictData);

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    public SysDictData selectDictDataById(String dictCode);

    /**
     * 根据字典数据value,dictType查询信息
     *
     * @return 字典数据
     */
    public SysDictData selectDictDataByDictValueAndDictType(@Param("dictValue") String dictValue, @Param("dictType") String dictType);

    /**
     * 查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据
     */
    public int countDictDataByType(String dictType);

    /**
     * 通过字典ID删除字典数据信息
     *
     * @param dictCode 字典数据ID
     * @return 结果
     */
    public int deleteDictDataById(String dictCode);

    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     * @return 结果
     */
    public int deleteDictDataByIds(String[] dictCodes);

    int selectDictDataByPids(String[] dictCodes);

    /**
     * 新增字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    public int insertDictData(SysDictData dictData);

    /**
     * 修改字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    public int updateDictData(SysDictData dictData);

    /**
     * 同步修改字典类型
     *
     * @param oldDictType 旧字典类型
     * @param newDictType 新旧字典类型
     * @return 结果
     */
    public int updateDictDataType(@Param("oldDictType") String oldDictType, @Param("newDictType") String newDictType);

    /**
     * 根据字典类型查询字典数据信息
     * 是树的话就以树的形式展示
     *
     * @param dictType 字典类型字典类型
     * @return 树形结构的信息
     */
    public List<SysDictData> selectDictDataListTree(@Param("dictType") String dictType);

    List<SysDictData> selectDictDataStatus(@Param("dictType") String dictType);

    void updateDefault(String dictType);

    List<SysDictData> getDictDataAllByType(@Param("dictType") String dictType, @Param("searchValue") String searchValue);
}
