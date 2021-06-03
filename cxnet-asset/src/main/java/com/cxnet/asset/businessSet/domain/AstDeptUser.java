package com.cxnet.asset.businessSet.domain;

import java.util.Date;

import com.cxnet.framework.web.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangyl
 * @since 2021-03-29 10:39:24
 */
@Data
@ApiModel(description = "")
public class AstDeptUser extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 819568900217726851L;

    @ApiModelProperty("主键")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("年度")
    private String fiscal;

    @ApiModelProperty("单位id")
    private String unitId;

    @ApiModelProperty("单位编码")
    private String unitCode;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("人员编码")
    private String userCode;

    @ApiModelProperty("人员名称")
    private String userName;

    @ApiModelProperty("部门id")
    private String deptId;

    @ApiModelProperty("部门编码")
    private String deptCode;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("是否为部门管理员")
    private String isDeptAdmin;

    @ApiModelProperty("是否单位管理员")
    private String isUnitAdmin;

    @ApiModelProperty("0正常     2停用")
    private String status;

    @ApiModelProperty("0正常     2停用")
    private String delFlag;

    @ApiModelProperty("扩展字段1")
    private String extend1;

    @ApiModelProperty("扩展字段2")
    private String extend2;

    @ApiModelProperty("扩展字段3")
    private String extend3;

    @ApiModelProperty("扩展字段4")
    private String extend4;

    @ApiModelProperty("扩展字段5")
    private String extend5;

}

