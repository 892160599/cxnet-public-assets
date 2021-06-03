package com.cxnet.asset.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cxnet.asset.inventory.domain.AstInventoryBill;
import com.cxnet.asset.inventory.domain.vo.AstInventoryListVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * (AstInventoryBill)表数据库访问层
 *
 * @author zhangyl
 * @since 2021-04-02 10:02:38
 */
public interface AstInventoryBillMapper extends BaseMapper<AstInventoryBill> {

    List<AstInventoryBill> selectAll(AstInventoryBill astInventoryBill);

    int insertList(AstInventoryListVo astInventoryListVo);

    @Select("SELECT BILL_NO,CHECK_PLAN_NAME,STATUS,PLAN_START_DATE,PLAN_END_DATE from AST_INVENTORY_BILL where BILL_NO=#{billNo} and  DEL_FLAG='0'")
    AstInventoryBill getBillOne(@Param("billNo") String billNo);

}

