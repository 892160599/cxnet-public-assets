package com.cxnet.project.system.user.domain;

import com.alibaba.fastjson.JSON;
import com.cxnet.framework.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户和菜单关联实体 sys_user_menu
 *
 * @author Chanyin
 * @date 2020-04-14
 */
@Data
@ApiModel("用户和菜单关联")
public class SysUserMenu extends BaseEntity {

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private String userId;
    /**
     * 菜单ID
     */
    @ApiModelProperty("菜单ID")
    private String menuId;

    public String toJSONString() {
        return JSON.toJSONString(this);
    }

}
