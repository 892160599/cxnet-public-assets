package com.cxnet.project.system.user.mapper;

import java.util.List;
import java.util.Set;

import cn.hutool.core.date.DateTime;
import com.cxnet.rpc.domain.system.userrpc.SysUserRpc;
import org.apache.ibatis.annotations.Param;

import com.cxnet.project.system.user.domain.SysUser;
import org.apache.ibatis.annotations.Update;

/**
 * 用户表 数据层
 *
 * @author cxnet
 */
public interface SysUserMapper {
    /**
     * 根据条件分页查询用户列表
     *
     * @param sysUser 用户信息
     * @return 用户信息集合信息
     */
    public List<SysUser> selectUserList(SysUser sysUser);

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    public SysUser selectUserByUserName(String userName);

    /**
     * 通过手机号查询用户
     *
     * @param phone 手机号
     * @return
     */
    SysUser selectUserByPhone(String phone);

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    SysUserRpc selectUserRpcByName(String userName);

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    public SysUser selectUserById(String userId);

    /**
     * 新增用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    public int insertUser(SysUser user);

    /**
     * 新增用户岗位信息
     *
     * @param user 用户信息
     * @return 结果
     */
    public SysUser insertUserAndPost(SysUser user);

    /**
     * 批量新增用户信息
     *
     * @param sysUsers 用户信息
     * @return 结果
     */
    public int insertBatchSysUser(List<SysUser> sysUsers);

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    public int updateUser(SysUser user);

    /**
     * 修改用户头像
     *
     * @param userName 用户名
     * @param avatar   头像地址
     * @return 结果
     */
    public int updateUserAvatar(@Param("userName") String userName, @Param("avatar") String avatar);

    /**
     * 重置用户密码
     *
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    public int resetUserPwd(@Param("userName") String userName, @Param("password") String password);

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteUserById(String userId);

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    public int deleteUserByIds(String[] userIds);

    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    public int checkUserNameUnique(String userName);

    /**
     * 校验手机号码是否唯一
     *
     * @param phonenumber 手机号码
     * @return 结果
     */
    public SysUser checkPhoneUnique(String phonenumber);

    /**
     * 校验email是否唯一
     *
     * @param email 用户邮箱
     * @return 结果
     */
    public SysUser checkEmailUnique(String email);

    /**
     * 根据单位部门筛选人员
     *
     * @param deptCode 用户邮箱
     * @return 结果
     */
    public List<SysUser> selectUserByDept(String deptCode);

    /**
     * 通过角色ID查询用户集合
     *
     * @param roleId 角色ID
     * @return 用户集合
     */
    public List<SysUser> selectUserListByRoleId(String roleId);

    /**
     * 通过岗位ID查询用户集合
     *
     * @param postId 角色ID
     * @return 用户集合
     */
    public List<SysUser> selectUserListByPostId(String postId);

    /**
     * 根据用户ID查组织机构
     *
     * @param userId 用户ID
     * @return 组织机构代码
     */
    public String getDeptPermission(String userId);

    /**
     * 修改所在单位
     *
     * @param unitId 单位id
     */
    public int updateDeptId(@Param("unitId") String unitId, @Param("userId") String userId);


    /**
     * 锁定用户
     *
     * @param userName
     * @return
     */
    @Update("update sys_user set status = 3 where user_name = #{userName}")
    public int lockUser(@Param("userName") String userName);

    public int updateUserValidity(DateTime dateTime);

    /**
     * 更新用户信息
     *
     * @param user 信息
     * @return 结果
     */
    int updateUserInfo(SysUser user);

    List<SysUser> selectBdUserList(SysUser user);
}
