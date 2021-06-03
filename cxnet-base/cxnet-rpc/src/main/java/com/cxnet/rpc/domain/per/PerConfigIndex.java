package com.cxnet.rpc.domain.per;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 绩效固化指标配置
 *
 * @author caixx
 * @since 2021-05-20 15:55:40
 */
@Data
@ApiModel(description = "绩效固化指标配置")
public class PerConfigIndex implements Serializable {
    private static final long serialVersionUID = -28209649242889137L;

    @ApiModelProperty("主键")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("单据编码")
    private String modelCode;

    @ApiModelProperty("顺序")
    private Integer sort;

    @ApiModelProperty("单位id")
    private String unitId;

    @ApiModelProperty("指标编码")
    private String indexCode;

    @ApiModelProperty("指标名称")
    private String indexName;

    @ApiModelProperty("上级指标编码")
    private String parentCode;

    @ApiModelProperty("指标解释")
    private String explanation;

    @ApiModelProperty("评分标准")
    private String scoringCriteria;

    @ApiModelProperty("指标方向")
    private String direction;

    @ApiModelProperty("计量单位")
    private String unitOfMeasurement;

    @ApiModelProperty("目标值")
    private String targetValue;

    @ApiModelProperty("备注")
    private String remark;

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

    @ApiModelProperty("是否固化指标 0是 2否")
    private String isSolidified;

    @ApiModelProperty("上级指标名称")
    private String parentName;

    @ApiModelProperty("指标层级")
    private Integer indexLevel;

    @TableField(exist = false)
    private List<PerConfigIndex> children;

}