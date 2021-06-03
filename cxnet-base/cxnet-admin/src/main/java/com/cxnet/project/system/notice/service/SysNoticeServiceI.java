package com.cxnet.project.system.notice.service;

import java.util.List;

import com.cxnet.project.system.notice.domain.SysNotice;
import com.cxnet.project.system.notice.domain.vo.SysNoticeVo;

/**
 * 公告 服务层
 *
 * @author cxnet
 */
public interface SysNoticeServiceI {
    /**
     * 查询公告信息
     *
     * @param noticeId 公告ID
     * @return 公告信息
     */
    public SysNoticeVo selectNoticeById(String noticeId);

    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    public List<SysNotice> selectNoticeList(SysNotice notice);

    /**
     * 新增公告
     *
     * @param noticeVo 公告信息
     * @return 结果
     */
    public int insertNotice(SysNoticeVo noticeVo);

    /**
     * 修改公告
     *
     * @param noticeVo 公告信息
     * @return 结果
     */
    public int updateNotice(SysNoticeVo noticeVo);

    /**
     * 删除公告信息
     *
     * @param noticeId 公告ID
     * @return 结果
     */
    public int deleteNoticeById(String noticeId);

    /**
     * 批量删除公告信息
     *
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    public int deleteNoticeByIds(String[] noticeIds);
}
