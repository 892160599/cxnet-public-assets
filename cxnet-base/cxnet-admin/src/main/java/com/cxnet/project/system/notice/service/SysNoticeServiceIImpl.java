package com.cxnet.project.system.notice.service;

import java.util.Arrays;
import java.util.List;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.project.system.notice.domain.vo.SysNoticeVo;
import com.cxnet.project.system.noticeannex.domain.SysNoticeAnnex;
import com.cxnet.project.system.noticeannex.mapper.SysNoticeAnnexMapper;
import com.cxnet.project.system.noticeannex.service.SysNoticeAnnexService;
import com.cxnet.rpc.domain.message.MobMessageBill;
import com.cxnet.rpc.service.message.MobMessageBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cxnet.project.system.notice.domain.SysNotice;
import com.cxnet.project.system.notice.mapper.SysNoticeMapper;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 公告 服务层实现
 *
 * @author cxnet
 */
@Service
public class SysNoticeServiceIImpl implements SysNoticeServiceI {
    @Resource
    private SysNoticeMapper noticeMapper;
    @Resource
    private SysNoticeAnnexService sysNoticeAnnexService;
    @Resource
    private SysNoticeAnnexMapper sysNoticeAnnexMapper;
    @Autowired(required = false)
    private MobMessageBillService mobMessageBillService;


    /**
     * 查询公告信息
     *
     * @param noticeId 公告ID
     * @return 公告信息
     */
    @Override
    public SysNoticeVo selectNoticeById(String noticeId) {
        SysNoticeVo sysNoticeVo = new SysNoticeVo();
        SysNotice notice = noticeMapper.selectNoticeById(noticeId);
        List<SysNoticeAnnex> sysNoticeAnnexes = sysNoticeAnnexMapper.selectAnnex(notice.getNoticeId(), notice.getNoticeType());
        sysNoticeVo.setSysNotice(notice);
        sysNoticeVo.setSysNoticeAnnexes(sysNoticeAnnexes);
        return sysNoticeVo;
    }

    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    @Override
    public List<SysNotice> selectNoticeList(SysNotice notice) {
        if (StringUtils.isNotEmpty(notice.getNoticeScope())) {
            List<String> noticeScope = Arrays.asList(notice.getNoticeScope().split(","));
            notice.setNoticeScopeList(noticeScope);
        }
        return noticeMapper.selectNoticeList(notice);
    }

    /**
     * 新增公告
     *
     * @param noticeVo 公告信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertNotice(SysNoticeVo noticeVo) {
        SysNotice sysNotice = noticeVo.getSysNotice();
        sysNotice.setCreateBy(SecurityUtils.getUsername());
        noticeMapper.insertNotice(sysNotice);
        if ("2".equals(sysNotice.getNoticeScope())) {
            MobMessageBill mobMessageBill = new MobMessageBill();
            mobMessageBill.setAstId(sysNotice.getNoticeId());
            mobMessageBill.setTitle(sysNotice.getNoticeTitle());
            mobMessageBill.setBody(sysNotice.getNoticeContent());
            mobMessageBill.setMessageType(sysNotice.getNoticeType());
            mobMessageBillService.save(mobMessageBill);
        }
        insertAnnex(noticeVo);
        return 1;
    }

    public void insertAnnex(SysNoticeVo noticeVo) {
        SysNotice sysNotice = noticeVo.getSysNotice();
        String[] annexArray = noticeVo.getAnnexArray();
        QueryWrapper<SysNoticeAnnex> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysNoticeAnnex::getNoticeId, sysNotice.getNoticeId()).eq(SysNoticeAnnex::getAnnexType, sysNotice.getNoticeType());
        sysNoticeAnnexService.remove(queryWrapper);

        if (ArrayUtil.isNotEmpty(annexArray)) {
            for (String s : annexArray) {
                SysNoticeAnnex annex = new SysNoticeAnnex();
                annex.setNoticeId(sysNotice.getNoticeId());
                annex.setAnnexType(sysNotice.getNoticeType());
                annex.setFileId(s);
                sysNoticeAnnexService.save(annex);
            }
        }
    }

    /**
     * 修改公告
     *
     * @param noticeVo 公告信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateNotice(SysNoticeVo noticeVo) {
        SysNotice notice = noticeVo.getSysNotice();
        notice.setUpdateBy(SecurityUtils.getUsername());
        noticeMapper.updateNotice(notice);
        insertAnnex(noticeVo);
        return 1;
    }

    /**
     * 删除公告对象
     *
     * @param noticeId 公告ID
     * @return 结果
     */
    @Override
    public int deleteNoticeById(String noticeId) {
        return noticeMapper.deleteNoticeById(noticeId);
    }

    /**
     * 批量删除公告信息
     *
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    @Override
    public int deleteNoticeByIds(String[] noticeIds) {
        return noticeMapper.deleteNoticeByIds(noticeIds);
    }
}
