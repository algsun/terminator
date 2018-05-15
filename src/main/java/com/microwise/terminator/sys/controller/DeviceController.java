package com.microwise.terminator.sys.controller;

import com.microwise.terminator.common.config.Global;
import com.microwise.terminator.sys.entity.Device;
import com.microwise.terminator.sys.entity.NodeInfo;
import com.microwise.terminator.sys.service.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 设备Controller
 *
 * @author sun.cong
 * @create 2017-09-20 14:38
 **/
@Controller
@RequestMapping("sys/devices")
public class DeviceController {
    private static final Logger logger = LoggerFactory.getLogger(LogController.class);

    @Autowired
    private DeviceService deviceService;

    @GetMapping
    public String index(Model model, @ModelAttribute NodeInfo nodeInfo, @RequestParam(defaultValue = "1") int pageNum,
                        @RequestParam(defaultValue = "20") int pageSize) {
        try {
            model.addAttribute("pageInfo", deviceService.findDeviceList(nodeInfo, pageNum, pageSize));
            model.addAttribute("exceptionDevices", deviceService.findAbnormalDevices());
            model.addAttribute(Global.PAGE_PATH, "device/index");
        } catch (Exception e) {
            logger.error("未检索到设备数据", e);
        }
        return "index";
    }

    @GetMapping("detail")
    public String findDeviceDetails(Model model, String nodeid) {
        try {
            model.addAttribute("device", deviceService.findDeviceDetails(new NodeInfo(nodeid)));
            model.addAttribute(Global.PAGE_PATH, "device/device-detail");
        } catch (Exception e) {
            logger.error("未检索到设备数据", e);
        }
        return "index";
    }

    @GetMapping("edit")
    public String edit(Model model, NodeInfo nodeInfo) {
        try {
            model.addAttribute("device", deviceService.findDeviceDetails(nodeInfo));
            model.addAttribute(Global.PAGE_PATH, "device/update");
        } catch (Exception e) {
            logger.error("查询待修改设备失败");
        }
        return "index";
    }

    @PostMapping("update")
    public String update(RedirectAttributes redirectAttributes, Device device) {
        try {
            if (deviceService.update(device)) {
                redirectAttributes.addFlashAttribute("success", true);
                redirectAttributes.addFlashAttribute("message", "修改设备成功");
            } else {
                logger.info("设备修改不成功");
                redirectAttributes.addFlashAttribute("success", false);
                redirectAttributes.addFlashAttribute("message", "修改设备失败");
            }
        } catch (Exception e) {
            logger.error("修改设备不成功", e);
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "修改设备失败");
        }
        return "redirect:/sys/devices/edit?nodeid=" + device.getNodeid();
    }

    @GetMapping("modifySensitivity")
    public String modifySensitivity(RedirectAttributes redirectAttributes, Device device) {
        try {
            if (deviceService.updateSensitivity(device)) {
                redirectAttributes.addFlashAttribute("success", true);
                redirectAttributes.addFlashAttribute("message", "修改灵敏度成功");
            } else {
                logger.info("修改灵敏度失败");
                redirectAttributes.addFlashAttribute("success", false);
                redirectAttributes.addFlashAttribute("message", "修改灵敏度失败");
            }
        } catch (Exception e) {
            logger.error("修改灵敏度失败", e);
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "修改灵敏度失败");
        }
        return "redirect:/sys/devices/edit?nodeid=" + device.getNodeid();
    }

    @GetMapping("delete")
    public String delete(RedirectAttributes redirectAttributes, String nodeid) {
        try {
            if (deviceService.delete(nodeid)) {
                redirectAttributes.addFlashAttribute("success", true);
                redirectAttributes.addFlashAttribute("message", "设备删除成功");
            } else {
                logger.info("设备删除失败");
                redirectAttributes.addFlashAttribute("success", false);
                redirectAttributes.addFlashAttribute("message", "设备删除失败");
            }
        } catch (Exception e) {
            logger.error("删除设备失败", e);
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "设备删除失败");
        }
        return "redirect:/sys/devices";
    }
}
