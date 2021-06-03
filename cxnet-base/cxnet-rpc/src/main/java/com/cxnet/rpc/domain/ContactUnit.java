package com.cxnet.rpc.domain;

import com.cxnet.common.utils.poi.annotation.Excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 系统往来单位实体 sys_contact_unit
 *
 * @author renwei
 * @date 2020-08-17
 */
@Data
@ApiModel("系统往来单位")
public class ContactUnit extends RpcBaseEntity {

    /**
     * 主体单位id
     */
    @ApiModelProperty("主体单位id")
    private String unitId;
    /**
     * 所属单位代码
     */
    @Excel(name = "所属单位代码")
    @ApiModelProperty("所属单位代码")
    private String departCode;
    /**
     * 所属单位名称
     */
    @Excel(name = "所属单位名称")
    @ApiModelProperty("所属单位名称")
    private String departName;
    /**
     * 往来单位编码
     */
    @Excel(name = "往来单位编码")
    @ApiModelProperty("往来单位编码")
    private String unitCode;
    /**
     * 往来单位名称
     */
    @Excel(name = "往来单位名称")
    @ApiModelProperty("往来单位名称")
    private String unitName;
    /**
     * 上级往来单位
     */
    @Excel(name = "上级往来单位")
    @ApiModelProperty("上级往来单位")
    private String parentId;
    /**
     * 社会统一信用码
     */
    @Excel(name = "社会统一信用码")
    @ApiModelProperty("社会统一信用码")
    private String identyNumber;
    /**
     * 联系人
     */
    @Excel(name = "联系人")
    @ApiModelProperty("联系人")
    private String linker;
    /**
     * 联系电话
     */
    @Excel(name = "联系电话")
    @ApiModelProperty("联系电话")
    private String linkTel;
    /**
     * 联系地址
     */
    @Excel(name = "联系地址")
    @ApiModelProperty("联系地址")
    private String linkAdr;
    /**
     * 是否使用 0 正常 2 停用
     */
    @ApiModelProperty("是否使用 0 正常 2 停用")
    private String delFlag;
    /**
     * 开户银行
     */
    @Excel(name = "开户银行")
    @ApiModelProperty("开户银行")
    private String accountBank;
    /**
     * 账号
     */
    @Excel(name = "账号")
    @ApiModelProperty("账号")
    private String accountNumber;
    /**
     * 户名
     */
    @Excel(name = "户名")
    @ApiModelProperty("户名")
    private String accountName;
    /**
     * 开户行行号
     */
    @Excel(name = "开户行行号")
    @ApiModelProperty("开户行行号")
    private String bankNumber;
    /**
     * 上级往来单位编码
     */
    @Excel(name = "上级往来单位编码")
    @ApiModelProperty("上级往来单位编码")
    private String parentCode;

    /**
     * 上级往来单位名称
     */
    @Excel(name = "上级往来单位名称")
    @ApiModelProperty("上级往来单位名称")
    private String parentName;
    /**
     * 主体类型
     */
    private String unitType;
    /**
     * 主体类型
     */
    private String sysUnitId;

}
