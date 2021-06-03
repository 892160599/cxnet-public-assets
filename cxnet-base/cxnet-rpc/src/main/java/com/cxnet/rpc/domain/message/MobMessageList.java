package com.cxnet.rpc.domain.message;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cxnet.rpc.domain.RpcBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 消息子表
 *
 * @author zhangyl
 * @since 2021-05-21 14:01:48
 */
@Data
@ApiModel(description = "消息子表")
public class MobMessageList extends RpcBaseEntity implements Serializable {
    private static final long serialVersionUID = 945709821683992177L;

    @ApiModelProperty("$column.comment")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("消息id")
    private String messageId;

    @ApiModelProperty("是否已读  0 已读  2 未读")
    private String isRead;

}

