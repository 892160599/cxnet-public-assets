package com.cxnet.framework.aspectj;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cxnet.common.enums.HttpMethod;
import com.cxnet.common.utils.ObjectMapUtils;
import com.cxnet.common.utils.ServletUtils;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.common.utils.ip.IpUtils;
import com.cxnet.common.utils.spring.SpringUtils;
import com.cxnet.framework.aspectj.lang.annotation.BusinessLog;
import com.cxnet.framework.aspectj.lang.enums.BusinessStatus;
import com.cxnet.framework.manager.AsyncManager;
import com.cxnet.framework.manager.factory.AsyncFactory;
import com.cxnet.framework.security.LoginUser;
import com.cxnet.framework.security.service.TokenService;
import com.cxnet.project.businesslog.domain.SysBusinessLog;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.log.Log;
import lombok.extern.slf4j.Slf4j;

/**
 * 业务日志记录处理
 *
 * @author cxnet
 */
@Aspect
@Component
@Slf4j
public class BusinessLogAspect {

    /**
     * 配置织入点
     */
    @Pointcut("@annotation(com.cxnet.framework.aspectj.lang.annotation.BusinessLog)")
    public void logPointCut() {
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult) {
        handleLog(joinPoint, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, final Exception e, Object jsonResult) {
        try {
            // 获得注解
            BusinessLog businessLog = getAnnotationLog(joinPoint);
            if (businessLog == null) {
                return;
            }
            // 处理设置注解上的参数
            List<SysBusinessLog> sysBusinessLogList = getSysBusinessLogsList(joinPoint, e, jsonResult, businessLog);
            // 保存数据库
            AsyncManager.me().execute(AsyncFactory.recordBusinessOper(sysBusinessLogList, IpUtils.getIpAddr(ServletUtils.getRequest())));
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage(), e);
        }
    }

    /**
     * 获取操作日志列表
     *
     * @param
     * @param
     * @throws Exception
     */
    public List<SysBusinessLog> getSysBusinessLogsList(final JoinPoint joinPoint, final Exception e, Object jsonResult, BusinessLog businessLog) throws Exception {
        List<SysBusinessLog> sysBusinessLogList = new ArrayList<>();
        SysBusinessLog sysBusinessLog = new SysBusinessLog();
        //是否需求保存唯一标识
        if (businessLog.isRecordKey()) {
            //获取唯一标识
            String key = businessLog.key();
            //是否批量操作
            if (businessLog.isBatch()) {
                Object[] args = joinPoint.getArgs();
                if (ArrayUtil.isNotEmpty(args)) {
                    //获取数组中第一个元素
                    List ids = Convert.convert(List.class, args[0]);
                    for (Object id : ids) {
                        if (null != id) {
                            setBusinessLog(joinPoint, e, jsonResult, sysBusinessLog, businessLog, id.toString());
                            sysBusinessLogList.add(sysBusinessLog);
                            sysBusinessLog = new SysBusinessLog();
                        }
                    }
                }
            } else {
                Object[] args = joinPoint.getArgs();
                //获取数组中第一个值
                if (ArrayUtil.isNotEmpty(args) && null != args[0]) {
                    Map<String, Object> resultMap = ObjectMapUtils.objectToMap(args[0]);
                    String operatekey = "";

                    //获取主表对象
                    if (StringUtils.isNotBlank(businessLog.operatorTable())) {
                        Object masterObject = resultMap.get(businessLog.operatorTable());
                        operatekey = ReUtil.get(businessLog.key().trim() + "=(\\w+),", masterObject.toString(), 1);
                    } else {
                        operatekey = resultMap.get(businessLog.key().trim()) + "";
                    }
                    setBusinessLog(joinPoint, e, jsonResult, sysBusinessLog, businessLog, operatekey);
                    sysBusinessLogList.add(sysBusinessLog);
                }
            }
        } else {
            setBusinessLog(joinPoint, e, jsonResult, sysBusinessLog, businessLog, "");
            sysBusinessLogList.add(sysBusinessLog);
        }

        return sysBusinessLogList;
    }

    /**
     * 获取注解信息，设置业务日志
     *
     * @throws Exception
     */
    private SysBusinessLog setBusinessLog(final JoinPoint joinPoint, final Exception e, Object jsonResult, SysBusinessLog sysBusinessLog, BusinessLog businessLog, final String operatekey) throws Exception {
        sysBusinessLog.setOperateKey(operatekey);
        sysBusinessLog.setFieldName(businessLog.key());
        //设置模块
        sysBusinessLog.setModule(businessLog.module());
        //设置模块
        sysBusinessLog.setModule(businessLog.module());
        // 设置action动作
        sysBusinessLog.setBusinessType(businessLog.businessType().ordinal());
        // 设置操作人类别
        sysBusinessLog.setOperatorType(businessLog.operatorType().ordinal());
        // 设置内容
        sysBusinessLog.setContent(businessLog.value());
        // 是否需要保存request，参数和值
        if (businessLog.isSaveRequestData()) {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(joinPoint, sysBusinessLog);
        }
        // 获取当前的用户
        LoginUser loginUser = SpringUtils.getBean(TokenService.class).getLoginUser(ServletUtils.getRequest());
        sysBusinessLog.setStatus(BusinessStatus.SUCCESS.ordinal());
        // 请求的地址
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        sysBusinessLog.setOperIp(ip);
        // 返回参数
        if (businessLog.isSaveRequestData()) {
            sysBusinessLog.setJsonResult(JSON.toJSONString(jsonResult));
        }
        sysBusinessLog.setOperUrl(ServletUtils.getRequest().getRequestURI());

        if (loginUser != null) {
            sysBusinessLog.setOperUserId(loginUser.getUser().getUserId());
            sysBusinessLog.setOperUserName(loginUser.getUser().getNickName());
        }

        if (e != null) {
            sysBusinessLog.setStatus(BusinessStatus.FAIL.ordinal());
            sysBusinessLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
        }
        // 设置方法名称
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        sysBusinessLog.setMethod(className + "." + methodName + "()");
        // 设置请求方式
        sysBusinessLog.setRequestMethod(ServletUtils.getRequest().getMethod());
        //设置时间
        sysBusinessLog.setOperTime(DateUtil.date());
        return sysBusinessLog;

    }


    /**
     * 获取请求的参数，放到log中
     *
     * @param operLog 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(JoinPoint joinPoint, SysBusinessLog sysBusinessLog) throws Exception {
        String requestMethod = sysBusinessLog.getRequestMethod();
        if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
            String params = argsArrayToString(joinPoint.getArgs());
            sysBusinessLog.setOperParam(StringUtils.substring(params, 0, 2000));
        } else {
            Map<?, ?> paramsMap = (Map<?, ?>) ServletUtils.getRequest().getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            sysBusinessLog.setOperParam(StringUtils.substring(paramsMap.toString(), 0, 2000));
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private BusinessLog getAnnotationLog(JoinPoint joinPoint) throws Exception {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(BusinessLog.class);
        }
        return null;
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray) {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            for (int i = 0; i < paramsArray.length; i++) {
                if (!isFilterObject(paramsArray[i])) {
                    Object jsonObj = JSON.toJSON(paramsArray[i]);
                    params += jsonObj.toString() + " ";
                }
            }
        }
        return params.trim();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    public boolean isFilterObject(final Object o) {
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse;
    }
}
