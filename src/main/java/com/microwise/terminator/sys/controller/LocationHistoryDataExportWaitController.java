package com.microwise.terminator.sys.controller;

import com.microwise.terminator.sys.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * 导出历史数据等待Controller
 *
 * @author bai.weixing
 * @since 2017/10/11.
 */
@RequestMapping("/viewExport")
@Controller
public class LocationHistoryDataExportWaitController {

    @Autowired
    private LocationService locationService;

    @GetMapping
    public String viewExport(@RequestParam String locationId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  Date beginTime,
                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  Date endTime, Model model) {
        model.addAttribute("count", locationService.findHistoryDataListCount(locationId, beginTime, endTime));
        model.addAttribute("beginTime", beginTime);
        model.addAttribute("endTime", endTime);
        model.addAttribute("locationId", locationId);
        return "locationHistoryData/export";
    }
}
