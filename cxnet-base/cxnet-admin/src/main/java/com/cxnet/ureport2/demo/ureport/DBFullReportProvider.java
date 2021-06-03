package com.cxnet.ureport2.demo.ureport;

import com.bstek.ureport.exception.ReportException;
import com.bstek.ureport.provider.report.ReportFile;
import com.bstek.ureport.provider.report.ReportProvider;
import com.cxnet.ureport2.demo.config.UReportConfig;
import com.cxnet.ureport2.demo.model.ReportContent;
import com.cxnet.ureport2.demo.service.ReportFullService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据库报表存储器
 * - 所有的数据内容都存储到数据库，包括文件名及文件内容等
 *
 * @author cxnet
 * @date 2019-04-24
 **/
@Slf4j
@Component
public class DBFullReportProvider implements ReportProvider {

    private static final String NAME = "数据库存储-Full";
    private static final String ENCODING = "UTF-8";

    private final UReportConfig uReportConfig;
    private final ReportFullService reportFullService;

    public DBFullReportProvider(UReportConfig uReportConfig, ReportFullService reportFullService) {
        this.uReportConfig = uReportConfig;
        this.reportFullService = reportFullService;
    }

    @Override
    public InputStream loadReport(String fileName) {
        log.info("load report file content by fileName: {}", fileName);
        ReportContent reportContent = reportFullService.findByFileName(substring(fileName));

        try {
            return IOUtils.toInputStream(reportContent.getContent(), ENCODING);
        } catch (IOException e) {
            throw new ReportException(e);
        }
    }

    @Override
    public void deleteReport(String fileName) {
        boolean result = reportFullService.deleteByFileName(substring(fileName));
        log.info("delete report file {} by fileName: {}", result ? "success" : "fail", fileName);
    }

    @Override
    public List<ReportFile> getReportFiles() {
        List<ReportContent> contents = reportFullService.findAll();
        log.info("报表文件数量: {}", contents.size());

        return contents.stream()
                .map(this::toReportFile)
                .sorted((f1, f2) -> f2.getUpdateDate().compareTo(f1.getUpdateDate()))
                .collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void saveReport(String fileName, String content) {
        reportFullService.save(substring(fileName), content);
        log.info("save report file content success by fileName: {}", fileName);
    }

    @Override
    public boolean disabled() {
        return uReportConfig.isDisableDBFullProvider();
    }

    @Override
    public String getPrefix() {
        return uReportConfig.getFullPrefix();
    }

    /**
     * 去掉前缀
     */
    private String substring(String fileName) {
        String prefix = uReportConfig.getFullPrefix();
        if (fileName.startsWith(prefix)) {
            return fileName.substring(prefix.length());
        }

        return fileName;
    }

    private ReportFile toReportFile(ReportContent content) {
        return new ReportFile(content.getFileName(), content.getUpdateTime());
    }
}
