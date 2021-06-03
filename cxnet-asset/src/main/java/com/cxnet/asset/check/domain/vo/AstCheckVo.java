package com.cxnet.asset.check.domain.vo;


import com.cxnet.asset.annex.domain.AstAnnex;
import com.cxnet.asset.check.domain.AstCheckBill;
import com.cxnet.asset.check.domain.AstCheckList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * 资产验收vo
 *
 * @author caixx
 * @since 2021-03-25 09:39:41
 */
@Data
@ApiModel(description = "资产验收vo")
public class AstCheckVo {

    @ApiModelProperty(value = "资产验收主表")
    private AstCheckBill astCheckBill;

    @ApiModelProperty(value = "资产验收子表")
    private List<AstCheckList> astCheckLists;

    @ApiModelProperty(value = "资产验收附件表")
    private List<AstAnnex> astAnnexes;

}