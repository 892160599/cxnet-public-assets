package com.cxnet.rpc.domain.expense;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cxnet.rpc.domain.RpcBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 报销控制规则
 *
 * @author caixx
 * @since 2020-09-14 14:22:34
 */
@Data
@ApiModel(description = "报销控制规则")
public class ExpBasicControl extends RpcBaseEntity implements Serializable {
    private static final long serialVersionUID = 557819481719295466L;

    @ApiModelProperty("ID")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("控制规则编码")
    private String code;

    @ApiModelProperty("值")
    private String value;

    @ApiModelProperty("控制规则名称")
    private String name;

    @ApiModelProperty("控制规则类型（1:基础设置 2:流程设置）")
    private Integer type;

    @ApiModelProperty("说明")
    private String explain;

    @ApiModelProperty("顺序")
    private Integer orders;

}