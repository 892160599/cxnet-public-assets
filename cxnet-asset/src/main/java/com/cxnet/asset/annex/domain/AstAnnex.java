package com.cxnet.asset.annex.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 资产附件关联表
 *
 * @author makejava
 * @since 2021-03-25 10:55:12
 */
@Data
@ApiModel(description = "资产附件关联表")
public class AstAnnex implements Serializable {
    private static final long serialVersionUID = 294456411379353683L;

    @ApiModelProperty("附件id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String annexId;

    @ApiModelProperty("单据id")
    private String astId;

    @ApiModelProperty("文件id")
    private String fileId;

    @ApiModelProperty("附件类型 0:资产验收 2:资产盘点")
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

    @ApiModelProperty("创建时间")
    @TableField(exist = false)
    private Date createTime;

    @ApiModelProperty("上传人")
    @TableField(exist = false)
    private String createName;

}

