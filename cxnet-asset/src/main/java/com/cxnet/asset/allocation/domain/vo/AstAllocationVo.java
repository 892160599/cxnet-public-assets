package com.cxnet.asset.allocation.domain.vo;

import com.cxnet.asset.allocation.domain.AstAllocationBill;
import com.cxnet.asset.annex.domain.AstAnnex;
import com.cxnet.asset.card.domain.AstBillList;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 部门调剂VO
 *
 * @author zhaoyi
 * @since 2021-04-02 09:55:49
 */
@Data
public class AstAllocationVo {

    @ApiModelProperty("部门调剂主表")
    private AstAllocationBill astAllocationBill;

    @ApiModelProperty("部门调剂明细表")
    private List<AstBillList> astBillLists;

    @ApiModelProperty("资产业务附件表")
    private List<AstAnnex> astAnnexes;
}
