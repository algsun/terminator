package com.microwise.terminator.sys.controller;

import com.github.pagehelper.PageInfo;
import com.microwise.terminator.sys.entity.AlarmRecord;
import com.microwise.terminator.sys.entity.Office;
import com.microwise.terminator.sys.entity.User;
import com.microwise.terminator.sys.service.AlarmTaskService;
import com.microwise.terminator.sys.service.OfficeService;
import com.microwise.terminator.sys.util.Constants;
import com.microwise.terminator.sys.util.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * 报警任务controller
 *
 * @author bai.weixing
 * @since 2017/11/27.
 */
@RequestMapping("/alarmTask")
@Controller
public class AlarmTaskController {

    private static final Logger logger = LoggerFactory.getLogger(AlarmTaskController.class);

    @Autowired
    private AlarmTaskService alarmTaskService;

    @Autowired
    private OfficeService officeService;

    @GetMapping("/index")
    public String index(@RequestParam(defaultValue = "1") Integer pageNumber, HttpSession session,
                        @RequestParam(defaultValue = "20") Integer pageSize, Model model) {
        try {
            User user = UserUtils.getCurrentUser();
            String officeId = user.getOfficeId();
            Office office = officeService.findOffice(Integer.parseInt(officeId));
            PageInfo pageInfo = alarmTaskService.alarmTasks(officeId, user.getId(), pageNumber, pageSize);
            model.addAttribute("pageInfo", pageInfo);
            model.addAttribute(Constants.PAGE_PATH, "alarmTask/index");
            model.addAttribute("officeName", office.getOfficeName());
        } catch (Exception e) {
            logger.error("查询报警任务失败", e);
        }
        return "index";
    }

    @GetMapping("/edit")
    public String edit(String id, Model model) {
        try {
            AlarmRecord alarmTask = alarmTaskService.alarmTask(id);
            model.addAttribute("alarmTask", alarmTask);
            model.addAttribute(Constants.PAGE_PATH, "alarmTask/edit");
        } catch (Exception e) {
            logger.error("跳转报警任务处理页面出错", e);
        }
        return "index";
    }

    @PostMapping("/update")
    public String update(AlarmRecord alarmRecord, RedirectAttributes redirectAttributes, HttpSession session) {
        try {
            alarmTaskService.update(alarmRecord);
            //更新session中我的报警任务
            String notifier = UserUtils.getCurrentUser().getId();
            String officeId = UserUtils.getCurrentUser().getOfficeId();
            int alarmTaskCount = alarmTaskService.alarmTaskCount(officeId, notifier);
            session.removeAttribute(Constants.Session.ALARM_TASK_COUNT);
            session.setAttribute(Constants.Session.ALARM_TASK_COUNT, alarmTaskCount);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "处理报警任务成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "处理报警任务失败");
            logger.error("处理报警任务失败", e);
        }
        return "redirect:/alarmTask/index";
    }

}
