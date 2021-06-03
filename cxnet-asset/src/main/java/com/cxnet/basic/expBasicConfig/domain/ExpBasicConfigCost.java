package com.cxnet.basic.expBasicConfig.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cxnet.framework.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 报销单据配置-费用明细
 *
 * @author caixx
 * @since 2020-09-12 13:18:35
 */
@Data
@ApiModel(description = "报销单据配置-费用明细")
public class ExpBasicConfigCost extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -91918156634131237L;

    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty("主键")
    private String id;
    @ApiModelProperty("单据编码")
    private String modelCode;
    @ApiModelProperty("费用明细编码")
    private String costCode;
    @ApiModelProperty("费用明细名称")
    private String costName;
    @ApiModelProperty("说明")
    private String explain;
    @ApiModelProperty("排序")
    private Long sort;
    @TableField(exist = false)
    private Object object;
    @ApiModelProperty("单位id")
    private String unitId;

}