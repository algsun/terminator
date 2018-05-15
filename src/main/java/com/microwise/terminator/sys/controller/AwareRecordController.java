package com.microwise.terminator.sys.controller;

import com.microwise.terminator.common.config.Global;
import com.microwise.terminator.sys.service.AwareRecordService;
import com.microwise.terminator.sys.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * 知晓记录Controller
 *
 * @author sun.cong
 * @create 2017-11-10 10:39
 **/
@Controller
@RequestMapping("/analysis/awareRecords")
public class AwareRecordController {

    private Logger logger = LoggerFactory.getLogger(AlarmHistoryController.class);

    @Autowired
    private AwareRecordService awareRecordService;

    @GetMapping("/save")
    @ResponseBody
    public Boolean save() {
        try {
            if (awareRecordService.save()) {
                return true;
            }
        } catch (Exception e) {
            logger.error("添加知晓记录失败", e);
        }
        return false;
    }

    @RequestMapping
    public String index(Model model,
                        @RequestParam(required = false) String relicName,
                        @RequestParam(required = false) String user,
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
                    awareRecordService.findAwareRecords(relicName, user, begin, end, pageNum, pageSize));
            model.addAttribute("relicName", relicName);
            model.addAttribute("user", user);
            model.addAttribute("begin", begin);
            model.addAttribute(Global.PAGE_PATH, "awareRecord/index");
        } catch (Exception e) {
            logger.error("查询知晓记录失败", e);
        }
        return "index";
    }
}
