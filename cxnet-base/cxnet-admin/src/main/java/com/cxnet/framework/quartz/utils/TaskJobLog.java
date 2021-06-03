package com.cxnet.framework.quartz.utils;

import com.cxnet.common.utils.StringUtils;
import com.cxnet.framework.quartz.domain.ScheduleJobBean;
import com.cxnet.framework.quartz.domain.ScheduleJobLogBean;
import com.cxnet.framework.quartz.service.ScheduleJobLogService;
import lombok.SneakyThrows;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 定时器执行日志记录
 *
 * @author cxnet
 * @date 2021/4/25
 */
public class TaskJobLog extends QuartzJobBean {

    private static final Logger LOG = LoggerFactory.getLogger(TaskJobLog.class);

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext context) {
        Object o = context.getMergedJobDataMap().get(ScheduleJobBean.JOB_PARAM_KEY);
        ScheduleJobBean jobBean = new ScheduleJobBean();
        BeanUtils.copyProperties(o, jobBean);
        ScheduleJobLogService scheduleJobLogService = (ScheduleJobLogService) SpringContextUtil.getBean("scheduleJobLogService");
        // 定时器日志记录
        ScheduleJobLogBean logBean = new ScheduleJobLogBean();
        logBean.setJobId(jobBean.getJobId());
        logBean.setBeanName(jobBean.getBeanName());
        logBean.setParams(jobBean.getParams());
        logBean.setCreateTime(new Date());
        long beginTime = System.currentTimeMillis();
        try {
            String beanName = jobBean.getBeanName();
            String clazz = StringUtils.substringBeforeLast(beanName, ".");
            String func = StringUtils.substringAfterLast(beanName, ".");
            Object bean = null;
            // 判断是类别名还是全类名
            if (isValidClassName(clazz)) {
                bean = Class.forName(clazz).newInstance();
            } else {
                bean = SpringContextUtil.getBean(clazz);
            }
            // 获取参数类型
            List<Object[]> methodParams = getMethodParams(jobBean.getParams());
            // 根据参数类型执行
            if (StringUtils.isNotNull(methodParams) && methodParams.size() > 0) {
                Method method = bean.getClass().getDeclaredMethod(func, getMethodParamsType(methodParams));
                method.invoke(bean, getMethodParamsValue(methodParams));
            } else {
                Method method = bean.getClass().getDeclaredMethod(func);
                method.invoke(bean);
            }
            // 计算时间
            long executeTime = System.currentTimeMillis() - beginTime;
            logBean.setTimes((int) executeTime);
            logBean.setStatus(0);
            LOG.info("定时器 === >> " + jobBean.getJobId() + "执行成功,耗时 === >> " + executeTime);
        } catch (Exception e) {
            // 异常信息
            long executeTime = System.currentTimeMillis() - beginTime;
            logBean.setTimes((int) executeTime);
            logBean.setStatus(1);
            logBean.setError(e.getMessage());
        } finally {
            scheduleJobLogService.insert(logBean);
        }
    }

    /**
     * 校验是否为为class包名
     *
     * @param invokeTarget 名称
     * @return true是 false否
     */
    private static boolean isValidClassName(String invokeTarget) {
        return StringUtils.countMatches(invokeTarget, ".") > 1;
    }

    /**
     * 获取参数类型
     *
     * @param methodParams 参数相关列表
     * @return 参数类型列表
     */
    private static Class<?>[] getMethodParamsType(List<Object[]> methodParams) {
        Class<?>[] classs = new Class<?>[methodParams.size()];
        int index = 0;
        for (Object[] os : methodParams) {
            classs[index] = (Class<?>) os[1];
            index++;
        }
        return classs;
    }

    /**
     * 获取参数值
     *
     * @param methodParams 参数相关列表
     * @return 参数值列表
     */
    private static Object[] getMethodParamsValue(List<Object[]> methodParams) {
        Object[] classs = new Object[methodParams.size()];
        int index = 0;
        for (Object[] os : methodParams) {
            classs[index] = (Object) os[0];
            index++;
        }
        return classs;
    }

    /**
     * 字符串转对象参数
     *
     * @param invokeTarget 目标字符串
     * @return 结果集
     */
    private static List<Object[]> getMethodParams(String invokeTarget) {
        if (StringUtils.isEmpty(invokeTarget)) {
            return null;
        }
        String[] methodParams = invokeTarget.split(",");
        List<Object[]> classs = new LinkedList<>();
        for (String methodParam : methodParams) {
            String str = StringUtils.trimToEmpty(methodParam);
            // String字符串类型，包含'
            if (StringUtils.contains(str, "'")) {
                classs.add(new Object[]{StringUtils.replace(str, "'", ""), String.class});
            }
            // boolean布尔类型，等于true或者false
            else if (StringUtils.equals(str, "true") || StringUtils.equalsIgnoreCase(str, "false")) {
                classs.add(new Object[]{Boolean.valueOf(str), Boolean.class});
            }
            // long长整形，包含L
            else if (StringUtils.containsIgnoreCase(str, "L")) {
                classs.add(new Object[]{Long.valueOf(StringUtils.replaceIgnoreCase(str, "L", "")), Long.class});
            }
            // double浮点类型，包含D
            else if (StringUtils.containsIgnoreCase(str, "D")) {
                classs.add(new Object[]{Double.valueOf(StringUtils.replaceIgnoreCase(str, "D", "")), Double.class});
            }
            // float浮点类型，包含F
            else if (StringUtils.containsIgnoreCase(str, "F")) {
                classs.add(new Object[]{Double.valueOf(StringUtils.replaceIgnoreCase(str, "F", "")), Float.class});
            }
            // 其他类型归类为整形
            else {
                classs.add(new Object[]{Integer.valueOf(str), Integer.class});
            }
        }
        return classs;
    }
}