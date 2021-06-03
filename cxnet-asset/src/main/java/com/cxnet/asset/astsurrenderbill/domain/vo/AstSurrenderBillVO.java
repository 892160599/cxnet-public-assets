package com.cxnet.asset.astsurrenderbill.domain.vo;

import java.util.List;

import com.cxnet.asset.annex.domain.AstAnnex;
import com.cxnet.asset.astsurrenderbill.domain.AstSurrenderBill;
import com.cxnet.asset.card.domain.AstBillList;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 资产交回
 *
 * @author guks
 */
@Data
@ApiModel(description = "资产交回页面显示")
public class AstSurrenderBillVO {

    @ApiModelProperty(value = "资产交回主表")
    private AstSurrenderBill astSurrenderBill;

    @ApiModelProperty(value = "资产领用明细表")
    private List<AstBillList> astBillList;

    @ApiModelProperty(value = "资产领用附件表")
    private List<AstAnnex> astAnnexes;


}
