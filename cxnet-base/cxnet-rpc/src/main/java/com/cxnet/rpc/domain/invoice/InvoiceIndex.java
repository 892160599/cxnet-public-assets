package com.cxnet.rpc.domain.invoice;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cxnet.rpc.domain.RpcBaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 发票主表
 *
 * @author renwei
 * @since 2021-05-17 17:11:17
 */
@Data
@ApiModel(description = "发票主表")
public class InvoiceIndex extends RpcBaseEntity implements Serializable {
    private static final long serialVersionUID = -94230664067728096L;

    @ApiModelProperty("主键id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String invoiceId;

    @ApiModelProperty("发票类型")
    private String invoiceType;

    @ApiModelProperty("发票代码")
    private String invoiceCode;

    @ApiModelProperty("发票号码")
    private String invoiceNumber;

    @ApiModelProperty("发票日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date invoiceDate;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("发票状态")
    private String invoiceStatus;

    @ApiModelProperty("识别结果")
    private String OcrResult;

    @ApiModelProperty("发票属性")
    private String invoiceProperty;

    @ApiModelProperty("税前金额")
    private BigDecimal pretaxAmount;

    @ApiModelProperty("总额")
    private BigDecimal total;

    @ApiModelProperty("税额")
    private BigDecimal tax;

    @ApiModelProperty("税率")
    private String taxRate;

    @ApiModelProperty("校验码")
    private String checkCode;

    @ApiModelProperty("销售方")
    private String seller;

    @ApiModelProperty("销售方地址")
    private String sellerAddress;

    @ApiModelProperty("销售方开户行和账号")
    private String sellerBank;

    @ApiModelProperty("销售方电话")
    private String sellerPhone;

    @ApiModelProperty("销售方社会信用代码")
    private String sellerTaxId;

    @ApiModelProperty("购买方")
    private String buyer;

    @ApiModelProperty("购买方开户行和账号")
    private String buyerBank;

    @ApiModelProperty("购买方地址")
    private String buyerAddress;

    @ApiModelProperty("购买方电话")
    private String buyerPhone;

    @ApiModelProperty("购买方社会信用代码")
    private String buyerTaxId;

    @ApiModelProperty("车船税")
    private String carBoatTax;

    @ApiModelProperty("消费类型")
    private String consumeType;

    @ApiModelProperty("发票备注")
    private String remark;

    @ApiModelProperty("机打代码")
    private String machineCode;

    @ApiModelProperty("机打号码")
    private String machineNumber;

    @ApiModelProperty("通行费标志")
    private String tollRemark;

    @ApiModelProperty("成品油标志")
    private String oilRemark;

    @ApiModelProperty("高速标志")
    private String roadRemark;

    @ApiModelProperty("版式文件")
    private String formatFile;

    @ApiModelProperty("查验状态")
    private String checkStatus;

    @ApiModelProperty("查验次数")
    private String checkCount;

    @ApiModelProperty("查验时间")
    private Date checkTime;

    @ApiModelProperty("发票来源")
    private String invoiceOrigin;

    @ApiModelProperty("报销状态")
    private String expensesStatus;

    @ApiModelProperty("报销单据号")
    private String expBill;

    @ApiModelProperty("转让人")
    private String transferor;

    @ApiModelProperty("转让人姓名")
    private String transferorName;

    @ApiModelProperty("是否修改过")
    private String isUpdate;

    @ApiModelProperty("乘客姓名")
    private String passenger;

    @ApiModelProperty("乘客身份证号")
    private String passengerCard;

    @ApiModelProperty("乘车时间")
    private String passengerTime;

    @ApiModelProperty("出发站/入口")
    private String beginStation;

    @ApiModelProperty("到达站/出口")
    private String endStation;

    @ApiModelProperty("车次")
    private String trainNumber;

    @ApiModelProperty("座位类型")
    private String seatType;

    @ApiModelProperty("销售单位代号")
    private String sellerUnitCode;

    @ApiModelProperty("填开单位")
    private String tiankaiUnit;

    @ApiModelProperty("票价")
    private BigDecimal invoicePrice;

    @ApiModelProperty("燃油附加费")
    private BigDecimal oilCost;

    @ApiModelProperty("保险费")
    private BigDecimal premium;

    @ApiModelProperty("民航发展基金")
    private BigDecimal mhfzjj;

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

    @ApiModelProperty("删除标记")
    private String delFlag;

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

    @ApiModelProperty("扩展字段6")
    private String extend6;

    @ApiModelProperty("扩展字段7")
    private String extend7;

    @ApiModelProperty("扩展字段8")
    private String extend8;

    @ApiModelProperty("扩展字段9")
    private String extend9;

    @ApiModelProperty("扩展字段10")
    private String extend10;

    @ApiModelProperty("发票时间开始")
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date invoiceDateBegin;

    @ApiModelProperty("发票时间结束")
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date invoiceDateEnd;

    @ApiModelProperty("录入时间开始")
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTimeBegin;

    @ApiModelProperty("录入时间结束")
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTimeEnd;

    @ApiModelProperty("价税合计开始")
    @TableField(exist = false)
    private BigDecimal totalBegin;

    @ApiModelProperty("价税合计结束")
    @TableField(exist = false)
    private BigDecimal totalEnd;

    @ApiModelProperty("模糊查关键词")
    @TableField(exist = false)
    private String keyValue;

    @ApiModelProperty("是否连号")
    @TableField(exist = false)
    private String isLinkNumber;

    @ApiModelProperty("是否连号")
    @TableField(exist = false)
    private String bigTotal;

    @ApiModelProperty("是否识别")
    @TableField(exist = false)
    private String IsOcr;

    @ApiModelProperty("是否识别元素校验")
    @TableField(exist = false)
    private String IsOcrCheck;

    @ApiModelProperty("附件名称下载所用")
    @TableField(exist = false)
    private String fileName;

    @ApiModelProperty("识别错误代码")
    @TableField(exist = false)
    private String ocrErrorCode;

    @ApiModelProperty("发票附件子表")
    @TableField(exist = false)
    private InvoiceAnnex expAnnexe;
}