package com.cxnet.asset.astcollectbill.domain.vo;

import java.util.List;

import com.cxnet.asset.annex.domain.AstAnnex;
import com.cxnet.asset.astcollectbill.domain.AstCollectBill;
import com.cxnet.asset.card.domain.AstBillList;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 资产领用显示对象
 *
 * @author guks
 * @since 2021-04-15 11:58:24
 */
@Data
@ApiModel(description = "资产领用页面显示")
public class AstCollectBillVO {

    @ApiModelProperty(value = "资产领用主表")
    private AstCollectBill astCollectBill;

    @ApiModelProperty(value = "资产领用明细表")
    private List<AstBillList> astBillList;

    @ApiModelProperty(value = "资产领用附件表")
    private List<AstAnnex> astAnnexes;
}
