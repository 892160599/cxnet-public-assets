package com.cxnet.asset.card.domain.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.cxnet.framework.web.domain.BaseEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 卡片数据传输类
 *
 * @author
 */
@Data
public class AstCardDTO extends BaseEntity {

    @ApiModelProperty("卡片状态")
    private String astStatus;

    @ApiModelProperty("部门id")
    private String deptId;

    @ApiModelProperty("使用人id")
    private String empId;


    @ApiModelProperty("购置开始日期")
    private Date acquisitionStartDate;


    @ApiModelProperty("购置结束日期")
    private Date acquisitionEndDate;

    @ApiModelProperty("价值开始值")
    private String cosStart;

    @ApiModelProperty("价值结束值")
    private String cosEnd;

    @ApiModelProperty("资产名称")
    private String assetName;

    @ApiModelProperty("制单开始日期")
    private Date creatStartTime;


    @ApiModelProperty("制单结束日期")
    private Date creatEndTime;

    @ApiModelProperty("使用部门名称")
    private String departmentId;


    @ApiModelProperty("管理部门id")
    private String adminDepartmentId;


    @ApiModelProperty("存放地点id")
    private String placeId;

    @ApiModelProperty("单位id")
    private String unitId;

    @ApiModelProperty("卡片id数组")
    private List<String> ids;
}
