/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.microwise.terminator.sys.util;


import com.microwise.terminator.common.SpringContextHolder;
import com.microwise.terminator.common.utils.CacheUtils;
import com.microwise.terminator.sys.entity.Menu;
import com.microwise.terminator.sys.entity.Office;
import com.microwise.terminator.sys.entity.Role;
import com.microwise.terminator.sys.entity.User;
import com.microwise.terminator.sys.mapper.MenuMapper;
import com.microwise.terminator.sys.mapper.OfficeMapper;
import com.microwise.terminator.sys.mapper.RoleMapper;
import com.microwise.terminator.sys.mapper.UserMapper;
import com.microwise.terminator.sys.shiro.SystemAuthorizingRealm.Principal;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import java.util.List;

/**
 * 用户工具类
 *
 * @author ThinkGem
 * @version 2013-12-05
 */
public class UserUtils {

    private static UserMapper userMapper = SpringContextHolder.getBean(UserMapper.class);
    private static RoleMapper roleMapper = SpringContextHolder.getBean(RoleMapper.class);
    private static MenuMapper menuMapper = SpringContextHolder.getBean(MenuMapper.class);
    private static OfficeMapper officeMapper = SpringContextHolder.getBean(OfficeMapper.class);

    public static final String USER_CACHE = "userCache";
    public static final String USER_CACHE_ID_ = "id_";
    public static final String USER_CACHE_LOGIN_NAME_ = "ln";
    public static final String USER_CACHE_LIST_BY_OFFICE_ID_ = "oid_";

    public static final String CACHE_AUTH_INFO = "authInfo";
    public static final String CACHE_ROLE_LIST = "roleList";
    public static final String CACHE_MENU_LIST = "menuList";
    public static final String CACHE_AREA_LIST = "areaList";
    public static final String CACHE_OFFICE_LIST = "officeList";
    public static final String CACHE_OFFICE_ALL_LIST = "officeAllList";

    /**
     * 根据ID获取用户
     *
     * @param id
     * @return 取不到返回null
     */
    public static User get(String id) {
        User user = (User) CacheUtils.get(USER_CACHE, USER_CACHE_ID_ + id);
        if (user == null) {
            user = userMapper.selectByPrimaryKey(id);
            if (user == null) {
                return null;
            }
            user.setRoles(roleMapper.findRoles(user.getId()));
            CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
            CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
        }
        return user;
    }

    /**
     * 根据登录名获取用户
     *
     * @param loginName
     * @return 取不到返回null
     */
    public static User getByLoginName(String loginName) {
        User user = (User) CacheUtils.get(USER_CACHE, USER_CACHE_LOGIN_NAME_ + loginName);
        if (user == null) {
            user = userMapper.selectOne(new User(loginName));
            if (user == null) {
                return null;
            }
            user.setRoles(roleMapper.findRoles(user.getId()));
            CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
            CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
        }
        return user;
    }

    /**
     * 清除当前用户缓存
     */
    public static void clearCache() {
        removeCache(CACHE_AUTH_INFO);
        removeCache(CACHE_ROLE_LIST);
        removeCache(CACHE_MENU_LIST);
        removeCache(CACHE_AREA_LIST);
        removeCache(CACHE_OFFICE_LIST);
        removeCache(CACHE_OFFICE_ALL_LIST);
        UserUtils.clearCache(getCurrentUser());
    }

    /**
     * 清除指定用户缓存
     *
     * @param user
     */
    public static void clearCache(User user) {
        CacheUtils.remove(USER_CACHE, USER_CACHE_ID_ + user.getId());
        CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName());
//        CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getOldLoginName());
    }

    /**
     * 获取当前用户
     *
     * @return 取不到返回 new User()
     */
    public static User getCurrentUser() {
        Principal principal = getPrincipal();
        if (principal != null) {
            User user = get(principal.getId());
            if (user != null) {
                return user;
            }
            return new User();
        }
        // 如果没有登录，则返回实例化空的User对象。
        return new User();
    }

    /**
     * 获取当前用户角色列表
     *
     * @return
     */
    public static List<Role> getRoles() {
        @SuppressWarnings("unchecked")
        List<Role> roles = (List<Role>) getCache(CACHE_ROLE_LIST);
        if (roles == null) {
            User user = getCurrentUser();
            if (user.isAdmin()) {
                roles = roleMapper.findAll();
            } else {
                roles = roleMapper.findRoles(user.getId());
            }
//            putCache(CACHE_ROLE_LIST, roles);
        }
        return roles;
    }

    /**
     * 获取当前用户授权菜单
     *
     * @return
     */
    public static List<Menu> getMenus() {
        @SuppressWarnings("unchecked")
        List<Menu> menus = (List<Menu>) getCache(CACHE_MENU_LIST);
        if (menus == null) {
            User user = getCurrentUser();
            if (user.isAdmin()) {
                menus = menuMapper.findAllList();
            } else {
                menus = menuMapper.findByUserId(user.getId());
            }
            putCache(CACHE_MENU_LIST, menus);
        }
        return menus;
    }

    /**
     * 获取授权主要对象
     */
    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    /**
     * 获取当前登录者对象
     */
    public static Principal getPrincipal() {
        try {
            Subject subject = SecurityUtils.getSubject();
            Principal principal = (Principal) subject.getPrincipal();
            if (principal != null) {
                return principal;
            }
        } catch (UnavailableSecurityManagerException | InvalidSessionException ignored) {

        }
        return null;
    }

    public static Session getSession() {
        try {
            Subject subject = SecurityUtils.getSubject();
            Session session = subject.getSession(false);
            if (session == null) {
                session = subject.getSession();
            }
            if (session != null) {
                return session;
            }
//			subject.logout();
        } catch (InvalidSessionException e) {

        }
        return null;
    }

    // ============== User Cache ==============
    public static Object getCache(String key) {
        return getCache(key, null);
    }

    public static Object getCache(String key, Object defaultValue) {
        Object obj = getSession().getAttribute(key);
        return obj == null ? defaultValue : obj;
    }

    public static void putCache(String key, Object value) {
        getSession().setAttribute(key, value);
    }

    public static void removeCache(String key) {
        getSession().removeAttribute(key);
    }


}
