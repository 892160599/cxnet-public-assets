package com.cxnet.rpc.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author: rw
 * @create: 2020-10-26 10:30
 **/
@Data
@ApiModel("待办已办实体")
public class SysDbYb {
    @ApiModelProperty("单据id")
    private String primaryId;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty("各单据 主键id名称  查询用")
    private String itemId;
    @ApiModelProperty("申请金额")
    private String money;
    @ApiModelProperty("单位编码")
    private String unitCode;
    @ApiModelProperty("单位名称")
    private String unitName;
    @ApiModelProperty("部门编码")
    private String deptCode;
    @ApiModelProperty("部门名称")
    private String deptName;
    @ApiModelProperty("主表名")
    private String tableMain;
    @ApiModelProperty("实例id")
    private String processinstid;
    @ApiModelProperty("模块编码")
    private String modelCode;
    @ApiModelProperty("模块名称")
    private String modelName;
    @ApiModelProperty("路径")
    @TableField(exist = false)
    private String path;
    @ApiModelProperty("审批岗位")
    private String auditPost;
    @ApiModelProperty("创建人名称")
    private String createName;
    @ApiModelProperty("父级路径")
    private String parentPath;
    @TableField(exist = false)
    /*** 用户名*/
    @ApiModelProperty("用户名")
    private String nickName;
    /*** 单据号*/
    @ApiModelProperty("单据号")
    private String billNo;
    /*** 单据事由*/
    @ApiModelProperty("单据事由")
    private String billReason;
    /*** 单据状态*/
    @ApiModelProperty("单据状态")
    private String status;
    /*** 单位部门简称*/
    @ApiModelProperty("单位部门简称")
    @TableField(exist = false)
    private String unitDeptName;
    /*** 单据名称*/
    @ApiModelProperty("单据名称")
    private String billName;
    /*** 所有待办人*/
    @ApiModelProperty("所有待办人")
    private String assetName;
    /*** 跳转标识 3内控报销分摊 */
    @ApiModelProperty("跳转标识 3内控报销分摊")
    private String jumpSign;

}
