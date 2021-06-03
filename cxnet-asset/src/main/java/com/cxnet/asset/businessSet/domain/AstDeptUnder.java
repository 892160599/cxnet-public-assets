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
 * 资产管理员部门归口管理
 *
 * @author zhangyl
 * @since 2021-03-29 14:51:03
 */
@Data
@ApiModel(description = "资产管理员部门归口管理")
public class AstDeptUnder extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 810685356328942771L;

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

    @ApiModelProperty("部门编码")
    private String deptCode;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("类别编码")
    private String typeCode;

    @ApiModelProperty("类别名称")
    private String typeName;

    @ApiModelProperty("是否启用 0正常  2停用")
    private String status;

    @ApiModelProperty("删除标志 0正常  2停用")
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

