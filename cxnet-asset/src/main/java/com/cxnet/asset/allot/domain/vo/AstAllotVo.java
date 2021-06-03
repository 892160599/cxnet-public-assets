package com.cxnet.asset.allot.domain.vo;


import com.cxnet.asset.allocation.domain.AstAllocationBill;
import com.cxnet.asset.allot.domain.AstAllotBill;
import com.cxnet.asset.annex.domain.AstAnnex;
import com.cxnet.asset.card.domain.AstBillList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 资产调拨
 *
 * @author zhaoyi
 * @since 2021-04-09 14:33:01
 */
@Data
@ApiModel("资产调拨")
public class AstAllotVo {

    @ApiModelProperty("单位调拨主表")
    private AstAllotBill allotBill;

    @ApiModelProperty("单位调拨明细表")
    private List<AstBillList> astBillLists;

    @ApiModelProperty("资产业务附件表")
    private List<AstAnnex> astAnnexes;
}
