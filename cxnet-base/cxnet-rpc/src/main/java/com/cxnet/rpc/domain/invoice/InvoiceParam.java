package com.cxnet.rpc.domain.invoice;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cxnet.rpc.domain.RpcBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 发票业务参数
 *
 * @author renqianhang
 * @since 2021-05-17 13:43:32
 */
@Data
@ApiModel(description = "发票业务参数")
public class InvoiceParam extends RpcBaseEntity implements Serializable {
    private static final long serialVersionUID = -46082937004569984L;

    @ApiModelProperty("id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("购买方名称")
    private String buyer;

    @ApiModelProperty("购买方纳税人识别号")
    private String buyerTaxid;

    @ApiModelProperty("手动录入发票文件是否必传 (0开启 2关闭)")
    private String isFileMust;

    @ApiModelProperty("手动录入,上传附件是否自动识别 (0开启 2关闭)")
    private String isAutoOcr;

    @ApiModelProperty("电子发票文件格式是否检验 (0开启 2关闭)")
    private String isFormatExamine;

    @ApiModelProperty("是否开启查验服务 (0开启 2关闭)")
    private String isOpenExamine;

    @ApiModelProperty("是否自动查验发票 (0开启 2关闭)")
    private String isAutoExamine;

    @ApiModelProperty("是否运行不同人员录入同一张发票 (0开启 2关闭)")
    private String isSameInvoice;

    @ApiModelProperty("机打号码,机打代码是否核对检验 (0开启 2关闭)")
    private String isCodeExamine;

    @ApiModelProperty("单位id")
    private String unitId;

    @ApiModelProperty("单位编码")
    private String unitCode;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("部门id")
    private String deptId;

    @ApiModelProperty("部门编码")
    private String deptCode;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("删除标志（0代表存在 2代表删除）")
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

}

