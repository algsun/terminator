package com.microwise.terminator.sys.controller;

import com.microwise.terminator.common.config.Global;
import com.microwise.terminator.sys.entity.Office;
import com.microwise.terminator.sys.entity.Role;
import com.microwise.terminator.sys.entity.User;
import com.microwise.terminator.sys.service.OfficeService;
import com.microwise.terminator.sys.service.RoleService;
import com.microwise.terminator.sys.service.UserService;
import com.microwise.terminator.sys.util.UserUtils;
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
 * 用户controller
 *
 * @author sun.cong
 * @since 2017/8/28
 */
@Controller
@RequestMapping("/sys/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private OfficeService officeService;

    /**
     * 列表显示所有用户
     *
     * @param model
     * @param pageNum
     * @return
     */
    @GetMapping
    public String index(Model model, @RequestParam(defaultValue = "1") int pageNum,
                        @RequestParam(defaultValue = "20") int pageSize) {
        try {
            model.addAttribute("currentUser", UserUtils.getCurrentUser());
            model.addAttribute("pageInfo", userService.findUsers(pageNum, pageSize));
            model.addAttribute(Global.PAGE_PATH, "user/index");
        } catch (Exception e) {
            logger.error("未检索到用户数据", e);
            model.addAttribute("success", false);
            model.addAttribute("message", "未检索到用户数据");
        }
        return "index";
    }

    @GetMapping("/new")
    public String create(Model model, RedirectAttributes redirectAttributes, User user) {
        List<Role> roles = roleService.findByExample();
        List<Office> offices = officeService.findOffices(false);
        if (roles.size() == 0) {
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "添加用户前请至少添加一个角色！");
            return "redirect:/sys/roles/new";
        }
        if (offices.size() == 0) {
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "添加用户前请至少添加一个机构！");
            return "redirect:/sys/offices/new";
        }
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        model.addAttribute("offices", offices);
        model.addAttribute(Global.PAGE_PATH, "user/new");
        return "index";
    }

    @PostMapping("/save")
    public String save(@Valid User user, BindingResult bindingResult,
                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "用户添加失败");
            return "redirect:/sys/users/new";
        }
        try {
            userService.save(user);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "用户添加成功");
        } catch (Exception e) {
            logger.error("增加用户不成功", e);
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "用户添加失败");
            return "redirect:/sys/users/new";
        }
        return "redirect:/sys/users";
    }

    @GetMapping("/edit")
    public String edit(Model model, String id) {
        if (id == null || id == "") {
            id = UserUtils.getCurrentUser().getId();
        }
        model.addAttribute("roles", roleService.findByExample());
        model.addAttribute("offices", officeService.findOffices(false));
        model.addAttribute("user", userService.findUserWithRoles(id));
        model.addAttribute(Global.PAGE_PATH, "user/edit");
        return "index";
    }

    @PostMapping("/update")
    public String update(@Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "修改用户失败");
            return "redirect:/sys/users/edit";
        }
        try {
            userService.updateUserWithRole(user);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "修改用户成功");
        } catch (Exception e) {
            logger.error("修改用户失败", e);
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "修改用户失败");
            return "redirect:/sys/users/edit";
        }
        return "redirect:/sys/users";

    }

    @GetMapping("/delete")
    public String delete(RedirectAttributes redirectAttributes, String id) {
        try {
            userService.delete(id);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "删除用户成功");
        } catch (Exception e) {
            logger.error("删除用户失败", e);
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "删除用户失败");
        }
        return "redirect:/sys/users";
    }

    @PostMapping("/exists")
    @ResponseBody
    public boolean exists(String condition, String id) {
        return userService.findUser(condition, id) == null;
    }

}
