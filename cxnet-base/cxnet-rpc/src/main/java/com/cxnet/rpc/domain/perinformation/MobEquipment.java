package com.cxnet.rpc.domain.perinformation;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cxnet.rpc.domain.RpcBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 设备绑定表
 *
 * @author zhangyl
 * @since 2021-05-14 10:18:08
 */
@Data
@ApiModel(description = "设备绑定表")
public class MobEquipment extends RpcBaseEntity implements Serializable {
    private static final long serialVersionUID = -59753669058283900L;

    @ApiModelProperty("主键")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("设备版本")
    private String deviceVersion;

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

    public MobEquipment() {
    }

    public MobEquipment(String userId, String deviceVersion) {
        this.userId = userId;
        this.deviceVersion = deviceVersion;
    }
}

