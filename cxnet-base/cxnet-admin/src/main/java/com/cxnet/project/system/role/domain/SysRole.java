package com.cxnet.project.system.role.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.cxnet.common.utils.poi.annotation.Excel;
import com.cxnet.common.utils.poi.annotation.Excel.ColumnType;
import com.cxnet.framework.web.domain.BaseEntity;

/**
 * 角色表 sys_role
 *
 * @author cxnet
 */
@ApiModel("角色")
public class SysRole extends BaseEntity {

    /**
     * 角色ID
     */
    @ApiModelProperty("角色ID")
    @Excel(name = "角色ID", cellType = ColumnType.NUMERIC)
    private String roleId;

    /**
     * 角色名称
     */
    @ApiModelProperty("角色名称")
    @Excel(name = "角色名称")
    private String roleName;

    /**
     * 角色权限
     */
    @ApiModelProperty("角色权限")
    @Excel(name = "角色权限")
    private String roleKey;

    /**
     * 角色排序
     */
    @ApiModelProperty("角色排序")
    @Excel(name = "角色排序")
    private String roleSort;

    /**
     * 数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限）
     */
    @ApiModelProperty("数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限）")
    @Excel(name = "数据范围", readConverterExp = "1=所有数据权限,2=自定义数据权限,3=本部门数据权限,4=本部门及以下数据权限")
    private String dataScope;

    /**
     * 角色状态（0正常 1停用）
     */
    @ApiModelProperty("角色状态")
    @Excel(name = "角色状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @ApiModelProperty("删除标志（0代表存在 2代表删除）")
    private String delFlag;

    /**
     * 用户是否存在此角色标识 默认不存在
     */
    private boolean flag = false;

    /**
     * 菜单组
     */
    private String[] menuIds;

    /**
     * APP应用组
     */
    private String[] appMenuIds;

    /**
     * 部门组（数据权限）
     */
    private String[] deptIds;

    /**
     * 菜单id
     */
    private String menuId;

    /**
     * 是否系统内置角色(0否 1是)
     */
    private String isFinal;

    /**
     * 是否权限控制
     */
    private String isDataScope;

    @ApiModelProperty("备注")
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public SysRole() {

    }

    public SysRole(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public boolean isAdmin() {
        return isAdmin(this.roleId);
    }

    public static boolean isAdmin(String roleId) {
        return roleId != null && "4".equals(roleId);
    }

    //    @NotBlank(message = "角色名称不能为空")
    @Size(min = 0, max = 30, message = "角色名称长度不能超过30个字符")
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    //    @NotBlank(message = "权限字符不能为空")
    @Size(min = 0, max = 100, message = "权限字符长度不能超过100个字符")
    public String getRoleKey() {
        return roleKey;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }

    //    @NotBlank(message = "显示顺序不能为空")
    public String getRoleSort() {
        return roleSort;
    }

    public void setRoleSort(String roleSort) {
        this.roleSort = roleSort;
    }

    @Override
    public String getDataScope() {
        return dataScope;
    }

    @Override
    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
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

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String[] getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(String[] menuIds) {
        this.menuIds = menuIds;
    }

    public String[] getDeptIds() {
        return deptIds;
    }

    public void setDeptIds(String[] deptIds) {
        this.deptIds = deptIds;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }


    public String[] getAppMenuIds() {
        return appMenuIds;
    }

    public void setAppMenuIds(String[] appMenuIds) {
        this.appMenuIds = appMenuIds;
    }

    public String getIsFinal() {
        return isFinal;
    }

    public void setIsFinal(String isFinal) {
        this.isFinal = isFinal;
    }

    public String getIsDataScope() {
        return isDataScope;
    }

    public void setIsDataScope(String isDataScope) {
        this.isDataScope = isDataScope;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("roleId", getRoleId())
                .append("roleName", getRoleName())
                .append("roleKey", getRoleKey())
                .append("roleSort", getRoleSort())
                .append("dataScope", getDataScope())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
