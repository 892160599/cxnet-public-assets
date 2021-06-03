package com.cxnet.baseData.assetType.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 基础资料附件表
 *
 * @author zhangyl
 * @since 2021-03-26 13:56:47
 */
@Data
@ApiModel(description = "基础资料附件表")
public class BdAnnex implements Serializable {
    private static final long serialVersionUID = 274340964337058483L;

    @ApiModelProperty("附件id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String annexId;

    @ApiModelProperty("单据id")
    private String astId;

    @ApiModelProperty("文件id")
    private String fileId;

    @ApiModelProperty("附件类型 0:资产验收")
    private String annexType;

    @ApiModelProperty("序号,标记")
    private Integer annexOrder;

    @ApiModelProperty("字典值集")
    private String dictValue;

    @ApiModelProperty("备注")
    private String bz;

    @ApiModelProperty("真实名字")
    private String realName;

    @ApiModelProperty("文件路径")
    private String fileName;

}

