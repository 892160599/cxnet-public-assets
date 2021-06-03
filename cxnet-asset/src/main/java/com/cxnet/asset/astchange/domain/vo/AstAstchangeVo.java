package com.cxnet.asset.astchange.domain.vo;

import com.cxnet.asset.annex.domain.AstAnnex;
import com.cxnet.asset.astchange.domain.AstAstchangeBill;
import com.cxnet.asset.astchange.domain.AstAstchangeList;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 资产变动主VO
 *
 * @author zhaoyi
 * @since 2021-04-23 10:06:14
 */
@Data
public class AstAstchangeVo {

    @ApiModelProperty("资产变动主表")
    private AstAstchangeBill astAstchangeBill;

    @ApiModelProperty("资产变动明细表")
    private List<AstAstchangeList> astAstchangeLists;

    @ApiModelProperty("资产业务附件表")
    private List<AstAnnex> astAnnexes;
}
