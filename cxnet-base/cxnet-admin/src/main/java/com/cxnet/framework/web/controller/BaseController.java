package com.cxnet.framework.web.controller;

import java.beans.PropertyEditorSupport;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollectionUtil;
import com.cxnet.common.domain.CommonPrint;
import com.cxnet.common.exception.CustomException;
import com.cxnet.framework.config.FineReportConfig;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.project.assets.data.cache.CacheMgr;
import com.cxnet.project.system.menu.domain.SysMenu;
import com.cxnet.project.system.menu.service.SysMenuServiceI;
import com.cxnet.project.system.user.domain.SysUser;
import com.cxnet.rpc.service.basedata.BdPersonServiceRpc;

import net.sf.ehcache.search.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cxnet.common.constant.HttpStatus;
import com.cxnet.common.utils.DateUtils;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.common.utils.sql.SqlUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.framework.web.page.PageDomain;
import com.cxnet.framework.web.page.TableDataInfo;
import com.cxnet.framework.web.page.TableSupport;

/**
 * web层通用数据处理
 *
 * @author cxnet
 */
@Component
public class BaseController {

    @Autowired(required = false)
    private SysMenuServiceI sysMenuServiceI;

    @Autowired(required = false)
    private FineReportConfig fineReportConfig;

    @Autowired(required = false)
    private BdPersonServiceRpc bdPersonServiceRpc;

    /**
     * 外链标识(0表示是外链)
     */
    private static final String FRAME_FLAG = "0";

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

    /**
     * 响应请求缓存分页数据
     */
    protected Map getCacheDataTable(String key, Map queryMap, Map orderByMap) {
        HashMap<String, Object> res = new HashMap<>(2);
        List list = new ArrayList<>();
        PageDomain pageDomain = TableSupport.buildPageRequest();
        if (pageDomain.getPageNum() == null && pageDomain.getPageSize() == null) {
            list = new CacheMgr().getCache(key, queryMap, orderByMap);
            res.put("total", list.size());
        } else {
            Integer pageNum = pageDomain.getPageNum();
            Integer pageSize = pageDomain.getPageSize();
            Integer pageStart = pageNum == 1 ? 0 : (pageNum - 1) * pageSize;
            Map cache = new CacheMgr().getCache(key, queryMap, orderByMap, pageStart, pageSize);
            List<Result> results = (List<Result>) cache.get("results");
            int total = (int) cache.get("total");
            for (Result result : results) {
                list.add(result.getValue());
            }
            res.put("total", total);
        }
        res.put("rows", list);
        return res;
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected TableDataInfo getDataTable(List<?> list) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setRows(list);
        if (CollectionUtil.isEmpty(list)) {
            rspData.setTotal(0);
        } else {
            rspData.setTotal(new PageInfo(list).getTotal());
        }
        return rspData;
    }


    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows) {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjaxResult(int rows, Object obj) {
        return rows > 0 ? AjaxResult.success(obj) : AjaxResult.error();
    }


    /**
     * 获取帆软打印路径
     *
     * @param commonPrint
     * @return
     */
    protected String basePrint(CommonPrint commonPrint) {
        if (StringUtils.isEmpty(commonPrint.getId())) {
            throw new CustomException("至少选择一条打印数据！");
        }
        //获取权限标识
        String perms = commonPrint.getPerms();

        SysMenu sysMenu = sysMenuServiceI.selectMenuByPerms(perms);
        String redirecturl = "";

        if (null != sysMenu) {
            String isFrame = sysMenu.getIsFrame();
            if (FRAME_FLAG.equals(isFrame)) {
                StringBuffer buffer = new StringBuffer();
                buffer.append(fineReportConfig.getFileStoreDir());
                String path = sysMenu.getPath();
                //判断根路径不为空
                if (org.apache.commons.lang3.StringUtils.isNotBlank(path)) {
                    buffer.append(path);

                } else {
                    buffer.append(fineReportConfig.getDefaultFile());
                }
                buffer.append("&id=");
                buffer.append(commonPrint.getId());
                buffer.append("&userCode=");
                buffer.append(SecurityUtils.getUsername());
                buffer.append("&unitCode=");
                buffer.append(commonPrint.getUnitCode());
                buffer.append("&deptCode=");
                buffer.append(commonPrint.getDeptCode());
                redirecturl = buffer.toString();
            }
        }
        return redirecturl;
    }

    /**
     * 判断当前登录用户是否是财务人员
     *
     * @return
     */
    protected boolean isFinancialOfficer() {
        return bdPersonServiceRpc.isFinancialOfficer(SecurityUtils.getUserId());
    }


}
