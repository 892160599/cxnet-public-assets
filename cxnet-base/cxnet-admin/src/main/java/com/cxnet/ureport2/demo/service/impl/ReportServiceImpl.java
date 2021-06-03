package com.cxnet.ureport2.demo.service.impl;

import com.cxnet.ureport2.demo.mapper.ReportMetaMapper;
import com.cxnet.ureport2.demo.model.ReportMeta;
import com.cxnet.ureport2.demo.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author cxnet
 * @date 2019-04-24
 **/
@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

    private final ReportMetaMapper mapper;

    public ReportServiceImpl(ReportMetaMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void save(String path) {
        log.info("save path: {}", path);
        if (isExisted(path)) {
            return;
        }

        ReportMeta reportMeta = new ReportMeta();
        reportMeta.setPath(path);
        reportMeta.setCreateTime(new Date());
        mapper.insertSelective(reportMeta);
    }

    @Override
    public void delete(String path) {
        log.info("delete path: {}", path);
        mapper.deleteByPath(path);
    }

    private boolean isExisted(String path) {
        return mapper.selectByPath(path) != null;
    }
}
