package com.cxnet.asset.businessHandling.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.cxnet.framework.web.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 资产业务办理
 *
 * @author caixx
 * @since 2021-03-31 16:04:18
 */
@Data
@ApiModel(description = "资产业务办理")
public class VAstBusinessHandling implements Serializable {
    private static final long serialVersionUID = 845021149132419293L;

    @ApiModelProperty("主键id")
    private String id;

    @ApiModelProperty("单据编码")
    private String billCode;

    @ApiModelProperty("总金额")
    private BigDecimal totalAmt;

    @ApiModelProperty("总数")
    private Integer totalNum;

    @ApiModelProperty("单据状态")
    private String status;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("流程id")
    private String processinstid;

    @ApiModelProperty("待审批岗位")
    private String approvalPost;

    @ApiModelProperty("单据类型编码")
    private String modelCode;

    @ApiModelProperty("单位id")
    private String unitId;

    @ApiModelProperty("单位编码")
    private String unitCode;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("制单人")
    private String createBy;

    @ApiModelProperty("制单人名称")
    public String createName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    public Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("更新时间")
    public Date updateTime;

    @TableField(exist = false)
    @ApiModelProperty("开始时间")
    private Date startTime;

    @TableField(exist = false)
    @ApiModelProperty("结束时间")
    private Date endTime;

    @TableField(exist = false)
    private List<String> piids;


}