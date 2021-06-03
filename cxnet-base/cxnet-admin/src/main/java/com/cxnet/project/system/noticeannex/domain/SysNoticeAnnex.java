package com.cxnet.project.system.noticeannex.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.cxnet.common.utils.poi.annotation.Excel;
import com.cxnet.framework.web.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 通知公告附件表
 *
 * @author ssw
 * @since 2020-10-26 12:26:40
 */
@Data
@ApiModel(description = "通知公告附件表")
public class SysNoticeAnnex implements Serializable {
    private static final long serialVersionUID = 215586745712966474L;

    @ApiModelProperty("附件id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String annexId;

    @ApiModelProperty("系统ID")
    private String noticeId;

    @ApiModelProperty("文件id")
    private String fileId;

    @ApiModelProperty("附件类型（1：通知公告 2：操作指南 3 政策法规）")
    private String annexType;

    @ApiModelProperty("序号,标记")
    private String annexOrder;

    @ApiModelProperty("字典值集")
    private String dictValue;

    @ApiModelProperty("备注")
    private String bz;

    /**
     * 真实名称
     */
    @Excel(name = "真实名称")
    @ApiModelProperty("真实名称")
    @TableField(exist = false)
    private String realName;
    /**
     * 附件名称
     */
    @Excel(name = "附件名称")
    @ApiModelProperty("附件名称")
    @TableField(exist = false)
    private String fileName;
    /**
     * 创建人姓名
     */
    @Excel(name = "创建人姓名")
    @ApiModelProperty("创建人姓名")
    @TableField(exist = false)
    private String createName;

}