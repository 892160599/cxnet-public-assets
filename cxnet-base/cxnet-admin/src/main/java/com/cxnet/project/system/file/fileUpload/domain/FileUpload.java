package com.cxnet.project.system.file.fileUpload.domain;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.cxnet.common.utils.poi.annotation.Excel;
import com.cxnet.framework.web.domain.BaseEntity;

/**
 * 文件上传实体 sys_file_upload
 *
 * @author cxnet
 * @date 2020-07-24
 */
@Data
@ApiModel("文件上传")
public class FileUpload extends BaseEntity {

    /**
     * 文件ID
     */
    @ApiModelProperty("文件ID")
    private String fileId;
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
     * 创建人姓名
     */
    @Excel(name = "创建人姓名")
    @ApiModelProperty("创建人姓名")
    private String createName;

}
