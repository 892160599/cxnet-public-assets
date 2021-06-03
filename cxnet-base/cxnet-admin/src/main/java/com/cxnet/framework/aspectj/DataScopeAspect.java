package com.cxnet.framework.aspectj;

import java.lang.reflect.Method;
import java.util.List;

import cn.hutool.core.util.ObjectUtil;
import com.cxnet.common.exception.CustomException;
import com.cxnet.project.system.dept.service.SysDeptServiceI;
import com.cxnet.project.system.menu.domain.SysMenu;
import com.cxnet.project.system.menu.mapper.SysMenuMapper;
import com.cxnet.project.system.role.domain.SysRoleMenu;
import com.cxnet.project.system.role.mapper.SysRoleMenuMapper;
import com.cxnet.rpc.domain.RpcBaseEntity;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.cxnet.common.utils.ServletUtils;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.common.utils.spring.SpringUtils;
import com.cxnet.framework.aspectj.lang.annotation.DataScope;
import com.cxnet.framework.security.LoginUser;
import com.cxnet.framework.security.service.TokenService;
import com.cxnet.framework.web.domain.BaseEntity;
import com.cxnet.project.system.role.domain.SysRole;
import com.cxnet.project.system.user.domain.SysUser;

/**
 * 数据过滤处理
 *
 * @author cxnet
 */
@Aspect
@Component
public class DataScopeAspect {

    @Autowired(required = false)
    private SysDeptServiceI sysDeptServiceI;

    @Autowired(required = false)
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Autowired(required = false)
    private SysMenuMapper sysMenuMapper;

    /**
     * 仅本人数据权限
     */
    public static final String DATA_SCOPE_SELF = "1";
    /**
     * 部门数据权限
     */
    public static final String DATA_SCOPE_DEPT = "2";
    /**
     * 部门及以下数据权限
     */
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "3";
    /**
     * 单位数据权限
     */
    public static final String DATA_SCOPE_UNIT = "4";
    /**
     * 单位及以下数据权限
     */
    public static final String DATA_SCOPE_UNIT_AND_CHILD = "5";
    /**
     * 全部数据权限
     */
    public static final String DATA_SCOPE_ALL = "6";
    /**
     * 自定数据权限
     */
    public static final String DATA_SCOPE_CUSTOM = "7";

    /**
     * 配置织入点
     */
    @Pointcut("@annotation(com.cxnet.framework.aspectj.lang.annotation.DataScope)")
    public void dataScopePointCut() {
    }

    @Before("dataScopePointCut()")
    public void doBefore(JoinPoint point) throws Throwable {
        handleDataScope(point);
    }

    protected void handleDataScope(final JoinPoint joinPoint) {
        // 获得注解
        DataScope controllerDataScope = getAnnotationLog(joinPoint);
        if (controllerDataScope == null) {
            return;
        }
        // 菜单id
        String[] menuPaths = ServletUtils.getMenuPath();
        SysMenu sysMenu = sysMenuMapper.selectMenuByPathAndTopPath(menuPaths[menuPaths.length - 1]);
        if (ObjectUtil.isNull(sysMenu)) {
            throw new CustomException("查询菜单权限出错，请联系管理员！");
        }
        // 获取当前的用户
        LoginUser loginUser = SpringUtils.getBean(TokenService.class).getLoginUser(ServletUtils.getRequest());
        // 所有授权部门
        String deptIds = sysDeptServiceI.getDeptsIdsByUserId();
        // 所有授权部门及下级部门
        String deptsAndSubsetsIds = sysDeptServiceI.getDeptsAndSubsetsIdsByUserId();
        // 所属单位及下级单位
        String thisUnitAndSubsetsIds = sysDeptServiceI.getThisUnitAndSubsetsIds();
        SysUser currentUser = loginUser.getUser();
        if (currentUser != null) {
            // 如果是超级管理员，则不过滤数据
            if (!currentUser.isAdmin()) {
                // 查询权限列表
                List<SysRoleMenu> sysRoleMenus = sysRoleMenuMapper.selectByUserIdAndMenuId(currentUser.getUserId(), sysMenu.getMenuId());
                dataScopeFilter(joinPoint, currentUser, controllerDataScope.mainAlias(),
                        deptIds, deptsAndSubsetsIds, thisUnitAndSubsetsIds, sysRoleMenus);
            }
        }
    }

    /**
     * 数据范围过滤
     *
     * @param joinPoint 切点
     * @param user      用户
     * @param mainAlias 主表别名
     * @param deptIds   当前登录用户所有部门id
     */
    public static void dataScopeFilter(JoinPoint joinPoint, SysUser user, String mainAlias, String deptIds, String deptsAndSubsetsIds, String thisUnitAndSubsetsIds, List<SysRoleMenu> sysRoleMenus) {
        StringBuilder sqlString = new StringBuilder();
        for (SysRoleMenu sysRoleMenu : sysRoleMenus) {
            String dataScope = sysRoleMenu.getDataScope();
            String menuId = sysRoleMenu.getMenuId();
            switch (dataScope) {
                case DATA_SCOPE_SELF:
                    if (StringUtils.isNotBlank(mainAlias)) {
                        sqlString.append(StringUtils.format(" OR {}.create_by = '{}' ", mainAlias, user.getUserName()));
                    } else {
                        // 数据权限为仅本人且没有userAlias别名不查询任何数据
                        sqlString.append(" OR 1=0 ");
                    }
                    break;
                case DATA_SCOPE_DEPT:
                    sqlString.append(StringUtils.format(" OR ({}.unit_id = '{}' and {}.dept_id in ('{}'))", mainAlias, user.getDeptId(), mainAlias, deptIds));
                    break;
                case DATA_SCOPE_DEPT_AND_CHILD:
                    sqlString.append(StringUtils.format(
                            " OR ({}.unit_id = '{}' and {}.dept_id IN ('{}'))",
                            mainAlias, user.getDeptId(), mainAlias, deptsAndSubsetsIds));
                    break;
                case DATA_SCOPE_UNIT:
                    sqlString.append(StringUtils.format(" OR {}.unit_id = '{}'", mainAlias, user.getDeptId()));
                    break;
                case DATA_SCOPE_UNIT_AND_CHILD:
                    sqlString.append(StringUtils.format(
                            " OR ({}.unit_id in ('{}'))",
                            mainAlias, thisUnitAndSubsetsIds));
                    break;
                case DATA_SCOPE_ALL:
                    sqlString = new StringBuilder();
                    break;
                case DATA_SCOPE_CUSTOM:
                    sqlString.append(StringUtils.format(
                            " OR ({}.unit_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = '{}' and menu_id = '{}')" +
                                    " OR {}.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = '{}' and menu_id = '{}')) ",
                            mainAlias, sysRoleMenu.getRoleId(), menuId, mainAlias, sysRoleMenu.getRoleId(), menuId));
                    break;
                default:
            }
        }

        if (StringUtils.isNotBlank(sqlString.toString())) {
            RpcBaseEntity baseEntity = (RpcBaseEntity) joinPoint.getArgs()[0];
            baseEntity.setDataScope(" AND (" + sqlString.substring(4) + ")");
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private DataScope getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(DataScope.class);
        }
        return null;
    }
}
