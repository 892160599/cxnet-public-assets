package com.cxnet.framework.web.domain;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity基类
 *
 * @author cxnet
 */
@Data
@ApiModel("mybatisPlus基础实体")
public class MpBaseEntity implements Serializable {

    /**
     * 搜索值
     */
    @ApiModelProperty("搜索值")
    @TableField(exist = false)
    private String searchValue;

    /**
     * 数据权限
     */
    @ApiModelProperty("数据权限")
    @TableField(exist = false)
    private String dataScope;

    /**
     * 请求参数
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ApiModelProperty("请求参数")
    @TableField(exist = false)
    private String params;

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
