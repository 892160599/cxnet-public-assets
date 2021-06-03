package com.cxnet.asset.inventory.domain.vo;

import com.cxnet.asset.annex.domain.AstAnnex;
import com.cxnet.asset.card.domain.AstBillList;
import com.cxnet.asset.inventory.domain.AstSurplusBill;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zhangyl
 * @since 2021-04-19 14:14:44
 */

@Data
public class AstSurplusBillVo {

    @ApiModelProperty(value = "资产盘盈主表")
    private AstSurplusBill astSurplusBill;

    @ApiModelProperty(value = "资产盘盈明细表")
    private List<AstBillList> astBillList;

    @ApiModelProperty(value = "附件表")
    private List<AstAnnex> astAnnexList;
}
