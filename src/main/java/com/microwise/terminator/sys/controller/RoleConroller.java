package com.microwise.terminator.sys.controller;

import com.microwise.terminator.sys.entity.Role;
import com.microwise.terminator.sys.service.MenuService;
import com.microwise.terminator.sys.service.RoleService;
import com.microwise.terminator.sys.util.Constants;
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
 * 角色管理controller
 *
 * @author bai.weixing
 * @since 2017/9/5
 */
@RequestMapping("/sys/roles")
@Controller
public class RoleConroller {

    private static final Logger logger = LoggerFactory.getLogger(RoleConroller.class);

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @GetMapping
    public String index(@RequestParam(defaultValue = "1") int pageNumber, @RequestParam(defaultValue = "20") int pageSize, Model model) {
        model.addAttribute("pagehelper", roleService.findAllPagination(pageNumber, pageSize));
        model.addAttribute(Constants.PAGE_PATH, "role/index");
        return "index";
    }

    @GetMapping("/new")
    public String create(Role role, Model model) {
        model.addAttribute("role", role);
        model.addAttribute("menus", menuService.findAll());
        model.addAttribute(Constants.PAGE_PATH, "role/new");
        return "index";
    }

    /**
     * 验证角色名是否有效
     *
     * @param oldName
     * @param name
     * @return
     */
    @ResponseBody
    @GetMapping("checkName")
    public boolean checkName(String oldName, String name) {
        if (name != null && name.equals(oldName)) {
            return true;
        } else if (name != null && roleService.findByName(name).size() == 0) {
            return true;
        }
        return false;
    }

    @PostMapping
    public String save(@Valid Role role, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "添加角色失败");
            redirectAttributes.addFlashAttribute("role", role);
            return "redirect:/sys/roles/new";
        }
        try {
            roleService.saveOrupdate(role);
            logger.info("添加角色成功");
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "添加角色成功");
        } catch (Exception e) {
            logger.error("添加角色失败", e);
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "添加角色失败");
            redirectAttributes.addFlashAttribute("role", role);
            return "redirect:/sys/roles/new";
        }
        return "redirect:/sys/roles";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable String id, Model model) {
        model.addAttribute("role", roleService.find(id));
        model.addAttribute("menus", menuService.findAll());
        model.addAttribute(Constants.PAGE_PATH, "role/update");
        return "index";
    }

    @PostMapping("/{id}/update")
    public String update(@Valid Role role, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "修改角色失败");
            redirectAttributes.addFlashAttribute("role", role);
            return "redirect:/sys/roles/{id}/edit";
        }
        try {
            roleService.saveOrupdate(role);
            logger.info("修改角色成功");
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "修改角色成功");
        } catch (Exception e) {
            logger.error("修改角色失败", e);
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "修改角色失败");
            redirectAttributes.addFlashAttribute("role", role);
            return "redirect:/sys/roles/{id}/edit";
        }
        return "redirect:/sys/roles";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            if (roleService.isUsed(id)) {
                redirectAttributes.addFlashAttribute("success", true);
                redirectAttributes.addFlashAttribute("message", "角色已经被使用,不能删除！");
                return "redirect:/sys/roles";
            }
            roleService.delete(id);
            logger.info("删除角色成功");
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "删除角色成功");
        } catch (Exception e) {
            logger.error("删除角色失败", e);
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "删除角色失败");
        }
        return "redirect:/sys/roles";
    }

}
