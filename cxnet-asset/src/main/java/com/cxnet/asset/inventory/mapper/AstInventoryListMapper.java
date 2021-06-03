package com.cxnet.asset.inventory.mapper;

import com.cxnet.asset.inventory.domain.AstInventoryList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cxnet.rpc.domain.basedata.BdPersonnel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 资产盘点子表(AstInventoryList)表数据库访问层
 *
 * @author zhangyl
 * @since 2021-04-06 09:34:12
 */
public interface AstInventoryListMapper extends BaseMapper<AstInventoryList> {

    List<AstInventoryList> selectAll(AstInventoryList astInventoryList);

    @Select("SELECT APPLY_DEPT_CODE,APPLY_DEPT_NAME,COUNT(DISTINCT EMP_CODE) DEPT_COUNT,\n" +
            "count(DISTINCT decode(IS_CHECK,'1',EMP_CODE,null)) STOCKED_COUNT, \n" +
            "count(DISTINCT decode(IS_CHECK,'2',EMP_CODE,null)) COUNTED\n" +
            "from AST_INVENTORY_LIST where BILL_NO=#{billNo} and APPLY_DEPT_CODE is not NULL\n" +
            "GROUP BY APPLY_DEPT_CODE,APPLY_DEPT_NAME")
    List<AstInventoryList> selectDept(@Param("billNo") String billNo);

    @Select("SELECT nvl(COUNT(1),0) from AST_INVENTORY_LIST where BILL_NO=#{billNo}")
    int selectCount(@Param("billNo") String billNo);

    @Select("SELECT nvl(SUM(COST),0) COST from AST_INVENTORY_LIST where BILL_NO=#{billNo}")
    int selSum(@Param("billNo") String billNo);

    @Select("SELECT nvl(SUM(QTY),0) from AST_INVENTORY_LIST where BILL_NO=#{billNo} and CLASS_CODE=#{classCode}")
    int selectCountBill(@Param("billNo") String billNo, @Param("classCode") String classCode);

    @Select("SELECT nvl(SUM(COST),0) COST from AST_INVENTORY_LIST where BILL_NO=#{billNo} and CLASS_CODE=#{classCode}")
    int selSumBill(@Param("billNo") String billNo, @Param("classCode") String classCode);

    int insertAstIntory(List<AstInventoryList> astInventoryLists);
}

