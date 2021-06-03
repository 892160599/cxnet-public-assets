package com.cxnet.rpc.domain.message;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cxnet.rpc.domain.RpcBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 消息表
 *
 * @author zhangyl
 * @since 2021-05-21 14:01:15
 */
@Data
@ApiModel(description = "消息表")
public class MobMessageBill extends RpcBaseEntity implements Serializable {
    private static final long serialVersionUID = -33566947133843683L;

    @ApiModelProperty("主键id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("单据id")
    private String astId;

    @ApiModelProperty("提交人")
    private String submitter;

    @ApiModelProperty("单据名称")
    private String billName;

    @ApiModelProperty("单据号")
    private String billCode;

    @ApiModelProperty("消息类型")
    private String messageType;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("内容")
    private String body;

    @TableField(exist = false)
    private String listId;

    @TableField(exist = false)
    private String userId;

    @TableField(exist = false)
    private String IsRead;
}

