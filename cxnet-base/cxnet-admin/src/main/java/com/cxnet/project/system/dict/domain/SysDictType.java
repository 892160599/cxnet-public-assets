package com.cxnet.project.system.dict.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.cxnet.common.utils.poi.annotation.Excel;
import com.cxnet.common.utils.poi.annotation.Excel.ColumnType;
import com.cxnet.framework.web.domain.BaseEntity;

import java.util.List;

/**
 * 字典类型表 sys_dict_type
 *
 * @author cxnet
 */
@ApiModel("字典类型")
public class SysDictType extends BaseEntity {

    /**
     * 字典主键
     */
    @ApiModelProperty("字典主键")
    @Excel(name = "字典主键", cellType = ColumnType.NUMERIC)
    private String dictId;

    /**
     * 字典名称
     */
    @ApiModelProperty("字典名称")
    @Excel(name = "字典名称")
    private String dictName;

    /**
     * 字典类型
     */
    @ApiModelProperty("字典类型")
    @Excel(name = "字典类型")
    private String dictType;

    /**
     * 状态（0正常 1停用）
     */
    @ApiModelProperty("状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 是否默认（Y是 N否）
     */
    @ApiModelProperty("是否默认（Y是 N否）")
    @Excel(name = "是否默认（Y是 N否）")
    private String isDefault;

    private List<String> ids;

    @ApiModelProperty("备注")
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public String getDictId() {
        return dictId;
    }

    public void setDictId(String dictId) {
        this.dictId = dictId;
    }

    //    @NotBlank(message = "字典名称不能为空")
    @Size(min = 0, max = 100, message = "字典类型名称长度不能超过100个字符")
    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    //    @NotBlank(message = "字典类型不能为空")
    @Size(min = 0, max = 100, message = "字典类型类型长度不能超过100个字符")
    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("dictId", getDictId())
                .append("dictName", getDictName())
                .append("dictType", getDictType())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .append("isDefault", getIsDefault())
                .append("ids", getIds())
                .toString();
    }
}
