package com.microwise.terminator.sys.controller;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.microwise.terminator.common.security.shiro.session.SessionDAO;
import com.microwise.terminator.common.servlet.ValidateCodeServlet;
import com.microwise.terminator.common.utils.CacheUtils;
import com.microwise.terminator.common.utils.IdGen;
import com.microwise.terminator.common.utils.StringUtils;
import com.microwise.terminator.sys.entity.AlarmRecord;
import com.microwise.terminator.sys.entity.Menu;
import com.microwise.terminator.sys.entity.User;
import com.microwise.terminator.sys.service.AlarmHistoryService;
import com.microwise.terminator.sys.service.AlarmTaskService;
import com.microwise.terminator.sys.shiro.FormAuthenticationFilter;
import com.microwise.terminator.sys.shiro.SystemAuthorizingRealm.Principal;
import com.microwise.terminator.sys.util.Constants;
import com.microwise.terminator.sys.util.UserUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by lijianfei on 17/3/3.
 *
 * @author li.jianfei
 * @since 17/3/3
 */
@Controller
@SessionAttributes(value = {Constants.Session.MENUS, Constants.Session.CURRENT_SYSTEM,
        Constants.Session.CURRENT_MENU, Constants.Session.ALARM_TASK_COUNT, Constants.Session.TATTLETALE_URL, Constants.Session.CURRENT_USER})
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private SessionDAO sessionDAO;

    @Autowired
    private AlarmTaskService alarmTaskService;

    @Autowired
    private AlarmHistoryService alarmHistoryService;

    @GetMapping("/login")
    public String loginTemplate() {
        return "login";
    }

    @Value("${tattletaleURL}")
    private String tattletaleURL;


    /**
     * 登录失败，真正登录的POST请求由Filter完成
     */
    @PostMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
        Principal principal = UserUtils.getPrincipal();

        // 如果已经登录，则跳转到管理首页
        if (principal != null) {
            return "redirect: index";
        }

        String username = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
        boolean rememberMe = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM);
        boolean mobile = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_MOBILE_PARAM);
        String exception = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        String message = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM);

        if (StringUtils.isBlank(message) || StringUtils.equals(message, "null")) {
            message = "用户或密码错误, 请重试.";
        }

        model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
        model.addAttribute(FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM, rememberMe);
        model.addAttribute(FormAuthenticationFilter.DEFAULT_MOBILE_PARAM, mobile);
        model.addAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, exception);
        model.addAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, message);

        if (logger.isDebugEnabled()) {
            logger.debug("login fail, active session size: {}, message: {}, exception: {}",
                    sessionDAO.getActiveSessions(false).size(), message, exception);
        }

        // 非授权异常，登录失败，验证码加1。
        if (!UnauthorizedException.class.getName().equals(exception)) {
            model.addAttribute("isValidateCodeLogin", isValidateCodeLogin(username, true, false));
        }

        // 验证失败清空验证码
        request.getSession().setAttribute(ValidateCodeServlet.VALIDATE_CODE, IdGen.uuid());

        return "login";
    }


    /**
     * 登录成功，进入管理首页
     */
//    @RequiresPermissions("user")
    @GetMapping("/")
    public String index(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes, Model model) {
        Principal principal = UserUtils.getPrincipal();

        // 登录成功后，验证码计算器清零
        isValidateCodeLogin(principal.getLoginName(), false, true);

        if (logger.isDebugEnabled()) {
            logger.debug("show index, active session size: {}", sessionDAO.getActiveSessions(false).size());
        }

        List<Menu> menus = UserUtils.getMenus();
        model.addAttribute(Constants.Session.MENUS, menus);
        model.addAttribute(Constants.Session.CURRENT_USER, UserUtils.getCurrentUser());
        //首次登陆默认选中的导航和按钮
        Menu firstSystem = null;
        Menu firstMenu = null;
        Menu menu = menus.get(0);
        if ("1".equals(menu.getParentId())) {
            firstSystem = menu;
            for (Menu m : menus) {
                if (menu.getId().equals(m.getParentId())) {
                    firstMenu = m;
                    break;
                }
            }
        } else {
            firstMenu = menu;
            for (Menu m : menus) {
                if (menu.getParentId().equals(m.getId())) {
                    firstSystem = m;
                }
            }
        }

        //我的报警任务
        User user = UserUtils.getCurrentUser();
        String notifier = user.getId();
        String officeId = user.getOfficeId();
        try {
            int alarmTaskCount = alarmTaskService.alarmTaskCount(officeId, notifier);
            PageInfo pageInfo = alarmTaskService.alarmTasks(officeId, notifier, 0, 0);
            model.addAttribute(Constants.Session.ALARM_TASK_COUNT, alarmTaskCount);
            List<AlarmRecord> alarmRecords = alarmHistoryService.buildLoginAlarm();
            redirectAttributes.addFlashAttribute("loginAlarm", alarmHistoryService.buildAlarmRecord(alarmRecords));
        } catch (Exception e) {
            model.addAttribute(Constants.Session.ALARM_TASK_COUNT, 0);
            logger.error("查询我的报警任务失败", e);
        }
        model.addAttribute(Constants.Session.CURRENT_SYSTEM, firstSystem.getId());
        model.addAttribute(Constants.Session.CURRENT_MENU, firstMenu);
        model.addAttribute(Constants.Session.TATTLETALE_URL, tattletaleURL);
        return "redirect:" + firstMenu.getHref();
    }


    @GetMapping("/403")
    public String unauthorizedRole() {
        logger.info("------没有权限-------");
        return "403";
    }

    @GetMapping("/user/edit/{userid}")
    public String getUserList(@PathVariable int userId) {
        logger.info("------进入用户信息修改-------");
        return "user_edit";
    }

    /**
     * 是否是验证码登录
     *
     * @param useruame 用户名
     * @param isFail   计数加1
     * @param clean    计数清零
     * @return
     */
    @SuppressWarnings("unchecked")
    public static boolean isValidateCodeLogin(String useruame, boolean isFail, boolean clean) {
        Map<String, Integer> loginFailMap = (Map<String, Integer>) CacheUtils.get("loginFailMap");
        if (loginFailMap == null) {
            loginFailMap = Maps.newHashMap();
            CacheUtils.put("loginFailMap", loginFailMap);
        }
        Integer loginFailNum = loginFailMap.get(useruame);
        if (loginFailNum == null) {
            loginFailNum = 0;
        }
        if (isFail) {
            loginFailNum++;
            loginFailMap.put(useruame, loginFailNum);
        }
        if (clean) {
            loginFailMap.remove(useruame);
        }
        return loginFailNum >= 3;
    }
}
