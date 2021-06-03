package com.cxnet.rpc.domain.basedata;

import com.baomidou.mybatisplus.annotation.TableField;
import com.cxnet.common.jsr303group.UpdateGroup;
import com.cxnet.common.utils.poi.annotation.Excel;
import com.cxnet.rpc.domain.RpcBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
public class BdPersonnel extends RpcBaseEntity {
    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    @NotBlank(message = "部门人员编号不能为空", groups = {UpdateGroup.class})
    private String personnelId;
    /**
     * 人员编码
     */
    @Excel(name = "人员编码")
    @ApiModelProperty("人员编码")
    private String userCode;
    /**
     * 人员名称
     */
    @Excel(name = "人员名称")
    @ApiModelProperty("人员名称")
    private String userName;
    /**
     * 部门编码
     */
    @Excel(name = "部门编码")
    @ApiModelProperty("部门编码")
    private String deptCode;
    /**
     * 部门名称
     */
    @Excel(name = "部门名称")
    @ApiModelProperty("部门名称")
    @TableField(exist = false)
    private String deptName;
    /**
     * 部门ID
     */
    @Excel(name = "部门ID")
    @ApiModelProperty("部门ID")
    private String deptId;
    /**
     * 电话
     */
    @Excel(name = "电话")
    @ApiModelProperty("电话")
    private String phone;
    /**
     * 性别（1 男 2 女）
     */
    @Size(min = 0, max = 1, message = "性别长度不能超过1个字符")
    @Excel(name = "性别", readConverterExp = "1=,男=,2=,女=")
    @ApiModelProperty("性别（1 男 2 女）")
    private String gender;
    /**
     * 民族
     */
    @Excel(name = "民族")
    @ApiModelProperty("民族")
    private String nation;
    /**
     * 证件类型(1 居民身份证 2外国人居留证 3护照)
     */
    @Size(min = 0, max = 1, message = "证件类型长度不能超过1个字符")
    @Excel(name = "证件类型(1 居民身份证 2外国人居留证 3护照)")
    @ApiModelProperty("证件类型(1 居民身份证 2外国人居留证 3护照)")
    private String certificate;
    /**
     * 证件号码
     */
    @Excel(name = "证件号码")
    @ApiModelProperty("证件号码")
    private String idNumber;
    /**
     * 出生日期
     */
    @Excel(name = "出生日期", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("出生日期")
    private Date birthday;
    /**
     * 入职日期
     */
    @Excel(name = "入职日期", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("入职日期")
    private Date entryTime;
    /**
     * 职务级别
     */
    @Size(min = 0, max = 1, message = "职务级别长度不能超过1个字符")
    @Excel(name = "职务级别（1省级 2厅级）")
    @ApiModelProperty("职务级别（1省级 2厅级）")
    private String dutyLevel;
    /**
     * 费用控制级别
     */
    @Size(min = 0, max = 1, message = "费用控制级别长度不能超过1个字符")
    @Excel(name = "费用控制级别（1省厅 2厅级 3其他人员）")
    @ApiModelProperty("费用控制级别（1省厅 2厅级 3其他人员）")
    private String costControlLevel;
    /**
     * 人员账号
     */
    @Excel(name = "人员账号")
    @ApiModelProperty("人员账号")
    private String userId;
    /**
     * 开户行
     */
    @Excel(name = "开户行")
    @ApiModelProperty("开户行")
    private String openingBank;
    /**
     * 银行账号
     */
    @Excel(name = "银行账号")
    @ApiModelProperty("银行账号")
    private String bankAccount;
    /**
     * 人员排序号
     */
    @Excel(name = "人员排序号")
    @ApiModelProperty("人员排序号")
    private Long userSort;
    /**
     * 状态（1 启用 2禁用）
     */
    @Size(min = 0, max = 1, message = "状态长度不能超过1个字符")
    @Excel(name = "状态", readConverterExp = "0=,启=用,2=禁用")
    @ApiModelProperty("状态0 启用 2禁用）")
    private String status;
    /**
     * 删除标记（0存在 2已删除）
     */
    @Size(min = 0, max = 1, message = "删除标记长度不能超过1个字符")
    @Excel(name = "删除标记", readConverterExp = "0 存在 2 已删除")
    @ApiModelProperty("删除标记（0存在 2已删除）")
    private String delFlag;
    /**
     * 部门父级编码
     */
    @Excel(name = "单位ID")
    @ApiModelProperty("单位Id")
    @NotBlank(message = "单位编号不能为空")
    private String unitId;
    /**
     * 用户登录名
     */
    @Excel(name = "用户登录名")
    @ApiModelProperty("用户登录名")
    @TableField(exist = false)
    private String userNumber;
    /**
     * 用户昵称
     */
    @Excel(name = "用户昵称")
    @ApiModelProperty("用户昵称")
    @TableField(exist = false)
    private String userNickName;
    /**
     * 是否是新增（1：新增 2：引入）
     */
    @Excel(name = "是否是新增（1：新增 2：引入）")
    @ApiModelProperty("是否是新增（1：新增 2：引入）")
    @TableField(exist = false)
    private String createOrImp;
    /**
     * 部门集合
     */
    @Excel(name = "部门集合")
    @ApiModelProperty("部门集合")
    @TableField(exist = false)
    private List<String> deptIds;
    /**
     * 来源部门,是引用的话显示默认部门，时新增的不显示
     */
    @Excel(name = "来源部门")
    @ApiModelProperty("来源部门")
    @TableField(exist = false)
    private String sourceDept;

    @ApiModelProperty("公务卡卡号")
    private String businessNumber;

    @ApiModelProperty("备注")
    private String remark;
}
