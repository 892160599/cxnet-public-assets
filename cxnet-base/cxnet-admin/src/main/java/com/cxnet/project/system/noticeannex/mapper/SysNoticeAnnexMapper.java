package com.cxnet.project.system.noticeannex.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cxnet.project.system.noticeannex.domain.SysNoticeAnnex;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 通知公告附件表(SysNoticeAnnex)表数据库访问层
 *
 * @author ssw
 * @since 2020-10-26 12:26:40
 */
public interface SysNoticeAnnexMapper extends BaseMapper<SysNoticeAnnex> {
    /**
     * 查询附件文件的详细信息
     *
     * @param noticeId  通知公告id
     * @param annexType 附件类型
     * @return
     */
    List<SysNoticeAnnex> selectAnnex(@Param("noticeId") String noticeId, @Param("annexType") String annexType);
}