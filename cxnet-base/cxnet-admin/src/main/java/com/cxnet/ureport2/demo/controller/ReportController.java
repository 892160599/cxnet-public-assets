package com.cxnet.ureport2.demo.controller;

import com.bstek.ureport.export.ExportManager;
import com.bstek.ureport.export.html.HtmlReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 报表接口
 *
 * @author cxnet
 * @date 2019-04-24
 **/
@Slf4j
@RestController
@RequestMapping("/report")
public class ReportController {

    private static final String PREVIEW = "/ureport/preview?_u=%s&_t=0";
    private final ExportManager exportManager;

    public ReportController(ExportManager exportManager) {
        this.exportManager = exportManager;
    }

    /**
     * 导出html，图表无法导出
     */
    @GetMapping("/html")
    public String html(@RequestParam("fileName") String fileName,
                       Map<String, Object> parameters,
                       HttpServletRequest request) {
        HtmlReport htmlReport = exportManager.exportHtml(fileName, request.getContextPath(), parameters);

        return toHtml(htmlReport.getStyle(), htmlReport.getContent());
    }

    /**
     * 分页导出html，图表无法导出
     */
    @GetMapping("/htmlPage")
    public String htmlPage(@RequestParam("fileName") String fileName,
                           @RequestParam("pageIndex") Integer pageIndex,
                           Map<String, Object> parameters,
                           HttpServletRequest request) {
        HtmlReport htmlReport = exportManager.exportHtml(fileName, request.getContextPath(), parameters, pageIndex);

        return toHtml(htmlReport.getStyle(), htmlReport.getContent());
    }

    /**
     * 浏览图表类型的报表
     */
    @GetMapping("/view")
    public void view(@RequestParam("fileName") String fileName,
                     HttpServletRequest request,
                     HttpServletResponse response) throws ServletException, IOException {
        String uri = String.format(PREVIEW, fileName);
        request.getRequestDispatcher(uri).forward(request, response);
    }

    private String toHtml(String style, String body) {
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<style type=\"text/css\">\n" +
                "%s" +
                "</style>\n" +
                "%s" +
                "</body>\n" +
                "</html>";

        return String.format(html, style, body);
    }
}
