package com.cxnet.project.system.version.domain;

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
 * 系统版本控制
 *
 * @author caixx
 * @since 2020-12-25 18:10:26
 */
@Data
@ApiModel(description = "系统版本控制")
public class SysModelVersion implements Serializable {
    private static final long serialVersionUID = -69045631277834646L;

    @ApiModelProperty("系统名称")
    private String systemName;
    @ApiModelProperty("模块名称")
    private String modelName;
    @ApiModelProperty("系统版本")
    private String version;
    @ApiModelProperty("是否启用 1启用 0不启用")
    private String isUse;
    @ApiModelProperty("创建时间")
    private Date createTime;
}