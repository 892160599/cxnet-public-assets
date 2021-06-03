package com.cxnet.baseData.govexpeco.bdGovexpeco.mapper;

import com.cxnet.baseData.govexpeco.bdGovexpeco.domain.BdGovexpeco;
import com.cxnet.common.utils.tree.Zone;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 政府预算经济分类Mapper接口
 *
 * @author cxnet
 * @date 2020-08-17
 */
public interface BdGovexpecoMapper {
    /**
     * 查询政府预算经济分类
     *
     * @param govId 政府预算经济分类ID
     * @return 政府预算经济分类
     */
    public BdGovexpeco selectBdGovexpecoById(String govId);

    /**
     * 查询政府预算经济分类集合
     *
     * @param bdGovexpeco 政府预算经济分类
     * @return 政府预算经济分类集合
     */
    public List<BdGovexpeco> selectBdGovexpecoList(BdGovexpeco bdGovexpeco);

    /**
     * 新增政府预算经济分类
     *
     * @param bdGovexpeco 政府预算经济分类
     * @return 结果
     */
    public int insertBdGovexpeco(BdGovexpeco bdGovexpeco);

    /**
     * 批量新增政府预算经济分类
     *
     * @param bdGovexpecos 政府预算经济分类
     * @return 结果
     */
    public int insertBatchBdGovexpeco(List<BdGovexpeco> bdGovexpecos);

    /**
     * 修改政府预算经济分类
     *
     * @param bdGovexpeco 政府预算经济分类
     * @return 结果
     */
    public int updateBdGovexpeco(BdGovexpeco bdGovexpeco);

    /**
     * 批量修改政府预算经济分类
     *
     * @param bdGovexpecos 政府预算经济分类
     * @return 结果
     */
    public int updateBatchBdGovexpeco(List<BdGovexpeco> bdGovexpecos);

    /**
     * 删除政府预算经济分类
     *
     * @param govId 政府预算经济分类ID
     * @return 结果
     */
    public int deleteBdGovexpecoById(String govId);

    /**
     * 批量删除政府预算经济分类
     *
     * @param govIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteBdGovexpecoByIds(String[] govIds);

    /**
     * 查询政府预算经济分类Tree
     *
     * @param bdGovexpeco
     * @return
     */
    List<Zone> selectBdGovexpecoListTree(BdGovexpeco bdGovexpeco);

    /**
     * 是否存在下级节点
     *
     * @return
     */
    Integer existChildren(@Param("year") Long year, @Param("unitId") String unitId, @Param("govCode") String govCode);


    int checkCode(BdGovexpeco bdGovexpeco);

    int deleteBdGovexpecoByYear(@Param("unitId") String unitId, @Param("year") Long year);

    List<BdGovexpeco> selectBdGovexpecoByCodes(@Param("list") List<String> codes, @Param("year") Long year, @Param("unitId") String unitId);

    int selectLatestYear();

    void carryForwardData(@Param("newYear") Integer newYear, @Param("oldYear") Integer oldYear);

}
