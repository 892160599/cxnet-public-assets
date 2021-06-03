package com.cxnet.baseData.expfunc.bdExpfunc.mapper;

import com.cxnet.baseData.expfunc.bdExpfunc.domain.BdExpfunc;
import com.cxnet.common.utils.tree.Zone;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 支出功能分类Mapper接口
 *
 * @author cxnet
 * @date 2020-08-17
 */
public interface BdExpfuncMapper {
    /**
     * 查询支出功能分类
     *
     * @param funcId 支出功能分类ID
     * @return 支出功能分类
     */
    public BdExpfunc selectBdExpfuncById(String funcId);

    /**
     * 查询支出功能分类集合
     *
     * @param bdExpfunc 支出功能分类
     * @return 支出功能分类集合
     */
    public List<BdExpfunc> selectBdExpfuncList(BdExpfunc bdExpfunc);

    /**
     * 查询支出功能分类tree
     *
     * @param bdExpfunc 支出功能分类
     * @return 支出功能分类tree
     */
    public List<Zone> selectBdExpfuncTree(BdExpfunc bdExpfunc);

    /**
     * 新增支出功能分类
     *
     * @param bdExpfunc 支出功能分类
     * @return 结果
     */
    public int insertBdExpfunc(BdExpfunc bdExpfunc);

    /**
     * 批量新增支出功能分类
     *
     * @param bdExpfuncs 支出功能分类
     * @return 结果
     */
    public int insertBatchBdExpfunc(List<BdExpfunc> bdExpfuncs);

    /**
     * 修改支出功能分类
     *
     * @param bdExpfunc 支出功能分类
     * @return 结果
     */
    public int updateBdExpfunc(BdExpfunc bdExpfunc);

    /**
     * 批量修改支出功能分类
     *
     * @param bdExpfuncs 支出功能分类
     * @return 结果
     */
    public int updateBatchBdExpfunc(List<BdExpfunc> bdExpfuncs);

    /**
     * 删除支出功能分类
     *
     * @param funcId 支出功能分类ID
     * @return 结果
     */
    public int deleteBdExpfuncById(String funcId);

    /**
     * 批量删除支出功能分类
     *
     * @param funcIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteExpfuncByIds(String[] funcIds);

    /**
     * 根据id查询
     *
     * @param funcCode
     * @return
     */
    List<BdExpfunc> selectBdExpfuncByIds(String[] funcCode);

    /**
     * 查询所有上级集合
     *
     * @param year
     * @param funcCode
     * @return
     */
    List<BdExpfunc> selectBdExpfuncUpList(@Param("year") Long year, @Param("funcCode") String funcCode);

    Integer existBdExpfunc(BdExpfunc bdExpfunc);

    /**
     * 查询下级是否存在
     *
     * @return
     */
    Integer existChildren(@Param("year") Long year, @Param("unitId") String unitId, @Param("funcCode") String funcCode);


    int checkCode(BdExpfunc bdExpfunc);

    List<BdExpfunc> selectBdExpfuncByCodes(@Param("list") List<String> codes, @Param("year") Long year, @Param("deptCode") String deptCode);


    int deleteBdExpfuncByYear(@Param("unitId") String unitId, @Param("year") Long year);

    /**
     * 支出功能分类结转年度计算
     *
     * @return 结果
     */
    int selectLatestYear();

    /**
     * 支出功能分类年度结转
     *
     * @param newYear 结转年度
     * @param oldYear 原年度
     */
    void carryForwardData(@Param("newYear") Integer newYear, @Param("oldYear") Integer oldYear);
}
