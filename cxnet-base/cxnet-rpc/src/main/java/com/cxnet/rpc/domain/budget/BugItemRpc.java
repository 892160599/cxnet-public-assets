package com.cxnet.rpc.domain.budget;

import com.cxnet.common.jsr303group.UpdateGroup;
import com.cxnet.common.utils.poi.annotation.Excel;
import com.cxnet.rpc.domain.RpcBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * 预算项目主实体 bug_item
 *
 * @author renwei
 * @date 2020-08-24
 */
@Data
@ApiModel("预算项目主表")
public class BugItemRpc extends RpcBaseEntity {

    /**
     * 项目id
     */
    @Excel(name = "项目id")
    @ApiModelProperty("项目id")
    @NotBlank(message = "预算项目编号不能为空", groups = {UpdateGroup.class})
    private String projectId;
    /**
     * 项目单据号
     */
    @Excel(name = "项目单据号")
    @ApiModelProperty("项目单据号")
    private String projectBill;
    /**
     * 项目编码
     */
    @Excel(name = "项目编码")
    @ApiModelProperty("项目编码")
    private String projectCode;
    /**
     * 项目名称
     */
    @Excel(name = "项目名称")
    @ApiModelProperty("项目名称")
    private String projectName;
    /**
     * 项目来源
     */
    @Excel(name = "项目来源")
    @ApiModelProperty("项目来源")
    private String projectSource;
    /**
     * 上级项目编码
     */
    @Excel(name = "上级项目编码")
    @ApiModelProperty("上级项目编码")
    private String parentProjectCode;
    /**
     * 上级项目名称
     */
    @Excel(name = "上级项目名称")
    @ApiModelProperty("上级项目名称")
    private String parentProjectName;
    /**
     * 项目分类
     */
    @Excel(name = "项目分类")
    @ApiModelProperty("项目分类")
    private String projectSort;
    /**
     * 项目属性
     */
    @Excel(name = "项目属性")
    @ApiModelProperty("项目属性")
    private String projectProperty;
    /**
     * 开始时间
     */
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("开始时间")
    private String startTime;
    /**
     * 结束时间
     */
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("结束时间")
    private String endTime;
    /**
     * 项目类型
     */
    @Excel(name = "项目类型")
    @ApiModelProperty("项目类型")
    private String projectType;
    /**
     * 项目目录
     */
    @Excel(name = "项目目录")
    @ApiModelProperty("项目目录")
    private String projectList;
    /**
     * 责任部门编码
     */
    @Excel(name = "责任部门编码")
    @ApiModelProperty("责任部门编码")
    private String dutyDepartCode;
    /**
     * 责任部门名称
     */
    @Excel(name = "责任部门名称")
    @ApiModelProperty("责任部门名称")
    private String dutyDepartName;
    /**
     * 项目负责人
     */
    @Excel(name = "项目负责人")
    @ApiModelProperty("项目负责人")
    private String dutyPerson;
    /**
     * 负责人电话
     */
    @Excel(name = "负责人电话")
    @ApiModelProperty("负责人电话")
    private String dutyTelephone;
    /**
     * 项目总投资
     */
    @Excel(name = "项目总投资")
    @ApiModelProperty("项目总投资")
    private Long projectTotalMoney;
    /**
     * 项目建设内容
     */
    @Excel(name = "项目建设内容")
    @ApiModelProperty("项目建设内容")
    private String constructContent;
    /**
     * 部门Id
     */
    @Excel(name = "部门Id")
    @ApiModelProperty("部门Id")
    private String deptId;
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
    private String deptName;

    /**
     * 单位Id
     */
    @Excel(name = "单位Id")
    @ApiModelProperty("单位Id")
    private String unitId;
    /**
     * 单位编码
     */
    @Excel(name = "单位编码")
    @ApiModelProperty("单位编码")
    private String unitCode;
    /**
     * 单位名称
     */
    @Excel(name = "单位名称")
    @ApiModelProperty("单位名称")
    private String unitName;
    /**
     * 项目状态
     */
    @Excel(name = "项目状态")
    @ApiModelProperty("项目状态")
    private String projectStatus;
    /**
     * 流程id
     */
    @Excel(name = "流程id")
    @ApiModelProperty("流程id")
    private String processinstid;
    /**
     * 待审批岗位
     */
    @Excel(name = "待审批岗位")
    @ApiModelProperty("待审批岗位")
    private String approvalPost;
    /**
     * 审核日期
     */
    @Excel(name = "审核日期", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("审核日期")
    private Date auditDate;
    /**
     * 审核意见
     */
    @Excel(name = "审核意见")
    @ApiModelProperty("审核意见")
    private String auditOpinion;
    /**
     * 岗位状态
     */
    @Excel(name = "岗位状态")
    @ApiModelProperty("岗位状态")
    private String postStatus;
    /**
     * 是否政府项目
     */
    @Excel(name = "是否政府项目")
    @ApiModelProperty("是否政府项目")
    private String isGover;
    /**
     * 是否基建项目
     */
    @Excel(name = "是否基建项目")
    @ApiModelProperty("是否基建项目")
    private String isCapitalConst;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @ApiModelProperty("删除标志（0代表存在 2代表删除）")
    private String delFlag;
    /**
     * 项目年度
     */
    @Excel(name = "项目年度")
    @ApiModelProperty("项目年度")
    private String projectYear;
    /**
     * 台账状态
     */
    @Excel(name = "台账状态")
    @ApiModelProperty("台账状态")
    private String ledgerStatus;

    private List<String> piis;
    /**
     * 创建人姓名
     */
    @Excel(name = "创建人姓名")
    @ApiModelProperty("创建人姓名")
    private String createName;

    /**
     * 项目年度
     */
    @Excel(name = "项目年度")
    @ApiModelProperty("项目年度")
    private String fiscal;

}
