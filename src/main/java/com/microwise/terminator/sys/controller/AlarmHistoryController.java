package com.microwise.terminator.sys.controller;

import com.microwise.terminator.common.config.Global;
import com.microwise.terminator.sys.service.AlarmHistoryService;
import com.microwise.terminator.sys.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * 报警记录Controller
 *
 * @author sun.cong
 * @create 2017-11-10 10:39
 **/
@Controller
@RequestMapping("analysis/alarmHistorys")
public class AlarmHistoryController {
    @Autowired
    private AlarmHistoryService alarmHistoryService;

    private static final Logger logger = LoggerFactory.getLogger(AlarmHistoryController.class);

    @RequestMapping
    public String index(Model model,
                        @RequestParam(required = false) String relicName,
                        @RequestParam(defaultValue = "-1") int state,
                        @DateTimeFormat(pattern = "yyyy-MM-dd") Date begin,
                        @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
                        @RequestParam(defaultValue = "1") int pageNum,
                        @RequestParam(defaultValue = "20") int pageSize) {
        try {
            if (begin == null) {
                begin = DateTimeUtil.startOfMonth(new Date());
            }
            if (end == null) {
                model.addAttribute("end", new Date());
                end = DateTimeUtil.getTomorrow(new Date());
            } else {
                model.addAttribute("end", end);
                end = DateTimeUtil.getTomorrow(end);
            }
            model.addAttribute("pageInfo",
                    alarmHistoryService.findAlarmHistoryList(relicName, state, begin, end, pageNum, pageSize));
            model.addAttribute("relicName", relicName);
            model.addAttribute("state", state);
            model.addAttribute("begin", begin);
            model.addAttribute(Global.PAGE_PATH, "alarmHistory/index");
        } catch (Exception e) {
            logger.error("检索报警历史记录失败", e);
        }
        return "index";
    }
}
