package com.cxnet.asset.astrepairbill.domain.vo;

import java.util.List;

import com.cxnet.asset.annex.domain.AstAnnex;
import com.cxnet.asset.astrepairbill.domain.AstRepairBill;
import com.cxnet.asset.card.domain.AstBillList;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 资产报修显示对象
 *
 * @author guks
 * @since
 */
@Data
@ApiModel(description = "资产报修显示对象")
public class AstRepairBillVO {

    @ApiModelProperty(value = "资产报修主表")
    private AstRepairBill astRepairBill;

    @ApiModelProperty(value = "资产领用明细表")
    private List<AstBillList> astBillList;

    @ApiModelProperty(value = "资产领用附件表")
    private List<AstAnnex> astAnnexes;
}