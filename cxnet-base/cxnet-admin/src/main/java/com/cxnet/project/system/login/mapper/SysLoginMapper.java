package com.cxnet.project.system.login.mapper;

import com.cxnet.project.system.menu.domain.SysMenu;
import com.cxnet.project.system.user.domain.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 菜单表 数据层
 *
 * @author cxnet
 */
public interface SysLoginMapper {

    /**
     * 根据用户账号查询用户信息
     *
     * @param userName 用户账号
     * @return 用户信息
     */
    @Select("SELECT * FROM SYS_USER WHERE USER_NAME = #{userName}")
    public SysUser selectUserInfoByUserName(@Param("userName") String userName);


}
