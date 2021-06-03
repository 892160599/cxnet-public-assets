package com.cxnet.asset.inventory.domain.vo;

import com.cxnet.asset.annex.domain.AstAnnex;
import com.cxnet.asset.inventory.domain.AstInventoryBill;
import com.cxnet.asset.inventory.domain.AstInventoryList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zhangyl
 * @since 2021-04-19 14:14:44
 */

@Data
@ApiModel(description = "资产盘点vo")
public class AstInventoryBillVo {
    @ApiModelProperty(value = "资产盘点主表")
    private AstInventoryBill astInventoryBill;

    @ApiModelProperty(value = "资产盘点明细表")
    private List<AstInventoryList> astInventoryList;

    @ApiModelProperty(value = "附件表")
    private List<AstAnnex> astAnnexList;
}
