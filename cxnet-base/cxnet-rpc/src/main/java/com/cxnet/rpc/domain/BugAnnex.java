package com.cxnet.rpc.domain;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.cxnet.common.utils.poi.annotation.Excel;

/**
 * 采购附件关联实体 bug_annex
 *
 * @author cxnet
 * @date 2020-08-24
 */
@Data
@ApiModel("采购附件关联")
public class BugAnnex extends RpcBaseEntity {

    /**
     * 附件id
     */
    @ApiModelProperty("附件id")
    private String annexId;
    /**
     * 预算单据id
     */
    @Excel(name = "预算单据id")
    @ApiModelProperty("预算单据id")
    private String bugId;
    /**
     * 文件id
     */
    @Excel(name = "文件id")
    @ApiModelProperty("文件id")
    private String fileId;
    /**
     * 附件类型 0预算项目申报附件 1预算项目备案附件 2基建项目附件 3预算指标附件
     */
    @Excel(name = "附件类型 0预算项目申报附件 1预算项目备案附件 2基建项目附件 3预算指标附件")
    @ApiModelProperty("附件类型 0预算项目申报附件 1预算项目备案附件 2基建项目附件 3预算指标附件")
    private String annexType;
    /**
     * 序号,标记
     */
    @Excel(name = "序号,标记")
    @ApiModelProperty("序号,标记")
    private String annexOrder;
    /**
     * 真实名称
     */
    @Excel(name = "真实名称")
    @ApiModelProperty("真实名称")
    private String realName;
    /**
     * 附件名称
     */
    @Excel(name = "附件名称")
    @ApiModelProperty("附件名称")
    private String fileName;
    /**
     * 字典值集
     */
    @Excel(name = "字典值集")
    @ApiModelProperty("字典值集")
    private String dictValue;

    /**
     * 附件备注
     */
    @Excel(name = "附件备注")
    @ApiModelProperty("附件备注")
    private String bz;

    /**
     * 创建人姓名
     */
    @Excel(name = "创建人姓名")
    @ApiModelProperty("创建人姓名")
    private String createName;

}
