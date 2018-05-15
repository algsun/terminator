package com.microwise.terminator.sys.controller;

import com.github.pagehelper.PageInfo;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.microwise.terminator.common.utils.DateUtils;
import com.microwise.terminator.sys.entity.Location;
import com.microwise.terminator.sys.entity.RecentDataVO;
import com.microwise.terminator.sys.entity.Sensorinfo;
import com.microwise.terminator.sys.service.LocationService;
import com.microwise.terminator.sys.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 位置点历史数据
 *
 * @author bai.weixing
 * @since 2017/9/29.
 */
@RequestMapping("/locationHistoryData")
@Controller
public class LocationHistoryDataController {

    @Autowired
    private LocationService locationService;

    @GetMapping
    public String index(@RequestParam(defaultValue = "1") int pageNumber,
                        @RequestParam(defaultValue = "20") int pageSize,
                        @RequestParam(value = "beginTime", required = false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")  Date beginTime,
                        @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date endTime,
                        @RequestParam(value = "relicId") String relicId,
                        @RequestParam(value = "locationId", required = false) String locationId,
                        Model model) {
        Location location = new Location();
        location.setZoneid(relicId);
        List<Location> locations = locationService.findList(location);
        model.addAttribute("locations", locations);
        if(locations.size()<1) {
            return "locationHistoryData/index";
        }
        if (beginTime == null) {
            beginTime = DateUtils.getDayBegin();
        }
        if (endTime == null) {
            endTime = DateUtils.getDayEnd();
        }
        if(locationId == null && locations.size()>0) {
            locationId = locations.get(0).getId();
        }
        model.addAttribute("beginTime", beginTime);
        model.addAttribute("endTime", endTime);
        model.addAttribute("locationId", locationId);
        model.addAttribute("relicId", relicId);
        queryData(locationId, beginTime, endTime, model, pageNumber, pageSize);
        return "locationHistoryData/index";
    }


    private void queryData(String locationId, Date startDate, Date endDate, Model model, int pageNumber, int pageSize) {
        Location location = locationService.findLocationById(locationId);
        List<Sensorinfo> sensorinfos = location.getSensorinfoList();
        Map<Integer, Sensorinfo> sensorinfoMap = Maps.uniqueIndex(sensorinfos, new Function<Sensorinfo, Integer>() {
            @Override
            public Integer apply(Sensorinfo sensorinfo) {
                return sensorinfo.getSensorphysicalid();
            }
        });

        PageInfo<RecentDataVO> pageInfo = locationService.findHistoryDataList(locationId, startDate, endDate, pageNumber, pageSize);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("sensorinfos", sensorinfos);
        model.addAttribute("sensorinfoMap", sensorinfoMap);
    }
}
