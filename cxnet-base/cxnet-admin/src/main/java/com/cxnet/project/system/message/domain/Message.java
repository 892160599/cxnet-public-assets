package com.cxnet.project.system.message.domain;

import lombok.Data;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.cxnet.common.utils.poi.annotation.Excel;
import com.cxnet.framework.web.domain.BaseEntity;

/**
 * 消息记录实体 sys_message
 *
 * @author cxnet
 * @date 2020-03-11
 */
@Data
@ApiModel("消息记录实体")
public class Message extends BaseEntity {

    /**
     * 编号
     */
    @ApiModelProperty("编号")
    private String messageId;
    /**
     * 内容
     */
    @Excel(name = "内容")
    @ApiModelProperty("内容")
    private String content;
    /**
     * 菜单地址
     */
    @Excel(name = "菜单地址")
    @ApiModelProperty("菜单地址")
    private String menuUrl;
    /**
     * 类型
     */
    @Excel(name = "类型")
    @ApiModelProperty("类型")
    private String type;
    /**
     * 状态
     */
    @Excel(name = "状态")
    @ApiModelProperty("状态")
    private String status;
    /**
     * 删除标志
     */
    @ApiModelProperty("删除标志")
    private String delFlag;

    @ApiModelProperty("备注")
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String toJSONString() {
        return JSON.toJSONString(this);
    }

}
