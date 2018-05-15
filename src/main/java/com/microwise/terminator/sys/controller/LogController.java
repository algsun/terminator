package com.microwise.terminator.sys.controller;

import com.github.pagehelper.PageInfo;
import com.microwise.terminator.common.config.Global;
import com.microwise.terminator.common.utils.DateUtils;
import com.microwise.terminator.common.utils.StringUtils;
import com.microwise.terminator.sys.entity.Log;
import com.microwise.terminator.sys.entity.User;
import com.microwise.terminator.sys.service.LogService;
import com.microwise.terminator.sys.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 日志管理
 *
 * @author li.jianfei
 * @since 2017/9/5
 */
@RequestMapping("/sys/logs")
@Controller
public class LogController {

    private static final Logger logger = LoggerFactory.getLogger(LogController.class);

    @Autowired
    private LogService logService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String index(Model model, Log log, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "20") int pageSize) {
        try {
            if (log.getBeginDate() == null) {
                log.setBeginDate(DateUtils.setDays(DateUtils.parseDate(DateUtils.getDate()), 1));
            }
            if (log.getEndDate() == null) {
                log.setEndDate(new Date());
            }
            List<Log> logs = new ArrayList<>();
            if (StringUtils.isNotBlank(log.getCreateByName())) {
                List<User> users = userService.findUsersByName(log.getCreateByName());
                if (users != null && users.size() != 0) {
                    for (User user : users) {
                        log.setCreateBy(user.getId());
                        logs.add(log);
                    }
                } else {
                    model.addAttribute("log", log);
                    model.addAttribute(new PageInfo<Log>());
                    model.addAttribute(Global.PAGE_PATH, "log/index");
                    return "index";
                }

            } else {
                logs.add(log);
            }
            model.addAttribute("log", log);
            model.addAttribute("pageInfo", logService.findLogs(logs, pageNum, pageSize));
            model.addAttribute(Global.PAGE_PATH, "log/index");
        } catch (Exception e) {
            logger.error("未检索到日志数据", e);
            model.addAttribute("success", false);
            model.addAttribute("message", "未检索到日志数据");
        }

        return "index";
    }
}
