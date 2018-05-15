package com.microwise.terminator.sys.controller;

import com.microwise.terminator.common.config.Global;
import com.microwise.terminator.sys.entity.User;
import com.microwise.terminator.sys.service.AnalysisService;
import com.microwise.terminator.sys.service.UserService;
import com.microwise.terminator.sys.util.DateTimeUtil;
import com.microwise.terminator.sys.util.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;

/**
 * 统计分析Controller
 *
 * @author sun.cong
 * @create 2017-11-21 11:16
 **/
@Controller
@RequestMapping("/analysis/daily")
public class AnalysisController {
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private UserService userService;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private AnalysisService analysisService;

    private static final Logger logger = LoggerFactory.getLogger(AnalysisController.class);

    /**
     * 报警记录页面
     *
     * @param model
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping
    public String index(Model model, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "8") int pageSize) {
        try {
            model.addAttribute("dailyReport", analysisService.getDailyReport());
            model.addAttribute("pending", analysisService.getAlarmRecords(0));
            model.addAttribute("processed", analysisService.getAlarmRecords(1));
            model.addAttribute("devices", analysisService.findDeviceDetail(1));
            model.addAttribute("currentUser", UserUtils.getCurrentUser());
            model.addAttribute(Global.PAGE_PATH, "analysis/index");
        } catch (Exception e) {
            logger.error("检索日报数据时异常", e);
        }
        return "index";
    }

    /**
     * 订阅日报/放弃订阅
     *
     * @param follower
     * @return
     */
    @ResponseBody
    @GetMapping("/subscribe")
    public Boolean subscribe(@RequestParam(value = "follower") Integer follower) {
        try {
            User user = UserUtils.getCurrentUser();
            if (follower == 0) {
                user.setAttention(1);
                userService.update(user);
            } else if (follower == 1) {
                user.setAttention(0);
                userService.update(user);
            }
            return true;
        } catch (Exception e) {
            logger.error("关注失败", e);
        }
        return false;
    }

    /**
     * 每天中午8点生成前一天日报
     */
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendEmail() {
        try {
            Context context = new Context();
            List<User> users = userService.findUsersByAttention(1);
            for (User user : users) {
                context.setVariable("name", user.getName());
                context.setVariable("dailyReport", analysisService.getDailyReport());
                context.setVariable("pending", analysisService.getAlarmRecords(0));
                context.setVariable("processed", analysisService.getAlarmRecords(1));
                context.setVariable("devices", analysisService.findDeviceDetail(1));
                String text = templateEngine.process("email/email-daily", context);
                prepareAndSend(user.getEmail(), text);
            }
        } catch (Exception e) {
            logger.error("日报邮件发送失败", e);
        }
    }

    /**
     * 发送邮件
     *
     * @param recipient
     * @param text
     * @throws Exception
     */
    private void prepareAndSend(String recipient, String text) throws Exception {
        String today = DateTimeUtil.format("yyyyMMdd", new Date());
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setFrom("no-reply-terminator@microwise-system.com");
        helper.setTo(recipient);
        helper.setSubject(today + "报警日报");
        helper.setText(text, true);
        javaMailSender.send(message);
    }
}
