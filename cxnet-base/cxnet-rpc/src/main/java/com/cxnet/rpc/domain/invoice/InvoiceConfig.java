package com.cxnet.rpc.domain.invoice;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cxnet.rpc.domain.RpcBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 发票服务配置
 *
 * @author renwei
 * @since 2021-05-17 09:33:20
 */
@Data
@ApiModel(description = "发票服务配置")
public class InvoiceConfig extends RpcBaseEntity implements Serializable {
    private static final long serialVersionUID = -51989266640331065L;

    @ApiModelProperty("服务配置id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String serviceId;

    @ApiModelProperty("服务编码")
    private String serviceCode;

    @ApiModelProperty("服务名称")
    private String serviceName;

    @ApiModelProperty("请求方式")
    private String requestType;

    @ApiModelProperty("使用状态")
    private String status;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("apiID")
    private String apiId;

    @ApiModelProperty("api账号")
    private String apiKey;

    @ApiModelProperty("api密码")
    private String apiSecret;

    @ApiModelProperty("api接口地址")
    private String apiAddress;

    @ApiModelProperty("验真id")
    private String checkId;

    @ApiModelProperty("验真key")
    private String checkKey;

    @ApiModelProperty("验真密码")
    private String checkSecret;

    @ApiModelProperty("验真接口地址")
    private String checkAddress;

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

    @ApiModelProperty("删除标记")
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

    @ApiModelProperty("扩展字段6")
    private String extend6;

    @ApiModelProperty("扩展字段7")
    private String extend7;

    @ApiModelProperty("扩展字段8")
    private String extend8;

    @ApiModelProperty("扩展字段9")
    private String extend9;

    @ApiModelProperty("扩展字段10")
    private String extend10;

    @ApiModelProperty("识别接口总次数")
    private Integer apiTotalCount;

    @ApiModelProperty("识别接口使用次数")
    private Integer apiUsedCount;

    @ApiModelProperty("识别接口剩余次数")
    private Integer apiLeftCount;

    @ApiModelProperty("验真接口总次数")
    private Integer checkTotalCount;

    @ApiModelProperty("验真接口使用次数")
    private Integer checkUsedCount;

    @ApiModelProperty("验真接口剩余次数")
    private Integer checkLeftCount;

    @ApiModelProperty("发票文件路径")
    @TableField(exist = false)
    private String fileUrl;

    @ApiModelProperty("发票文件类型")
    @TableField(exist = false)
    private String fileType;

    @ApiModelProperty("发票文件名称")
    @TableField(exist = false)
    private String fileName;

    @ApiModelProperty("服务优先级")
    private String serviceLevel;

    @ApiModelProperty("识别token")
    private String apiToken;

    @ApiModelProperty("验真token")
    private String checkToken;

}