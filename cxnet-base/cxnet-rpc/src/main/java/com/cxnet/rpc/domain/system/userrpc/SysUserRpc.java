package com.cxnet.rpc.domain.system.userrpc;

import com.alibaba.fastjson.JSON;
import com.cxnet.common.utils.poi.annotation.Excel;
import com.cxnet.common.utils.poi.annotation.Excels;
import com.cxnet.rpc.domain.RpcBaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

public class SysUserRpc extends RpcBaseEntity {

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    @Excel(name = "用户ID")
    private String userId;

    /**
     * 单位ID
     */
    @ApiModelProperty("单位ID")
    private String deptId;

    /**
     * 单位编码
     */
    @ApiModelProperty("单位编码")
    private String deptCode;

    /**
     * 用户账号
     */
    @ApiModelProperty("用户账号")
    @Excel(name = "用户账号")
    private String userName;

    /**
     * 用户昵称
     */
    @ApiModelProperty("用户昵称")
    @Excel(name = "用户名称")
    private String nickName;

    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    @Excel(name = "邮箱")
    private String email;

    /**
     * 手机号码
     */
    @ApiModelProperty("手机号码")
    @Excel(name = "手机号码")
    private String phonenumber;

    /**
     * 用户性别
     */
    @ApiModelProperty("用户性别")
    @Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    private String sex;

    /**
     * 用户头像
     */
    @ApiModelProperty("用户头像")
    private String avatar;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;

    /**
     * 盐加密
     */
    @ApiModelProperty("盐加密")
    private String salt;

    /**
     * 用户类型
     */
    @ApiModelProperty("用户类型")
    private String userType;

    /**
     * 帐号状态（0正常 1停用）
     */
    @ApiModelProperty("帐号状态（0正常 1停用）")
    @Excel(name = "帐号状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 登录方式（1普通登录 2CA登录 3手机号登录）
     */
    @ApiModelProperty("登录方式（1普通登录 2CA登录 3手机号登录）")
    private String loginMode;

    /**
     * CA序列号
     */
    @ApiModelProperty("CA序列号")
    private String caSerialNumber;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @ApiModelProperty("删除标志（0代表存在 2代表删除）")
    private String delFlag;

    /**
     * 最后登陆IP
     */
    @ApiModelProperty("最后登陆IP")
    @Excel(name = "最后登陆IP")
    private String loginIp;

    /**
     * 最后登陆时间
     */
    @ApiModelProperty("最后登陆时间")
    @Excel(name = "最后登陆时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date loginDate;

    /**
     * 密码失效时间
     */
    @ApiModelProperty("密码失效时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validity;

    /*    *//**
     * 部门对象
     *//*
    @ApiModelProperty("部门对象")
    @Excels({@Excel(name = "部门名称", targetAttr = "deptName"), @Excel(name = "部门负责人", targetAttr = "leader")})
    private SysDept dept;

    *//**
     * 角色对象
     *//*
    @ApiModelProperty("角色对象")
    private List<SysRoleRpc> roles;

    *//**
     * 菜单对象
     *//*
    @ApiModelProperty("菜单对象")
    private List<SysMenu> menus;*/

    /**
     * 角色组
     */
    @ApiModelProperty("角色组")
    private String[] roleIds;

    /**
     * 岗位组
     */
    @ApiModelProperty("岗位组")
    private String[] postIds;

    /**
     * 菜单组
     */
    @ApiModelProperty("菜单组")
    private String[] menuIds;

    @ApiModelProperty("备注")
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isAdmin() {
        return isAdmin(this.userId);
    }

    public static boolean isAdmin(String userId) {
        return userId != null && "2".equals(userId);
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    @Size(min = 0, max = 30, message = "用户昵称长度不能超过30个字符")
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @NotBlank(message = "用户账号不能为空")
    @Size(min = 0, max = 30, message = "用户账号长度不能超过30个字符")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

/*    public SysDept getDept() {
        return dept;
    }

    public void setDept(SysDept dept) {
        this.dept = dept;
    }

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }*/

    public String[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String[] roleIds) {
        this.roleIds = roleIds;
    }

    public String[] getPostIds() {
        return postIds;
    }

    public void setPostIds(String[] postIds) {
        this.postIds = postIds;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
