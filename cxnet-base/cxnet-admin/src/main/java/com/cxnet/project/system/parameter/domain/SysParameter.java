package com.cxnet.project.system.parameter.domain;


import com.cxnet.framework.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * 系统配置表
 *
 * @author ssw
 */
@Data
public class SysParameter extends BaseEntity {
    /**
     * 参数编号
     */
    @ApiModelProperty("参数编号主键")
    private String parameterId;
    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    private String productName;
    /**
     * 版权信息
     */
    @ApiModelProperty("版权信息")
    private String copyRight;
    /**
     * 系统提示信息
     */
    @ApiModelProperty("系统提示信息")
    private String hintInfo;
    /**
     * 登录页是否有标题(0：是，1：否)
     */
    @ApiModelProperty("登录页是否有标题(0：是，1：否)")
    private Integer title;
    /**
     * 密码规则（0：纯数字（6~20位)，1：字母和数字需要同时存在（6~20位)，2:大小写字母、数字、特殊符号必须同时存在（8~20位),9：不限）
     */
    @ApiModelProperty(" 密码规则（0：纯数字（6~20位)，1：字母和数字需要同时存在（6~20位)，2:大小写字母、数字、特殊符号必须同时存在（8~20位),9：不限）")
    private String codeRule;
    /**
     * 图片编号
     */
    @ApiModelProperty("图片编号")
    private Integer imgId;
    /**
     * 图片地址
     */
    @ApiModelProperty("图片地址")
    private String imgUrl;
    /**
     * 登录页名称
     */
    @ApiModelProperty("登录页名称")
    private String loginIndexName;

    @ApiModelProperty("首页待办已办单位部门显示风格")
    private String homeUnitDeptStyle;

    @ApiModelProperty("备用字段")
    private String expExtend1;
    private String expExtend2;
    @ApiModelProperty("登陆超时")
    private String expExtend3;
    @ApiModelProperty("密码过期类型")
    private String expExtend4;
    @ApiModelProperty("移动端登陆超时（1：一天 2：一周 3：一个月 0: 不限制）")
    private String expExtend5;
    private String expExtend6;
    private String expExtend7;
    private String expExtend8;
    private String expExtend9;
    private String expExtend10;

    public String getParameterId() {
        return parameterId;
    }

    public void setParameterId(String parameterId) {
        this.parameterId = parameterId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCopyRight() {
        return copyRight;
    }

    public void setCopyRight(String copyRight) {
        this.copyRight = copyRight;
    }

    @Size(min = 0, max = 15, message = "系统提示信息不能超过15")
    public String getHintInfo() {
        return hintInfo;
    }

    public void setHintInfo(String hintInfo) {
        this.hintInfo = hintInfo;
    }

    public Integer getTitle() {
        return title;
    }

    public void setTitle(Integer title) {
        this.title = title;
    }

    public String getCodeRule() {
        return codeRule;
    }

    public void setCodeRule(String codeRule) {
        this.codeRule = codeRule;
    }

    public Integer getImgId() {
        return imgId;
    }

    public void setImgId(Integer imgId) {
        this.imgId = imgId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getLoginIndexName() {
        return loginIndexName;
    }

    public void setLoginIndexName(String loginIndexName) {
        this.loginIndexName = loginIndexName;
    }
}
