package com.cxnet.project.system.dept.domain;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableField;
import com.cxnet.common.utils.poi.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.cxnet.framework.web.domain.BaseEntity;

/**
 * 部门表 sys_dept
 *
 * @author cxnet
 */
@Data
@ApiModel("部门信息")
public class SysDept extends BaseEntity {
    /**
     * 部门ID
     */
    @ApiModelProperty("部门ID")
    private String deptId;

    /**
     * 父部门ID
     */
    @ApiModelProperty("父部门ID")
    private String parentId;

    /**
     * 部门编码
     */
    @Excel(name = "部门编码")
    @ApiModelProperty("部门编码")
    private String deptCode;

    /**
     * 父部门编码
     */
    @Excel(name = "父部门编码")
    @ApiModelProperty("父部门编码")
    private String parentCode;

    /**
     * 祖级列表
     */
    @ApiModelProperty("祖级列表")
    private String ancestors;

    /**
     * 部门名称
     */
    @Excel(name = "部门名称")
    @ApiModelProperty("部门名称")
    private String deptName;

    /**
     * 部门全称
     */
    @Excel(name = "部门全称")
    @ApiModelProperty("部门全称")
    private String fullName;

    /**
     * 部门类型
     */
    @Excel(name = "部门类型")
    @ApiModelProperty("部门类型")
    private String deptType;

    /**
     * 单位类型
     */
    @Excel(name = "单位类型")
    @ApiModelProperty("单位类型")
    private String unitType;

    /**
     * 显示顺序
     */
    @ApiModelProperty("显示顺序")
    private String orderNum;

    /**
     * 负责人
     */
    @Excel(name = "负责人")
    @ApiModelProperty("负责人")
    private String leader;

    /**
     * 联系电话
     */
    @Excel(name = "联系电话")
    @ApiModelProperty("联系电话")
    private String phone;

    /**
     * 邮箱
     */
    @Excel(name = "邮箱")
    @ApiModelProperty("邮箱")
    private String email;

    /**
     * 传真
     */
    @Excel(name = "传真")
    @ApiModelProperty("传真")
    private String fax;
    /**
     * 描述
     */
    @Excel(name = "描述")
    @ApiModelProperty("描述")
    private String description;

    /**
     * 部门状态:0正常,1停用
     */
    @ApiModelProperty("部门状态:0正常,1停用")
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @ApiModelProperty("删除标志（0代表存在 2代表删除）")
    private String delFlag;

    /**
     * 父部门名称
     */
    @ApiModelProperty("父部门名称")
    @TableField(exist = false)
    private String parentName;

    /**
     * 法人代表
     */
    @Excel(name = "法人代表")
    @ApiModelProperty("法人代表")
    private String legalPerson;
    /**
     * 业务科室名称
     */
    @Excel(name = "业务科室名称")
    @ApiModelProperty("业务科室名称")
    private String businessName;
    /**
     * 财务负责人
     */
    @Excel(name = "财务负责人")
    @ApiModelProperty("财务负责人")
    private String financeLeader;
    /**
     * 区划码
     */
    @Excel(name = "区划码")
    @ApiModelProperty("区划码")
    private String zoningCode;
    /**
     * 统一社会信用代码
     */
    @Excel(name = "统一社会信用代码")
    @ApiModelProperty("统一社会信用代码")
    private String creditCode;
    /**
     * 单位地址
     */
    @Excel(name = "单位地址")
    @ApiModelProperty("单位地址")
    private String unitAdress;
    /**
     * 单位性质
     */
    @Excel(name = "单位性质")
    @ApiModelProperty("单位性质")
    private String unitQuality;
    /**
     * 档次名称
     */
    @Excel(name = "档次名称")
    @ApiModelProperty("档次名称")
    private String gradeName;
    /**
     * 是否预算单位
     */
    @Excel(name = "是否预算单位")
    @ApiModelProperty("是否预算单位")
    private String isYsunit;
    /**
     * 预算管理级次
     */
    @Excel(name = "预算管理级次")
    @ApiModelProperty("预算管理级次")
    private String budgeLevel;
    /**
     * 单位行业类型
     */
    @Excel(name = "单位行业类型")
    @ApiModelProperty("单位行业类型")
    private String unitTrade;
    /**
     * 政府职能
     */
    @Excel(name = "政府职能")
    @ApiModelProperty("政府职能")
    private String goverFunction;

    @ApiModelProperty("所属单位id")
    private String affilUnitId;

    @Excel(name = "所属单位编码")
    @ApiModelProperty("所属单位编码")
    private String affilUnitCode;

    @ApiModelProperty("所属单位名称")
    private String affilUnitName;

    @ApiModelProperty("上级部门id")
    private String parentDeptId;

    @ApiModelProperty("上级部门编码")
    private String parentDeptCode;

    @ApiModelProperty("上级部门名称")
    private String parentDeptName;

    @ApiModelProperty("部门负责人用户id")
    private String deptLeadId;

    @ApiModelProperty("部门负责人用户代码")
    private String deptLeadCode;

    @ApiModelProperty("部门负责人用户名称")
    private String deptLeadName;

    @ApiModelProperty("分管领导用户id")
    private String fgLeadId;

    @ApiModelProperty("分管领导用户代码")
    private String fgLeadCode;

    @ApiModelProperty("分管领导用户名称")
    private String fgLeadName;

    @ApiModelProperty("所属财政单位代码")
    private String financialCode;

    @ApiModelProperty("所属财政单位名称")
    private String financialName;

    @ApiModelProperty("所属财政单位id")
    private String financialId;

    @ApiModelProperty("预算单位名称")
    private String agencyCode;

    @ApiModelProperty("是否为公务之家单位")
    private String isReim;
    /**
     * 子部门
     */
    @TableField(exist = false)
    private List<SysDept> children = new ArrayList<SysDept>();

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getAncestors() {
        return ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    @NotBlank(message = "部门名称不能为空")
    @Size(min = 0, max = 30, message = "部门名称长度不能超过30个字符")
    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @NotBlank(message = "显示顺序不能为空")
    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getFinanceLeader() {
        return financeLeader;
    }

    public void setFinanceLeader(String financeLeader) {
        this.financeLeader = financeLeader;
    }

    public String getZoningCode() {
        return zoningCode;
    }

    public void setZoningCode(String zoningCode) {
        this.zoningCode = zoningCode;
    }

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }

    public String getUnitAdress() {
        return unitAdress;
    }

    public void setUnitAdress(String unitAdress) {
        this.unitAdress = unitAdress;
    }

    public String getUnitQuality() {
        return unitQuality;
    }

    public void setUnitQuality(String unitQuality) {
        this.unitQuality = unitQuality;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getIsYsunit() {
        return isYsunit;
    }

    public void setIsYsunit(String isYsunit) {
        this.isYsunit = isYsunit;
    }

    public String getBudgeLevel() {
        return budgeLevel;
    }

    public void setBudgeLevel(String budgeLevel) {
        this.budgeLevel = budgeLevel;
    }

    public String getUnitTrade() {
        return unitTrade;
    }

    public void setUnitTrade(String unitTrade) {
        this.unitTrade = unitTrade;
    }

    public String getGoverFunction() {
        return goverFunction;
    }

    public void setGoverFunction(String goverFunction) {
        this.goverFunction = goverFunction;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    @Size(min = 0, max = 11, message = "联系电话长度不能超过11个字符")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<SysDept> getChildren() {
        return children;
    }

    public void setChildren(List<SysDept> children) {
        this.children = children;
    }

    public String getDeptLeadId() {
        return deptLeadId;
    }

    public void setDeptLeadId(String deptLeadId) {
        this.deptLeadId = deptLeadId;
    }

    public String getDeptLeadCode() {
        return deptLeadCode;
    }

    public void setDeptLeadCode(String deptLeadCode) {
        this.deptLeadCode = deptLeadCode;
    }

    public String getDeptLeadName() {
        return deptLeadName;
    }

    public void setDeptLeadName(String deptLeadName) {
        this.deptLeadName = deptLeadName;
    }

    public String getFgLeadId() {
        return fgLeadId;
    }

    public void setFgLeadId(String fgLeadId) {
        this.fgLeadId = fgLeadId;
    }

    public String getFgLeadCode() {
        return fgLeadCode;
    }

    public void setFgLeadCode(String fgLeadCode) {
        this.fgLeadCode = fgLeadCode;
    }

    public String getFgLeadName() {
        return fgLeadName;
    }

    public void setFgLeadName(String fgLeadName) {
        this.fgLeadName = fgLeadName;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
