package com.cxnet.project.businesslog.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 业务日志表
 *
 * @author guks
 * @since 2021-04-21 18:01:48
 */
@Data
@ApiModel(description = "业务日志表")
public class SysBusinessLog implements Serializable {
    private static final long serialVersionUID = -93972840820479687L;

    @ApiModelProperty("主键")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("模块名称")
    private String module;

    @ApiModelProperty("操作内容")
    private String content;

    @ApiModelProperty("业务类型（0=其它,1=新增,2=修改,3=删除,4=授权,5=导出,6=导入,7=强退,8=生成代码,9=清空数据,10=第三方推送,11=确认）")
    private Integer businessType;

    @ApiModelProperty("操作人类别")
    private Integer operatorType;

    @ApiModelProperty("方法名称")
    private String method;

    @ApiModelProperty("请求方式")
    private String requestMethod;

    @ApiModelProperty("请求URL")
    private String operUrl;

    @ApiModelProperty("操作地点")
    private String operLocation;

    @ApiModelProperty("操作地址")
    private String operIp;

    @ApiModelProperty("请求参数")
    private Object operParam;

    @ApiModelProperty("返回参数")
    private Object jsonResult;

    @ApiModelProperty("操作状态（0正常 1异常）")
    private Integer status;

    @ApiModelProperty("错误消息")
    private Object errorMsg;

    @ApiModelProperty("唯一标识")
    private String operateKey;

    @ApiModelProperty("标识字段名称")
    private String fieldName;

    @ApiModelProperty("操作人id")
    private String operUserId;

    @ApiModelProperty("操作人名称")
    private String operUserName;

    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date operTime;


}