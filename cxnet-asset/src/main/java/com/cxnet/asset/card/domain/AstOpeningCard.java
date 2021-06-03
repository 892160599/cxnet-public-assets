package com.cxnet.asset.card.domain;

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
 * 期初卡片导入记录表
 *
 * @author zhangyl
 * @since 2021-04-22 16:29:01
 */
@Data
@ApiModel(description = "期初卡片导入记录表")
public class AstOpeningCard extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -76645213888017524L;

    @ApiModelProperty("主键id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("年度")
    private Integer fiscal;

    @ApiModelProperty("单位id")
    private String unitId;

    @ApiModelProperty("单位编码")
    private String unitCode;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("部门id")
    private String deptId;

    @ApiModelProperty("部门代码")
    private String deptCode;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("导入人编码")
    private String confirmedCode;

    @ApiModelProperty("导入人名称")
    private String confirmedName;

    @ApiModelProperty("导入日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date confirmedDate;

    @ApiModelProperty("是否导入成功  0 成功   2 失败")
    private String isSucess;

    @ApiModelProperty("导入进度")
    private String schedule;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("是否删除   0正常   2停用")
    private String delFlag;

    @ApiModelProperty("说明")
    private String remark;

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

