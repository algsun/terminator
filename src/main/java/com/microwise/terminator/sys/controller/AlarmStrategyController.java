package com.microwise.terminator.sys.controller;

import com.microwise.terminator.sys.entity.AlarmStrategy;
import com.microwise.terminator.sys.entity.Sensorinfo;
import com.microwise.terminator.sys.service.AlarmStrategyService;
import com.microwise.terminator.sys.service.RelicService;
import com.microwise.terminator.sys.service.UserService;
import com.microwise.terminator.sys.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * 报警策略controller
 *
 * @author bai.weixing
 * @since 2017/11/9.
 */
@RequestMapping("/alarmStrategy")
@Controller
public class AlarmStrategyController {

    private static final Logger logger = LoggerFactory.getLogger(AlarmStrategyController.class);

    @Autowired
    private RelicService relicService;

    @Autowired
    private AlarmStrategyService alarmStrategyService;

    @Autowired
    private UserService userService;

    @GetMapping("/{relicId}/index")
    public String index(@PathVariable String relicId, Model model) {
        try {
            model.addAttribute("alarmStrategys", alarmStrategyService.alarmStrategies(relicId));
            model.addAttribute("users", userService.findUsers());
            model.addAttribute("sensorinfos", relicService.findSensorinfo(relicId));
            model.addAttribute("relic", relicService.findById(relicId));
            model.addAttribute(Constants.PAGE_PATH, "alarmStrategy/index");
        } catch (Exception e) {
            model.addAttribute("success", false);
            model.addAttribute("message", "查询报警策略失败");
            logger.error("查询报警策略失败", e);
        }
        return "index";
    }


    @PostMapping("/{relicId}/new")
    public String create(@PathVariable String relicId, Model model) {
        List<Sensorinfo> sensorinfos = relicService.findSensorinfo(relicId);
        model.addAttribute("sensorinfos", sensorinfos);
        model.addAttribute(Constants.PAGE_PATH, "alarmStrategy/new");
        return "index";
    }

    @PostMapping("/save")
    public String save(AlarmStrategy alarmStrategy, RedirectAttributes redirectAttributes) {
        try {
            alarmStrategyService.save(alarmStrategy);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "添加报警策略成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "添加报警策略失败");
            logger.error("添加报警策略失败", e);
        }
        return "redirect:/alarmStrategy/" + alarmStrategy.getAlarmpointid() + "/index";
    }

    @GetMapping("/delete/{relicId}/{id}")
    public String delete(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            alarmStrategyService.delete(id);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "删除报警策略成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "删除报警策略失败");
            logger.error("删除报警策略失败", e);
        }
        return "redirect:/alarmStrategy/{relicId}/index";
    }


    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        try {
            AlarmStrategy alarmStrategy = alarmStrategyService.getAlarmStrategy(id);
            model.addAttribute("users", userService.findUsers());
            model.addAttribute("alarmstrategy", alarmStrategy);
            model.addAttribute("sensorinfos", alarmStrategy.getAlarmthresholds());
            model.addAttribute(Constants.PAGE_PATH, "alarmStrategy/edit");
        } catch (Exception e) {
            model.addAttribute("success", false);
            model.addAttribute("message", "查询报警策略失败");
            logger.error("查询报警策略失败", e);
        }

        return "index";
    }


    @PostMapping("/update")
    public String update(AlarmStrategy alarmStrategy, RedirectAttributes redirectAttributes) {
        try {
            alarmStrategyService.update(alarmStrategy);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "更新报警策略成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "更新报警策略失败");
            logger.error("更新报警策略失败", e);
        }
        return "redirect:/alarmStrategy/" + alarmStrategy.getAlarmpointid() + "/index";
    }
}
