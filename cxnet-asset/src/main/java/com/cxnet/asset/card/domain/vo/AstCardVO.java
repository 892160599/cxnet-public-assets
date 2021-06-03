package com.cxnet.asset.card.domain.vo;

import java.io.Serializable;
import java.util.List;

import com.cxnet.asset.annex.domain.AstAnnex;
import com.cxnet.asset.astcardacsequip.domain.AstCardAcsequip;
import com.cxnet.asset.card.domain.AstCard;
import com.cxnet.framework.web.domain.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 资产显示
 *
 * @author
 * @since
 */
@Data
@ApiModel(description = "资产信息")
public class AstCardVO implements Serializable {

    /**
     * 序列号
     */
    private static final long serialVersionUID = -5445449054384734566L;

    @ApiModelProperty("主表信息")
    private AstCard astCard;

    @ApiModelProperty("附属设备")
    private List<AstCardAcsequip> astCardAcsequips;


    @ApiModelProperty("资产业务附件表")
    private List<AstAnnex> astAnnexes;


}
