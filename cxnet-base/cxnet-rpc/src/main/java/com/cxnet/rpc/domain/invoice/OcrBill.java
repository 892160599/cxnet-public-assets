package com.cxnet.rpc.domain.invoice;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cxnet.common.utils.poi.annotation.Excel;
import com.cxnet.rpc.domain.RpcBaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ocr识别信息表
 *
 * @author renwei
 * @since 2021-04-09 16:49:08
 */
@Data
@ApiModel(description = "ocr识别信息表")
public class OcrBill extends RpcBaseEntity implements Serializable {
    private static final long serialVersionUID = 125443127279013092L;

    @ApiModelProperty("主键id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String billId;

    @ApiModelProperty("发票类型")
    private String billType;

    @ApiModelProperty("发票类型名称")
    private String billTypeName;

    @ApiModelProperty("发票代码")
    private String billCode;

    @ApiModelProperty("发票号码")
    private String billNumber;

    @ApiModelProperty("开票日期")
    @Excel(name = "开票日期", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date billDate;

    @ApiModelProperty("税前金额")
    private BigDecimal pretaxAmount;

    @ApiModelProperty("总金额")
    private BigDecimal total;

    @ApiModelProperty("税额")
    private BigDecimal tax;

    @ApiModelProperty("校验码")
    private String checkCode;

    @ApiModelProperty("销售方名称")
    private String seller;

    @ApiModelProperty("销售方纳税人识别号")
    private String sellerTaxId;

    @ApiModelProperty("购买方方名称")
    private String buyer;

    @ApiModelProperty("购买方纳税人识别号")
    private String buyerTaxId;

    @ApiModelProperty("发票状态")
    private String billStatus;

    @ApiModelProperty("保存日期")
    @Excel(name = "保存日期", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date saveDate;

    @ApiModelProperty("报销年度")
    private String fiscal;

    @ApiModelProperty("单位id")
    private String unitId;

    @ApiModelProperty("单位编码")
    private String unitCode;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("部门id")
    private String deptId;

    @ApiModelProperty("部门代码")
    private String deptCode;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("扩展字段1")
    private String expExtend1;

    @ApiModelProperty("扩展字段2")
    private String expExtend2;

    @ApiModelProperty("扩展字段3")
    private String expExtend3;

    @ApiModelProperty("扩展字段4")
    private String expExtend4;

    @ApiModelProperty("扩展字段5")
    private String expExtend5;

    @ApiModelProperty("扩展字段6")
    private String expExtend6;

    @ApiModelProperty("扩展字段7")
    private String expExtend7;

    @ApiModelProperty("扩展字段8")
    private String expExtend8;

    @ApiModelProperty("扩展字段9")
    private String expExtend9;

    @ApiModelProperty("扩展字段10")
    private String expExtend10;

}