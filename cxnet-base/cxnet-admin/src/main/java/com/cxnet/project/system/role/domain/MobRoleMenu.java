package com.cxnet.project.system.role.domain;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 角色应用表
 *
 * @author makejava
 * @since 2021-05-18 17:04:45
 */
@Data
@ApiModel(description = "角色应用表")
public class MobRoleMenu implements Serializable {
    private static final long serialVersionUID = 924783531543419928L;

    @ApiModelProperty("主键")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("角色ID")
    private String roleId;

    @ApiModelProperty("菜单ID")
    private String menuId;

    @ApiModelProperty("数据范围")
    private String dataScope;

}