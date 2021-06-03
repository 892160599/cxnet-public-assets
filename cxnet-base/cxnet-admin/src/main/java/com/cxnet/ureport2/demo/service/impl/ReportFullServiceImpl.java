package com.cxnet.ureport2.demo.service.impl;

import com.cxnet.ureport2.demo.mapper.ReportContentMapper;
import com.cxnet.ureport2.demo.model.ReportContent;
import com.cxnet.ureport2.demo.service.ReportFullService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cxnet
 * @date 2019-04-25
 **/
@Slf4j
@Service
public class ReportFullServiceImpl implements ReportFullService {

    private final ReportContentMapper mapper;

    public ReportFullServiceImpl(ReportContentMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ReportContent findByFileName(String fileName) {
        return mapper.findByFileName(fileName);
    }

    @Override
    public ReportContent findById(String id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public void save(String fileName, String content) {
        if (isExisted(fileName)) {
            update(fileName, content);
        } else {
            ReportContent reportContent = assembleReportContent(fileName, content);
            mapper.insertSelective(reportContent);
        }
    }

    @Override
    public void update(String fileName, String content) {
        mapper.updateContentByFileName(fileName, content);
    }

    @Override
    public boolean deleteByFileName(String fileName) {
        return mapper.deleteByFileName(fileName) > 0;
    }

    @Override
    public boolean deleteById(String id) {
        return mapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public List<ReportContent> findAll() {
        List<ReportContent> all = mapper.findAll();
        if (all == null) {
            return new ArrayList<>();
        }

        return all;
    }

    private boolean isExisted(String fileName) {
        return findByFileName(fileName) != null;
    }

    private ReportContent assembleReportContent(String fileName, String content) {
        ReportContent reportContent = new ReportContent();
        reportContent.setFileName(fileName);
        reportContent.setContent(content);

        return reportContent;
    }
}
