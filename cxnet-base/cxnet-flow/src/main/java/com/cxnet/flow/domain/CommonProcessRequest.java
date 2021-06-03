package com.cxnet.flow.domain;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

/**
 * 流程提交参数对象
 *
 * @author caixx
 */
@Data
@ApiModel(value = "CommonProcessRequest-流程提交参数对象")
public class CommonProcessRequest {

    @ApiModelProperty(value = "单据id")
    private String[] id;

    @ApiModelProperty(value = "报销单据id")
    private List<String> expId;
    @ApiModelProperty(value = "报销单据类型编码")
    private String billTypeCode;

    @ApiModelProperty(value = "审批意见")
    private String opinion;

    @ApiModelProperty(value = "说明")
    private String description;

    @ApiModelProperty(value = "手写签批(图片id)")
    private String writeSign;

    @ApiModelProperty(value = "1:送审 2:审核 3:退回 4:收回")
    private String submitFlag;

    @ApiModelProperty(value = "移动端 批量通过时使用,单据id作为key，单据类型作为value")
    private Map<String, String> batchConsent;
	/*// 任务id
	private List<String> taskId;

	// 实例id
	private List<String> ProcessInstanceId;*/


}
