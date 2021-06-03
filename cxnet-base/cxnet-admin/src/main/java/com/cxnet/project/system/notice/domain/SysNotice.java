package com.cxnet.project.system.notice.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.cxnet.framework.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 通知公告表 sys_notice
 *
 * @author cxnet
 */
@Data
public class SysNotice extends BaseEntity {

    /**
     * 公告ID
     */
    private String noticeId;

    /**
     * 公告标题
     */
    private String noticeTitle;

    /**
     * 公告类型（1通知 2公告）
     */
    private String noticeType;

    /**
     * 公告内容
     */
    private String noticeContent;

    /**
     * 公告状态（0正常 1关闭）
     */
    private String status;

    /**
     * 创建人
     */
    private String createByName;

    /**
     * 是否已读（0：已读 1：未读）
     */
    private String read;
    /**
     * 单位id
     */
    private String unitId;
    /**
     * 单位编码
     */
    private String unitCode;
    /**
     * 单位名称
     */
    private String unitName;
    /**
     * 部门id
     */
    private String deptId;
    /**
     * 部门code
     */
    private String deptCode;
    /**
     * 部门名称
     */
    private String deptName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("发布范围 1 pc端  2 移动端")
    private String noticeScope;

    @TableField(exist = false)
    List<String> noticeScopeList;

}
