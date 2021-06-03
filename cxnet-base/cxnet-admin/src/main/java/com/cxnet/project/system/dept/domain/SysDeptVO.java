package com.cxnet.project.system.dept.domain;

import com.cxnet.common.utils.poi.annotation.Excel;
import com.cxnet.framework.web.domain.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 部门表 sys_dept
 *
 * @author cxnet
 */
@Data
@ApiModel("部门信息")
public class SysDeptVO extends BaseEntity {

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


}
