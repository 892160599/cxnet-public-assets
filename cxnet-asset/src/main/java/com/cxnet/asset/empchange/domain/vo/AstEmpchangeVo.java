package com.cxnet.asset.empchange.domain.vo;

import com.cxnet.asset.annex.domain.AstAnnex;
import com.cxnet.asset.empchange.domain.AstEmpchangeBill;
import com.cxnet.asset.empchange.domain.AstEmpchangeList;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 使用人变更VO
 *
 * @author zhaoyi
 * @since 2021-04-16 14:28:31
 */
@Data
public class AstEmpchangeVo {

    @ApiModelProperty("使用人变更主表")
    private AstEmpchangeBill astEmpchangeBill;

    @ApiModelProperty("使用人变更明细表")
    private List<AstEmpchangeList> astEmpchangeLists;

    @ApiModelProperty("资产业务附件表")
    private List<AstAnnex> astAnnexes;
}
