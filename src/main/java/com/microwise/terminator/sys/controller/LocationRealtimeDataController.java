package com.microwise.terminator.sys.controller;

import com.microwise.terminator.sys.entity.RealtimeDataVO;
import com.microwise.terminator.sys.entity.Sensorinfo;
import com.microwise.terminator.sys.service.LocationService;
import com.microwise.terminator.sys.service.RelicService;
import com.microwise.terminator.sys.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 位置点实时数据
 *
 * @author bai.weixing
 * @since 2017/10/11.
 */
@RequestMapping("/locationRealtimeData")
@Controller
public class LocationRealtimeDataController {

    @Autowired
    private LocationService locationService;

    @Autowired
    private RelicService relicService;

    @RequestMapping
    public String index(@RequestParam String relicId, Model model) {

        List<Sensorinfo> sensorinfoes = relicService.findSensorinfo(relicId);
        List<RealtimeDataVO> data = relicService.findRealtimeDataLocation(relicId);
        model.addAttribute("relicId", relicId);
        model.addAttribute("sensorinfoes", sensorinfoes);
        model.addAttribute("data", data);
        return "locationRealtimeData/index";
    }

    @RequestMapping("/relic/{relicId}/realtime-data.json")
    @ResponseBody
    public List<RealtimeDataVO> realtimeDataJson(@PathVariable String relicId) {
        return relicService.findRealtimeDataLocation(relicId);
    }

}
