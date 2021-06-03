package com.cxnet.baseData.govexpeco.bdGovexpeco.mapper;

import com.cxnet.baseData.govexpeco.bdGovexpeco.domain.BdGovGovexpeco;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 政府/部门经济分类关系Mapper接口
 *
 * @author cxnet
 * @date 2020-08-17
 */
public interface BdGovGovexpecoMapper {
    /**
     * 查询政府/部门经济分类关系
     *
     * @param unitId 政府/部门经济分类关系ID
     * @return 政府/部门经济分类关系
     */
    public BdGovGovexpeco selectBdGovExpecoById(String unitId);

    /**
     * 查询政府/部门经济分类关系集合
     *
     * @param bdGovExpeco 政府/部门经济分类关系
     * @return 政府/部门经济分类关系集合
     */
    public List<BdGovGovexpeco> selectBdGovExpecoList(BdGovGovexpeco bdGovExpeco);

    /**
     * 新增政府/部门经济分类关系
     *
     * @param bdGovExpeco 政府/部门经济分类关系
     * @return 结果
     */
    public int insertBdGovExpeco(BdGovGovexpeco bdGovExpeco);

    /**
     * 批量新增政府/部门经济分类关系
     *
     * @param bdGovExpecos 政府/部门经济分类关系
     * @return 结果
     */
    public int insertBatchBdGovExpeco(List<BdGovGovexpeco> bdGovExpecos);

    /**
     * 修改政府/部门经济分类关系
     *
     * @param bdGovExpeco 政府/部门经济分类关系
     * @return 结果
     */
    public int updateBdGovExpeco(BdGovGovexpeco bdGovExpeco);

    /**
     * 批量修改政府/部门经济分类关系
     *
     * @param bdGovExpecos 政府/部门经济分类关系
     * @return 结果
     */
    public int updateBatchBdGovExpeco(List<BdGovGovexpeco> bdGovExpecos);

    /**
     * 删除政府/部门经济分类关系
     *
     * @param unitId 政府/部门经济分类关系ID
     * @return 结果
     */
    public int deleteBdGovExpecoById(String unitId);

    /**
     * 批量删除政府/部门经济分类关系
     *
     * @param unitIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteBdGovExpecoByIds(String[] unitIds);

    List<BdGovGovexpeco> selectBdGovGovexpecoByYearGovCode(@Param("year") Long year, @Param("govCode") String govCode);

    List<BdGovGovexpeco> selectBdGovGovexpecoByYearExpecoCode(@Param("year") Long year, @Param("expecoCode") String expecoCode, @Param("unitQuality") String unitQuality);

    void deleteBdGovExpeco(@Param("year") Long year, @Param("govCode") String govCode);

}
