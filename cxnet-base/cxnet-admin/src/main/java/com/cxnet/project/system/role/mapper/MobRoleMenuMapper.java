package com.cxnet.project.system.role.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cxnet.project.system.role.domain.MobRoleMenu;

/**
 * 角色应用表(MobRoleMenu)表数据库访问层
 *
 * @author guks
 * @since 2021-05-18 17:04:44
 */
public interface MobRoleMenuMapper extends BaseMapper<MobRoleMenu> {

    List<MobRoleMenu> selectAll(MobRoleMenu mobRoleMenu);
}