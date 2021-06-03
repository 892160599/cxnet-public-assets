package com.cxnet.asset.asttransferbill.domain.vo;

import java.util.List;

import com.cxnet.asset.annex.domain.AstAnnex;
import com.cxnet.asset.asttransferbill.domain.AstTransferBill;
import com.cxnet.asset.card.domain.AstBillList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 资产移交显示对象
 *
 * @author guks
 * @since 2021-04-15 11:58:24
 */
@Data
@ApiModel(description = "资产移交页面显示")
public class AstTransferBillVO {

    @ApiModelProperty(value = "资产移交主表")
    private AstTransferBill astTransferBill;

    @ApiModelProperty(value = "资产移交明细表")
    private List<AstBillList> astBillList;

    @ApiModelProperty(value = "资产移交附件表")
    private List<AstAnnex> astAnnexes;
}