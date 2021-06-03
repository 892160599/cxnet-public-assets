package com.cxnet.asset.dispose.domain.vo;

import com.cxnet.asset.annex.domain.AstAnnex;
import com.cxnet.asset.card.domain.AstBillList;
import com.cxnet.asset.dispose.domain.AstDisposeBill;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 资产处置VO
 *
 * @author zhaoyi
 * @since 2021-03-25 13:56:33
 */
@Data
public class AstDisposeVo implements Serializable {

    @ApiModelProperty("资产处置主表")
    private AstDisposeBill astDisposeBill;

    @ApiModelProperty("资产处置明细表")
    private List<AstBillList> astBillLists;

    @ApiModelProperty("资产业务附件表")
    private List<AstAnnex> astAnnexes;
}
