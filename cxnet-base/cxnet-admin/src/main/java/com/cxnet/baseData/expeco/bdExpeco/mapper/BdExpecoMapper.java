package com.cxnet.baseData.expeco.bdExpeco.mapper;

import com.cxnet.baseData.expeco.bdExpeco.domain.BdExpeco;
import com.cxnet.common.utils.tree.Zone;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门预算经济分类Mapper接口
 *
 * @author cxnet
 * @date 2020-08-17
 */
public interface BdExpecoMapper {
    /**
     * 查询部门预算经济分类
     *
     * @param expecoId 部门预算经济分类ID
     * @return 部门预算经济分类
     */
    public BdExpeco selectBdExpecoById(String expecoId);

    /**
     * 查询部门预算经济分类集合
     *
     * @param bdExpeco 部门预算经济分类
     * @return 部门预算经济分类集合
     */
    public List<BdExpeco> selectBdExpecoList(BdExpeco bdExpeco);

    /**
     * 新增部门预算经济分类
     *
     * @param bdExpeco 部门预算经济分类
     * @return 结果
     */
    public int insertBdExpeco(BdExpeco bdExpeco);

    /**
     * 批量新增部门预算经济分类
     *
     * @param bdExpecos 部门预算经济分类
     * @return 结果
     */
    public int insertBatchBdExpeco(List<BdExpeco> bdExpecos);

    /**
     * 修改部门预算经济分类
     *
     * @param bdExpeco 部门预算经济分类
     * @return 结果
     */
    public int updateBdExpeco(BdExpeco bdExpeco);

    /**
     * 批量修改部门预算经济分类
     *
     * @param bdExpecos 部门预算经济分类
     * @return 结果
     */
    public int updateBatchBdExpeco(List<BdExpeco> bdExpecos);

    /**
     * 删除部门预算经济分类
     *
     * @param expecoId 部门预算经济分类ID
     * @return 结果
     */
    public int deleteBdExpecoById(String expecoId);

    /**
     * 批量删除部门预算经济分类
     *
     * @param expecoIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteBdExpecoByIds(String[] expecoIds);

    /**
     * 查询部门预算经济分类tree
     *
     * @param bdExpeco
     * @return
     */
    List<Zone> selectAssetTypeListTree(BdExpeco bdExpeco);


    /**
     * 根据id查询
     *
     * @param expecoCodes
     * @return
     */
    List<BdExpeco> selectBdExpecoByIds(String[] expecoCodes);

    /**
     * 查询所有上级集合
     *
     * @param year
     * @param expecoCode
     * @return
     */
    List<BdExpeco> selectBdExpfuncUpList(@Param("year") Long year, @Param("expecoCode") String expecoCode);

    Integer existChildren(@Param("year") Long year, @Param("unitId") String unitId, @Param("expecoCode") String expecoCode);

    int checkCode(BdExpeco bdExpeco);

    List<BdExpeco> selectBdExpecoByCodes(@Param("list") List<String> codes, @Param("year") Long year, @Param("unitId") String unitId);

    /**
     * 删除数据
     *
     * @param unitId
     * @param year
     * @return
     */
    int deleteBdExpecoByYear(@Param("unitId") String unitId, @Param("year") Long year);

    /**
     * 数据最新年度
     *
     * @return 结果
     */
    int selectLatestYear();

    /**
     * 部门经济分类结转
     *
     * @param newYear
     * @param oldYear
     */
    void carryForwardData(@Param("newYear") Integer newYear, @Param("oldYear") Integer oldYear);
}
