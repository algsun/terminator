package com.microwise.terminator.sys.controller;

import com.microwise.terminator.common.config.Global;
import com.microwise.terminator.sys.entity.Location;
import com.microwise.terminator.sys.service.DeviceService;
import com.microwise.terminator.sys.service.LocationService;
import com.microwise.terminator.sys.service.RelicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

/**
 * 位置点Controller
 *
 * @author sun.cong
 * @create 2017-09-25 10:53
 **/
@Controller
@RequestMapping("/sys/locations")
public class LocationController {
    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);
    @Autowired
    private LocationService locationService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private RelicService relicService;

    @GetMapping
    public String index(Model model, @ModelAttribute Location location, @RequestParam(defaultValue = "1") int pageNum,
                        @RequestParam(defaultValue = "20") int pageSize) {
        try {
            model.addAttribute("pageInfo", locationService.findLocationList(location, pageNum, pageSize));
            model.addAttribute(Global.PAGE_PATH, "location/index");
        } catch (Exception e) {
            logger.error("未检索到位置点数据", e);
        }
        return "index";
    }

    @GetMapping("new")
    public String create(Model model, Location location) {
        try {
            model.addAttribute("location", location);
            model.addAttribute("nodeList", deviceService.findUnbindDevices(null));
            model.addAttribute("relicList", relicService.findRelics(null, null));
            model.addAttribute(Global.PAGE_PATH, "location/new");
        } catch (Exception e) {
            logger.error("", e);
        }
        return "index";
    }

    @PostMapping("save")
    public String save(RedirectAttributes redirectAttributes, @Valid Location location, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addAttribute("success", false);
            redirectAttributes.addAttribute("message", "位置点添加失败");
            redirectAttributes.addFlashAttribute("location", location);
            return "redirect:/sys/locations/new";
        }
        try {
            locationService.save(location);
            locationService.notifyLocationChanged(location.getNodeid());
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "位置点添加成功");
        } catch (Exception e) {
            logger.error("添加位置点失败", e);
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "位置点添加失败");
            redirectAttributes.addFlashAttribute("location", location);
            return "redirect:/sys/locations/new";
        }
        return "redirect:/sys/locations";
    }

    @GetMapping("edit")
    public String edit(Model model, Location location) {
        try {
            model.addAttribute("location", locationService.findLocationById(location));
            model.addAttribute("nodeList", deviceService.findUnbindDevices(location));
            model.addAttribute("relicList", relicService.findRelics(null, null));
            model.addAttribute(Global.PAGE_PATH, "location/edit");
        } catch (Exception e) {
            logger.error("未检索到待修改数据", e);
        }
        return "index";
    }

    @PostMapping("update")
    public String update(Model model, RedirectAttributes redirectAttributes, @Valid Location location, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("success", false);
            model.addAttribute("message", "位置点修改失败");
            model.addAttribute(Global.PAGE_PATH, "location/edit");
            return "index";
        }
        try {
            List<String> deviceIds = locationService.updateLocation(location);
            for (String deviceId : deviceIds) {
                locationService.notifyLocationChanged(deviceId);
            }
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "位置点修改成功");
        } catch (Exception e) {
            logger.error("修改位置点失败", e);
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "位置点修改失败");
            redirectAttributes.addFlashAttribute("location", location);
            return "redirect:/sys/locations/edit";
        }
        return "redirect:/sys/locations";
    }

    @GetMapping("delete")
    public String delete(RedirectAttributes redirectAttributes, Location location) {
        try {
            String nodeId = locationService.deleteLocation(location);
            locationService.notifyLocationChanged(nodeId);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "位置点删除成功");
        } catch (Exception e) {
            logger.error("删除位置点失败", e);
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "位置点删除失败");
        }
        return "redirect:/sys/locations";
    }

    @PostMapping("exists")
    @ResponseBody
    public Boolean exists(String name, String id) {
        Boolean isNotExits = false;
        try {
            isNotExits = locationService.findLocationByName(name, id) == null;
        } catch (Exception e) {
            logger.error("位置点名称重复校验时异常", e);
        }
        return isNotExits;
    }
}
