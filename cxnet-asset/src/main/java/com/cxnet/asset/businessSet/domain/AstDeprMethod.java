package com.cxnet.asset.businessSet.domain;

import java.util.Date;

import com.cxnet.framework.web.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 资产折旧方法表
 *
 * @author zhangyl
 * @since 2021-03-25 10:00:11
 */
@Data
@ApiModel(description = "资产折旧方法表")
public class AstDeprMethod extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 266854498890971329L;

    @ApiModelProperty("主键")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("折旧方法编码")
    private String deprMethodCode;

    @ApiModelProperty("折旧方法名称")
    private String deprMethodName;

    @ApiModelProperty("折旧公式")
    private String deprValueFormula;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("状态（2:停用  0:正常）")
    private String status;

    @ApiModelProperty("单位id ")
    private String unitId;

    @ApiModelProperty("删除标志")
    private String delFlag;

    @ApiModelProperty("扩展字段1")
    private String extend1;

    @ApiModelProperty("扩展字段2")
    private String extend2;

    @ApiModelProperty("扩展字段3")
    private String extend3;

    @ApiModelProperty("扩展字段4")
    private String extend4;

    @ApiModelProperty("扩展字段5")
    private String extend5;

}

