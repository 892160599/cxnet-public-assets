package com.cxnet.project.system.user.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.constant.Constants;
import com.cxnet.common.exception.BaseException;
import com.cxnet.common.exception.user.CaptchaException;
import com.cxnet.common.exception.user.CaptchaExpireException;
import com.cxnet.common.utils.MessageUtils;
import com.cxnet.common.utils.RegularUtils;
import com.cxnet.framework.manager.AsyncManager;
import com.cxnet.framework.manager.factory.AsyncFactory;
import com.cxnet.framework.redis.RedisCache;
import com.cxnet.framework.redis.RedisUtil;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.project.system.parameter.domain.SysParameter;
import com.cxnet.project.system.parameter.mapper.SysParameterMapper;
import com.cxnet.project.system.parameter.service.SysParameterServiceI;
import com.cxnet.project.system.user.domain.SysUserMenu;
import com.cxnet.project.system.user.mapper.SysUserMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cxnet.common.constant.UserConstants;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.framework.aspectj.lang.annotation.DataScope;
import com.cxnet.project.system.post.domain.SysPost;
import com.cxnet.project.system.role.domain.SysRole;
import com.cxnet.project.system.user.domain.SysUser;
import com.cxnet.project.system.user.domain.SysUserPost;
import com.cxnet.project.system.user.domain.SysUserRole;
import com.cxnet.project.system.post.mapper.SysPostMapper;
import com.cxnet.project.system.role.mapper.SysRoleMapper;
import com.cxnet.project.system.user.mapper.SysUserMapper;
import com.cxnet.project.system.user.mapper.SysUserPostMapper;
import com.cxnet.project.system.user.mapper.SysUserRoleMapper;

import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * 用户 业务层处理
 *
 * @author cxnet
 */
@Service
public class SysUserServiceIImpl implements SysUserServiceI {
    @Autowired(required = false)
    private SysUserMapper userMapper;

    @Autowired(required = false)
    private SysRoleMapper roleMapper;

    @Autowired(required = false)
    private SysPostMapper postMapper;

    @Autowired(required = false)
    private SysUserRoleMapper userRoleMapper;

    @Autowired(required = false)
    private SysUserPostMapper userPostMapper;

    @Autowired(required = false)
    private SysUserMenuMapper userMenuMapper;

    @Autowired(required = false)
    private SysParameterMapper sysParameterMapper;

    @Autowired(required = false)
    private RedisCache redisCache;
    @Autowired(required = false)
    private RedisUtil redisUtil;

    @Autowired(required = false)
    private SysUserMapper sysUserMapper;

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
//    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUserList(SysUser user) {
        return userMapper.selectUserList(user);
    }

    /**
     * 查询未绑定部门人员的账号
     *
     * @param user 查询条件
     * @return 结果
     */
    @Override
    public List<SysUser> selectBdUserList(SysUser user) {
        return userMapper.selectBdUserList(user);
    }

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String userName) {
        return userMapper.selectUserByUserName(userName);
    }

    /**
     * 通过单位部门查询用户
     *
     * @param deptCode 用户名
     * @return 用户对象信息
     */
    @Override
    public List<SysUser> selectUserByDept(String deptCode) {
        return userMapper.selectUserByDept(deptCode);
    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(String userId) {
        return userMapper.selectUserById(userId);
    }

    /**
     * 根据用户ID查询菜单ID集合
     *
     * @param userId 用户ID
     * @return 选中菜单列表
     */
    @Override
    public List<Long> selectMenuIdsByUserId(String userId) {
        return userMenuMapper.selectMenuIdsByUserId(userId);
    }

    /**
     * 查询用户所属角色组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String userName) {
        List<SysRole> list = roleMapper.selectRolesByUserName(userName);
        StringBuffer idsStr = new StringBuffer();
        for (SysRole role : list) {
            idsStr.append(role.getRoleName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString())) {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 查询用户所属岗位组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserPostGroup(String userName) {
        List<SysPost> list = postMapper.selectPostsByUserName(userName);
        StringBuffer idsStr = new StringBuffer();
        for (SysPost post : list) {
            idsStr.append(post.getPostName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString())) {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    @Override
    public String checkUserNameUnique(String userName) {
        int count = userMapper.checkUserNameUnique(userName);
        if (count > 0) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkPhoneUnique(SysUser user) {
        String userId = StringUtils.isNull(user.getUserId()) ? "-1" : user.getUserId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhonenumber());
        if (StringUtils.isNotNull(info) && !info.getUserId().equals(userId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkEmailUnique(SysUser user) {
        String userId = StringUtils.isNull(user.getUserId()) ? "-1" : user.getUserId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && !info.getUserId().equals(userId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUser user) {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin()) {
            throw new CustomException("不允许系统管理员");
        }
    }

    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertUser(SysUser user) {
        checkPwd(user.getPassword());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        // 设置过期时间
        user.setValidity(this.validityTime());
        // 新增用户信息
        int rows = userMapper.insertUser(user);
        // 新增用户岗位关联
        insertUserPost(user);
        // 新增用户与角色管理
        insertUserRole(user);
        // 新增用户与菜单管理
        insertUserMenu(user);
        return rows;
    }

    /**
     * 验证密码规则
     *
     * @param pwd 密码
     */
    public void checkPwd(String pwd) {
        SysParameter sysParameter = sysParameterMapper.selectSysParameter();
        switch (sysParameter.getCodeRule()) {
//            case RegularUtils.NUMBER:
//                if (!RegularUtils.isNum(pwd)) {
//                    throw new CustomException("密码必须包含6~20位的数字");
//                }
//                break;
            case RegularUtils.NUMBER_AND_LETTER:
                if (!RegularUtils.isNumAndLetterOrSymbol(pwd)) {
                    throw new CustomException("密码必须包含6~20位的数字和字母");
                }
                break;
            case RegularUtils.COMPLEXITY:
                if (!RegularUtils.isStrong(pwd)) {
                    throw new CustomException("密码必须由8~20位的大小写字母、数字、特殊符号组成");
                }
                break;
            case "9":
                // 不控制密码复杂度
                break;
            default:
                throw new CustomException("未定义的密码规则");
        }
    }


    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public SysUser insertUserAndPost(SysUser user) {
        // 新增用户信息
        return userMapper.insertUserAndPost(user);
    }

    /**
     * 批量新增用户信息
     *
     * @param sysUsers 用户信息
     * @return 结果
     */
    @Override
    public int insertBatchSysUser(List<SysUser> sysUsers) {
        return userMapper.insertBatchSysUser(sysUsers);
    }

    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUser(SysUser user) {
        String userId = user.getUserId();
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(user);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPostByUserId(userId);
        // 新增用户与岗位管理
        insertUserPost(user);
        // 删除用户与菜单关联
        userMenuMapper.deleteUserMenuByUserId(userId);
        // 新增用户与菜单管理
        insertUserMenu(user);
        // 重置缓存
        Integer loginNum = redisCache.getCacheObject(user.getUserName());
        if (loginNum != null && loginNum > 0) {
            redisCache.setCacheObject(user.getUserName(), 0);
        }
        // 设置过期时间
        user.setValidity(this.validityTime());
        return userMapper.updateUserInfo(user);
    }


    /**
     * 修改用户菜单
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUserMenuIds(SysUser user) {
        String userId = user.getUserId();
        // 删除用户与菜单关联
        userMenuMapper.deleteUserMenuByUserId(userId);
        // 新增用户与菜单管理
        insertUserMenu(user);
        return 1;
    }

    /**
     * 修改用户状态
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserStatus(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserProfile(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户头像
     *
     * @param userName 用户名
     * @param avatar   头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(String userName, String avatar) {
        return userMapper.updateUserAvatar(userName, avatar) > 0;
    }

    /**
     * 重置用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetPwd(SysUser user) {
        checkPwd(user.getPassword());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        // 设置过期时间
        user.setValidity(this.validityTime());
        return userMapper.updateUser(user);
    }

    /**
     * 重置用户密码
     *
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    @Override
    public int resetUserPwd(String userName, String password) {
        checkPwd(password);
        int res = userMapper.resetUserPwd(userName, SecurityUtils.encryptPassword(password));
        if (res > 0) {
            // 清除缓存
            Integer loginNum = redisCache.getCacheObject(userName);
            if (loginNum != null && loginNum > 0) {
                redisCache.deleteObject(userName);
            }
            // 清除数据库锁定
            SysUser sysUser = userMapper.selectUserByUserName(userName);
            if (!"0".equals(sysUser.getStatus())) {
                sysUser.setStatus("0");
            }
            // 设置过期时间
            sysUser.setValidity(this.validityTime());
            userMapper.updateUser(sysUser);
        }
        return res;
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user) {
        String[] roles = user.getRoleIds();
        if (StringUtils.isNotNull(roles)) {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (String roleId : roles) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(user.getUserId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0) {
                userRoleMapper.batchUserRole(list);
            }
        }
    }

    /**
     * 新增用户岗位信息
     *
     * @param user 用户对象
     */
    public void insertUserPost(SysUser user) {
        String[] posts = user.getPostIds();
        if (StringUtils.isNotNull(posts)) {
            // 新增用户与岗位管理
            List<SysUserPost> list = new ArrayList<SysUserPost>();
            for (String postId : posts) {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }
            if (list.size() > 0) {
                userPostMapper.batchUserPost(list);
            }
        }
    }


    /**
     * 新增用户菜单信息
     *
     * @param user 用户对象
     */
    public void insertUserMenu(SysUser user) {
        String[] menus = user.getMenuIds();
        if (StringUtils.isNotNull(menus)) {
            // 新增用户与菜单管理
            List<SysUserMenu> list = new ArrayList<>();
            for (String menuId : menus) {
                SysUserMenu um = new SysUserMenu();
                um.setUserId(user.getUserId());
                um.setMenuId(menuId);
                list.add(um);
            }
            if (list.size() > 0) {
                userMenuMapper.batchUserMenu(list);
            }
        }
    }

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public int deleteUserById(String userId) {
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 删除用户与岗位表
        userPostMapper.deleteUserPostByUserId(userId);
        // 删除用户与菜单表
        userMenuMapper.deleteUserMenuByUserId(userId);
        return userMapper.deleteUserById(userId);
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    public int deleteUserByIds(String[] userIds) {
        for (String userId : userIds) {
            checkUserAllowed(new SysUser(userId));
        }
        return userMapper.deleteUserByIds(userIds);
    }

    /**
     * 通过角色ID查询用户集合
     *
     * @param roleId 角色ID
     * @return 用户集合
     */
    @Override
    public List<SysUser> selectUserListByRoleId(String roleId) {
        return userMapper.selectUserListByRoleId(roleId);
    }


    /**
     * 通过用户ID删除用户和角色关联
     *
     * @param userRole 用户角色信息
     * @return 结果
     */
    @Override
    public int deleteUserRoleInfo(SysUserRole userRole) {
        checkUserAllowed(new SysUser(userRole.getUserId()));
        return userRoleMapper.deleteUserRoleInfo(userRole);
    }

    /**
     * 通过岗位ID查询用户集合
     *
     * @param postId 角色ID
     * @return 用户集合
     */
    @Override
    public List<SysUser> selectUserListByPostId(String postId) {
        return userMapper.selectUserListByPostId(postId);
    }

    /**
     * 根据用户ID查组织机构
     *
     * @param userId 用户ID
     * @return 组织机构代码
     */
    @Override
    public String getDeptPermission(String userId) {
        return userMapper.getDeptPermission(userId);
    }

    @Override
    public AjaxResult changePwd(String userName, String nickName, String newPassword, String oldPassword, String code, String uuid) {
        SysUser sysUser = userMapper.selectUserByUserName(userName);
        if (sysUser.isAdmin()) {
            throw new CustomException("系统管理员不给予修改！");
        }
        if (!SecurityUtils.matchesPassword(oldPassword, sysUser.getPassword())) {
            return AjaxResult.error("修改密码失败，旧密码错误！");
        }
        if (SecurityUtils.matchesPassword(newPassword, sysUser.getPassword())) {
            return AjaxResult.error("新密码不能与旧密码相同！");
        }
        // 验证码
        Integer loginNum = redisCache.getCacheObject(userName);
        if (StringUtils.isNotEmpty(sysUser.getUserName()) && !sysUser.getNickName().equals(nickName)) {
            loginNum = loginNum == null ? 1 : loginNum + 1;
            if (loginNum >= 1 && loginNum < 3) {
                redisCache.setCacheObject(userName, loginNum);
            }
            if (loginNum >= 3 && loginNum < 5) {
                redisCache.setCacheObject(userName, loginNum);
                return AjaxResult.error("修改密码失败，用户姓名错误，五次以上将锁定账号5分钟！", Constants.SHOW_CHECK);
            }
            if (loginNum == 5) {
                redisCache.setCacheObject(userName, loginNum, 5, MINUTES);
                return AjaxResult.error("账号：" + userName + "已锁定，请5分钟后再试！", Constants.SHOW_CHECK);
            }
            if (loginNum > 5) {
                long expire = redisUtil.getExpire(userName);
                return AjaxResult.error("账号：" + userName + "已锁定，请" + expire + "秒后再试！", Constants.SHOW_CHECK);
            }
            return AjaxResult.error("修改密码失败，用户姓名错误，五次以上将锁定账号5分钟！");
        }
        if (StringUtils.isNotEmpty(code) || (loginNum != null && loginNum >= 3) || StringUtils.isNotEmpty(uuid)) {
            if (StringUtils.isEmpty(code)) {
                return AjaxResult.error("请输入验证码！", Constants.SHOW_CHECK);
            }
            String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
            String captcha = redisCache.getCacheObject(verifyKey);
            redisCache.deleteObject(verifyKey);
            if (captcha == null) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
                return AjaxResult.error("验证码错误！", Constants.SHOW_CHECK);
            }
            if (!code.equalsIgnoreCase(captcha)) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
                return AjaxResult.error("验证码错误！", Constants.SHOW_CHECK);
            }
        }
        if (loginNum != null && loginNum >= 5) {
            long expire = redisUtil.getExpire(userName);
            return AjaxResult.error("账号：" + userName + "已锁定，请" + expire + "秒后再试！", Constants.SHOW_CHECK);
        }
        return this.resetUserPwd(userName, newPassword) > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 设置密码过期时间
     *
     * @return 时间
     */
    public DateTime validityTime() {
        // 查询密码过期类型，设置过期时间
        String validity = sysParameterMapper.selectSysParameter().getExpExtend2();
        DateTime date = DateUtil.date();
        DateTime validityTime = null;
        switch (validity) {
            case "1":
                validityTime = DateUtil.offsetMonth(date, 1);
                break;
            case "2":
                validityTime = DateUtil.offsetMonth(date, 3);
                break;
            case "3":
                validityTime = DateUtil.offsetMonth(date, 6);
                break;
            case "0":
                validityTime = DateUtil.parse("2099-12-31");
                break;
            default:
                throw new CustomException("未定义的密码过期规则");
        }
        return validityTime;
    }

}
