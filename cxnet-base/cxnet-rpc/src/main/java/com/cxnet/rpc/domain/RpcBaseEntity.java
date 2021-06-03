package com.cxnet.rpc.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Entity基类
 *
 * @author cxnet
 */
@Data
@ApiModel("基础实体")
public class RpcBaseEntity implements Serializable {

    /**
     * 搜索值
     */
    @ApiModelProperty("搜索值")
    @TableField(exist = false)
    public String searchValue;

    /**
     * 创建者
     */
    @ApiModelProperty("创建者")
    @TableField(fill = FieldFill.INSERT)
    public String createBy;

    /**
     * 创建者名称
     */
    @ApiModelProperty("创建者名称")
    @TableField(fill = FieldFill.INSERT)
    public String createName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    public Date createTime;

    /**
     * 更新者
     */
    @ApiModelProperty("更新者")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    public String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    public Date updateTime;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    @TableField(exist = false)
    public String remark;

    /**
     * 数据权限
     */
    @ApiModelProperty("数据权限")
    @TableField(exist = false)
    public String dataScope;

    /**
     * 请求参数
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ApiModelProperty("请求参数")
    @TableField(exist = false)
    public String params;

    @SuppressWarnings("unchecked")
    public Map<String, Object> getParams() {
        if (params == null) {
            return new HashMap<>();
        }
        return JSON.parseObject(params, Map.class);
    }

    public void setParams(String params) {
        this.params = params;
    }
}
