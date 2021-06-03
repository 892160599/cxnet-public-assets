package com.cxnet.asset.card.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Max;

import org.hibernate.validator.constraints.Length;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cxnet.asset.check.domain.AstCheckBill;
import com.cxnet.asset.check.domain.AstCheckList;
import com.cxnet.common.utils.poi.annotation.Excel;
import com.cxnet.framework.web.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 资产主表
 *
 * @author guks
 * @since 2021-03-29 10:23:02
 */
@Data
@ApiModel(description = "资产主表")
public class AstCard extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -50533399743093815L;

    @ApiModelProperty("主键")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("单位id")
    private String unitId;

    @ApiModelProperty("单位编码")
    private String unitCode;


    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("卡片编号")
    private String astCode;

    @ApiModelProperty("资产类别编码")
    @Excel(name = "资产类别编码")
    private String categoryCode;

    @ApiModelProperty("资产类别名称")
    @Excel(name = "资产类别名称")
    private String categoryName;

    @ApiModelProperty("资产大类编码，存放一级1,2,3,4,5,6")
    @Excel(name = "资产大类编码")
    private String classCode;

    @ApiModelProperty("资产大类名称，存放通用设备、专用设备")
    @Excel(name = "资产大类名称")
    private String className;

    @ApiModelProperty("资产类型编码")
    @Excel(name = "资产类型编码")
    private String typeCode;

    @ApiModelProperty("资产类型名称")
    @Excel(name = "资产类型名称")
    private String typeName;

    @ApiModelProperty("资产名称")
    @Excel(name = "资产名称")
    private String assetName;

    @ApiModelProperty("取得方式编码")
    @Excel(name = "取得方式编码")
    private String addCode;

    @ApiModelProperty("取得日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "取得日期")
    private Date addDate;

    @ApiModelProperty("投入使用日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startUsedate;

    @ApiModelProperty("设计使用月份")
    private Integer useLife;

    @ApiModelProperty("预计资产到期日")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date finshUsedate;

    @ApiModelProperty("数量")
    @Excel(name = "数量")
    private Integer qty;

    @ApiModelProperty("计量单位")
    private String measurement;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("预算项目编号")
    private String projectCode;

    @ApiModelProperty("发票号")
    private String receiptCode;

    @ApiModelProperty("购置日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date acquisitionDate;

    @ApiModelProperty("采购合同id")
    private String conId;

    @ApiModelProperty("采购合同编号")
    private String conCode;

    @ApiModelProperty("价值类型")
    private String costType;

    @ApiModelProperty("财政拨款")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal finCost;

    @ApiModelProperty("价值")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Excel(name = "价值")
    private BigDecimal cost;

    @ApiModelProperty("非财政拨款")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal nofinCost;

    @ApiModelProperty("财务入账状态 (0 已入账 2 未入账)")
    private String isVou;

    @ApiModelProperty("财务入账日期")
    private Date vouDate;

    @ApiModelProperty("会计凭证号")
    private String vouNo;

    @ApiModelProperty("折旧状态 (0 折旧 2不折旧)")
    private String isDep;

    @ApiModelProperty("折旧方法")
    private String depMethod;

    @ApiModelProperty("已计提月份")
    private Integer depedMo;

    @ApiModelProperty("累计折旧")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal depTotal;

    @ApiModelProperty("待折旧月份")
    private Integer depreciationMo;


    @ApiModelProperty("月折旧额")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal depMoValue;

    @ApiModelProperty("账面净值")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal netValue;

    @ApiModelProperty("使用状况")
    private String usageStatus;

    @ApiModelProperty("使用部门id")
    private String departmentId;

    @ApiModelProperty("使用部门编码")
    private String departmentCode;

    @ApiModelProperty("使用部门名称")
    private String departmentName;

    @ApiModelProperty("使用人编码")
    private String empCode;

    @ApiModelProperty("使用人名称")
    private String empName;

    @ApiModelProperty("部门id")
    private String deptId;

    @ApiModelProperty("部门编码")
    private String deptCode;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("存放地点编码")
    private String placeCode;

    @ApiModelProperty("存放地点")
    private String placeName;

    @ApiModelProperty("使用信息备注")
    private String useRemark;

    @ApiModelProperty("基本信息图片id")
    private String basicFileId;

    @ApiModelProperty("盘点图片id")
    private String inventoryFileId;

    @ApiModelProperty("品牌")
    private String brand;

    @ApiModelProperty("规格型号")
    private String spec;

    @ApiModelProperty("产品序列号")
    private String productSn;

    @ApiModelProperty("生产厂家")
    private String mfrs;

    @ApiModelProperty("销售商")
    private String dealer;

    @ApiModelProperty("保修截止日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date guaranteeDate;

    @ApiModelProperty("产地")
    private String madein;

    @ApiModelProperty("使用权面积（㎡）")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal areaSyqmj;

    @ApiModelProperty("独用面积（㎡）")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal areaDymj;

    @ApiModelProperty("分摊面积（㎡）")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal areaFtmj;

    @ApiModelProperty("土地级次")
    private String landGradation;

    @ApiModelProperty("产权形式编码")
    private String rightFormCode;

    @ApiModelProperty("产权形式名称")
    private String rightFormName;

    @ApiModelProperty("权属证明")
    private String ownershipCert;

    @ApiModelProperty("权属性质")
    private String ownershipNature;

    @ApiModelProperty("权属证号")
    private String ownershipNumber;

    @ApiModelProperty("权属年限")
    private String ownershipAge;

    @ApiModelProperty("权属人")
    private String ownershipHolder;

    @ApiModelProperty("发证日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date ownershipDate;

    @ApiModelProperty("使用权类型")
    private String ownershipType;

    @ApiModelProperty("地类（用途）")
    private String landUse;

    @ApiModelProperty("坐落位置")
    private String location;

    @ApiModelProperty("自用面积（㎡）")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal areaZymj;

    @ApiModelProperty("其他面积（㎡）")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal areaQtmj;

    @ApiModelProperty("出借面积（㎡）")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal areaCjmj;

    @ApiModelProperty("出租面积（㎡）")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal areaCzmj;

    @ApiModelProperty("闲置面积（㎡）")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal areaXzmj;

    @ApiModelProperty("自用面积（元）")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal costZymj;

    @ApiModelProperty("出借面积（元）")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal costCjmj;

    @ApiModelProperty("出租面积（元")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal costCzmj;

    @ApiModelProperty("闲置面积（元）")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal costXzmj;

    @ApiModelProperty("其他面积（元）")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal costQtmj;

    @ApiModelProperty("建筑结构")
    private String buildStructure;

    @ApiModelProperty("房屋所有权人")
    private String houseOwner;

    @ApiModelProperty("竣工日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date completionDate;

    @ApiModelProperty("设计用途")
    private String designPurpose;

    @ApiModelProperty("构筑物计量")
    private String structureMeasure;

    @ApiModelProperty("建筑面积（㎡）")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal areaJzmj;

    @ApiModelProperty("其中：取暖面积（㎡）")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal areaQnmj;

    @ApiModelProperty("其中：危房面积（㎡）")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal areaWfmj;

    @ApiModelProperty("采购组织形式")
    private String purchaseType;

    @ApiModelProperty("设备用途")
    private String equipmentUse;

    @ApiModelProperty("车牌号")
    private String carNo;

    @ApiModelProperty("汽车排气量")
    private String carDisplacement;

    @ApiModelProperty("车辆行驶证")
    private String carLicense;

    @ApiModelProperty("持证人")
    private String carHolder;

    @ApiModelProperty("注册登记日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date carRegdate;

    @ApiModelProperty("发动机号")
    private String carEngineNo;

    @ApiModelProperty("车辆识别编码")
    private String carIdNo;

    @ApiModelProperty("车辆产地")
    private String carMadein;

    @ApiModelProperty("车辆用途")
    private String carUse;

    @ApiModelProperty("来源地")
    private String originPlace;

    @ApiModelProperty("藏品年代")
    private String collectionAge;

    @ApiModelProperty("文物等级")
    private String culturalGrade;

    @ApiModelProperty("出版社")
    private String press;

    @ApiModelProperty("出版日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date publishedDate;

    @ApiModelProperty("保存年限（月）")
    private String saveLife;

    @ApiModelProperty("档案号")
    private String arcNo;

    @ApiModelProperty("预计寿命/栽种年龄")
    private BigDecimal plantingAge;

    @ApiModelProperty("出生/栽种年份")
    private Integer plantingYear;

    @ApiModelProperty("纲属科")
    private String genericFamily;

    @ApiModelProperty("资产使用到期日期")
    private String expiredDate;

    @ApiModelProperty("是否已封存 1 是 0 否 ，处置后封存资产")
    private String isSealed;

    @ApiModelProperty("封存人编码")
    private String sealedEmpcode;

    @ApiModelProperty("封存人名称")
    private String sealedEmpname;

    @ApiModelProperty("封存时间")
    private String sealedDate;

    @ApiModelProperty("1 是 0 否 ，卡片新增后为初始状态，发生业务后变成0")
    private String initialState;

    @ApiModelProperty("原系统卡片编号")
    private String oldAstCode;

    @ApiModelProperty("验收单号")
    private String acceptanceNo;

    @ApiModelProperty("出租单位编码")
    private String rentAgyCode;

    @ApiModelProperty("出租单位名称")
    private String rentAgyName;

    @ApiModelProperty("出借单位编码")
    private String lendAgyCode;

    @ApiModelProperty("出借单位名称")
    private String lendAgyName;

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

    @ApiModelProperty("卡片状态 0 未确认,1已确认,2已处置,3已调拨")
    private String astStatus;

    @ApiModelProperty("管理部门id")
    private String adminDepartmentId;

    @ApiModelProperty("管理部门编码")
    private String adminDepartmentCode;

    @ApiModelProperty("管理部门名称")
    private String adminDepartmentName;

    @ApiModelProperty("资产类别id")
    private String categoryId;

    @ApiModelProperty("资产类型id")
    private String typeId;

    @ApiModelProperty("存放地点id")
    private String placeId;

    @ApiModelProperty("使用人id")
    private String empId;

    @ApiModelProperty("删除标记")
    private String delFlag;

    @TableField(exist = false)
    @ApiModelProperty("导入卡片行号")
    private String cardLine;

    @ApiModelProperty("期初导入")
    private String isInitImport;

    public AstCard() {
    }

    /**
     * 资产验收==》资产卡片
     *
     * @param astCheckBill
     * @param astCheckList
     */
    public AstCard(AstCheckBill astCheckBill, AstCheckList astCheckList) {
        unitId = astCheckBill.getUnitId();
        unitCode = astCheckBill.getUnitCode();
        unitName = astCheckBill.getUnitName();
        deptId = astCheckBill.getDeptId();
        deptCode = astCheckBill.getDeptCode();
        deptName = astCheckBill.getDeptName();
        categoryCode = astCheckList.getCategoryCode();
        categoryName = astCheckList.getCategoryName();
        classCode = astCheckList.getClassCode();
        className = astCheckList.getClassName();
        typeCode = astCheckList.getTypeCode();
        typeName = astCheckList.getTypeName();
        assetName = astCheckList.getAstName();
        costType = astCheckList.getCostType();
        cost = astCheckList.getCost();
        qty = astCheckList.getQty();
        acquisitionDate = astCheckList.getAcquisitionDate();
        startUsedate = astCheckList.getStartUsedate();
        useLife = astCheckList.getAssetUselife();
        vouDate = astCheckList.getVouDate();
        departmentId = astCheckList.getDepartmentId();
        departmentCode = astCheckList.getDepartmentCode();
        departmentName = astCheckList.getDepartmentName();
        empCode = astCheckList.getEmpCode();
        empName = astCheckList.getEmpName();
        astStatus = "1";
        netValue = BigDecimal.ZERO;
        depTotal = BigDecimal.ZERO;
        delFlag = "0";
        netValue = cost;
        depreciationMo = useLife;
        depedMo = 0;
        depMethod = astCheckList.getDepMethod();
        usageStatus = "1";
    }
}

