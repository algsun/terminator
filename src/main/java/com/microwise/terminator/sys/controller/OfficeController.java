package com.microwise.terminator.sys.controller;

import com.microwise.terminator.common.config.Global;
import com.microwise.terminator.sys.entity.Office;
import com.microwise.terminator.sys.service.OfficeService;
import com.microwise.terminator.sys.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


/**
 * 机构controller
 *
 * @author sun.cong
 * @since 2017/8/25
 */
@RequestMapping("/sys/offices")
@Controller
public class OfficeController {
    private static final Logger logger = LoggerFactory.getLogger(OfficeController.class);

    @Autowired
    private OfficeService officeService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String index(Model model, @RequestParam(defaultValue = "1") int pageNum,
                        @RequestParam(defaultValue = "20") int pageSize) {
        try {
            model.addAttribute("pageInfo", officeService.findOffices(false, pageNum, pageSize));
            model.addAttribute("users", userService.findUsers());
            model.addAttribute(Global.PAGE_PATH, "office/index");
        } catch (Exception e) {
            logger.error("未检索出机构数据", e);
            model.addAttribute("success", false);
            model.addAttribute("message", "未检索到机构数据");
        }
        return "index";
    }

    @GetMapping("/new")
    public String create(Model model, Office office) {
        model.addAttribute("office", office);
        model.addAttribute(Global.PAGE_PATH, "office/new");
        return "index";
    }

    @PostMapping("/save")
    public String save(RedirectAttributes redirectAttributes, @Valid Office office,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("office", office);
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "机构添加失败");
            return "redirect:/sys/offices/new";
        }
        try {
            office.preInsert(office);
            officeService.insert(office);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "机构添加成功");
        } catch (Exception e) {
            logger.error("添加机构不成功", e);
            redirectAttributes.addFlashAttribute("office", office);
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "机构添加失败");
            return "redirect:/sys/offices/new";

        }
        return "redirect:/sys/offices";
    }

    @GetMapping("/edit")
    public String edit(Model model, Integer id) {
        model.addAttribute("office", officeService.findOffice(id));
        model.addAttribute(Global.PAGE_PATH, "office/edit");
        return "index";
    }

    @PostMapping("/update")
    public String update(RedirectAttributes redirectAttributes, @Valid Office office,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "机构修改失败");
            return "redirect:/sys/offices/edit";
        }
        try {
            officeService.update(office);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "机构修改成功");
        } catch (Exception e) {
            logger.error("修改机构失败", e);
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "机构修改失败");
            return "redirect:/sys/offices/edit";
        }
        return "redirect:/sys/offices";
    }

    @GetMapping("/delete")
    public String delete(RedirectAttributes redirectAttributes, Integer id) {
        try {
            officeService.delete(id);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "删除机构成功");
        } catch (Exception e) {
            logger.error("删除机构失败", e);
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "删除机构失败");
        }
        return "redirect:/sys/offices";
    }

    @PostMapping("/exists")
    @ResponseBody
    public Boolean exists(String condition, Integer id) {
        return officeService.findOffice(condition, id) == null;
    }

    @PostMapping("/idExists")
    @ResponseBody
    public Boolean idExists(Integer id) {
        return officeService.idExists(id) == null;
    }

    @GetMapping("/findOffice")
    @ResponseBody
    public Office findOffice(Integer id) {
        return officeService.findOffice(id);
    }
}
