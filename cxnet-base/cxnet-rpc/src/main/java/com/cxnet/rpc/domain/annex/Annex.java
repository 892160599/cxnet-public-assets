package com.cxnet.rpc.domain.annex;

import com.baomidou.mybatisplus.annotation.TableId;
import com.cxnet.common.utils.poi.annotation.Excel;
import com.cxnet.rpc.domain.RpcBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 合同附件实体 con_annex
 *
 * @author renwei
 * @date 2020-07-28
 */
@Data
@ApiModel("合同附件")
public class Annex extends RpcBaseEntity {

    /**
     * 附件id
     */
    @ApiModelProperty("附件id")
    @TableId
    private String annexId;
    /**
     * 合同id
     */
    @Excel(name = "合同id")
    @ApiModelProperty("合同id")
    private String conId;
    /**
     * 文件id
     */
    @Excel(name = "文件id")
    @ApiModelProperty("文件id")
    private String fileId;
    /**
     * 附件类型 0 合同模板附件 1 合同附件 2 备案附件 3执行中止附件
     */
    @Excel(name = "附件类型 0 合同模板附件 1 合同附件 2 备案附件 3执行中止附件")
    @ApiModelProperty("附件类型 0 合同模板附件 1 合同附件 2 备案附件 3执行中止附件")
    private String annexType;
    //系统附件表
    /**
     * 真实名称
     */
    @Excel(name = "真实名称")
    @ApiModelProperty("真实名称")
    private String realName;
    /**
     * 文件名称
     */
    @Excel(name = "文件名称")
    @ApiModelProperty("文件名称")
    private String fileName;
    /**
     * 文件大小
     */
    @Excel(name = "文件大小")
    @ApiModelProperty("文件大小")
    private Long fileSize;
    /**
     * 文件后缀
     */
    @Excel(name = "文件后缀")
    @ApiModelProperty("文件后缀")
    private String fileSuffix;
    /**
     * 访问路径
     */
    @Excel(name = "访问路径")
    @ApiModelProperty("访问路径")
    private String fileUrl;
    /**
     * 文件类型
     */
    @Excel(name = "文件类型")
    @ApiModelProperty("文件类型")
    private String fileType;
    /**
     * 下载次数
     */
    @Excel(name = "下载次数")
    @ApiModelProperty("下载次数")
    private Long downCount;

    /**
     * 附件类别
     */
    @Excel(name = "附件类别")
    @ApiModelProperty("附件类别")
    private String dictValue;

    /**
     * 备注
     */
    @Excel(name = "备注")
    @ApiModelProperty("备注")
    private String bz;
    /**
     * 创建人
     */
    @Excel(name = "创建人")
    @ApiModelProperty("创建人")
    private String create_by;
    /**
     * 创建时间
     */
    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    @Excel(name = "创建时间")
    @ApiModelProperty("创建时间")
    private String create_time;

}
