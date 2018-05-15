package com.microwise.terminator.sys.controller;

import com.google.common.collect.Lists;
import com.microwise.terminator.sys.entity.RealtimeDataVO;
import com.microwise.terminator.sys.entity.Relic;
import com.microwise.terminator.sys.service.RelicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 概览Controller
 *
 * @author sun.cong
 * @create 2017-09-29 11:26
 **/
@Controller
@RequestMapping("/sys/overviews")
public class OverviewController {
    private static final Logger logger = LoggerFactory.getLogger(OverviewController.class);

    @Autowired
    private RelicService relicService;

    @RequestMapping
    public String index(Model model, String name) {
        try {
            List<Relic> relics = relicService.getRelicAndMonitorData(name);
            model.addAttribute("relics", relics);
            model.addAttribute("name", name);
        } catch (Exception e) {
            logger.error("", e);
        }
        return "overview/index";
    }

    @RequestMapping("/monitor-data")
    public String getMonitorData(Model model, String name, String relicId) {
        try {
            List<Relic> relics = relicService.getRelicAndMonitorData(name);
            model.addAttribute("relics", relics);
            model.addAttribute("name", name);
            if (relics != null && relicId == null) {
                relicId = relics.get(0).getId();
            }
            model.addAttribute("relicId", relicId);
        } catch (Exception e) {
            logger.error("", e);
        }
        return "overview/real-time-state";
    }

    @GetMapping("getRelicRealTimeData")
    @ResponseBody
    public List<RealtimeDataVO> getRelicRealTimeData(String relicId) {
        List<RealtimeDataVO> realtimeDataVOList = Lists.newArrayList();
        try {
            realtimeDataVOList = relicService.findRealtimeDataLocation(relicId);
        } catch (Exception e) {
            logger.error("检索实时数据时异常", e);
        }
        return realtimeDataVOList;
    }
}
