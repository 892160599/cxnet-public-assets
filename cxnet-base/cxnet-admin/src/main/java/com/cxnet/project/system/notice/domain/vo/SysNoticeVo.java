package com.cxnet.project.system.notice.domain.vo;

import com.cxnet.project.system.notice.domain.SysNotice;
import com.cxnet.project.system.noticeannex.domain.SysNoticeAnnex;

import java.util.List;

public class SysNoticeVo {

    private SysNotice sysNotice;

    private String[] annexArray;

    private List<SysNoticeAnnex> sysNoticeAnnexes;

    public SysNotice getSysNotice() {
        return sysNotice;
    }

    public void setSysNotice(SysNotice sysNotice) {
        this.sysNotice = sysNotice;
    }

    public String[] getAnnexArray() {
        return annexArray;
    }

    public void setAnnexArray(String[] annexArray) {
        this.annexArray = annexArray;
    }

    public List<SysNoticeAnnex> getSysNoticeAnnexes() {
        return sysNoticeAnnexes;
    }

    public void setSysNoticeAnnexes(List<SysNoticeAnnex> sysNoticeAnnexes) {
        this.sysNoticeAnnexes = sysNoticeAnnexes;
    }
}
