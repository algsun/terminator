package com.microwise.terminator.sys.controller;

import com.google.common.collect.Lists;
import com.microwise.terminator.sys.entity.Menu;
import com.microwise.terminator.sys.service.MenuService;
import com.microwise.terminator.sys.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.management.MemoryUsage;
import java.util.List;

/**
 * 菜单Controller
 *
 * @author bai.weixing
 * @since 2017/08/21
 */
@Controller
@RequestMapping("/sys/menus")
public class MenuController {

    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuService menuService;


    @GetMapping
    public String index(Model model) {
        List<Menu> menus = menuService.findAll();
        model.addAttribute("menus", menus);
        model.addAttribute(Constants.PAGE_PATH, "menu/index");
        return "index";
    }

    /**
     * 批量修改菜单排序
     */
    @PostMapping
    public String updateSort(String[] ids, Long[] sorts, RedirectAttributes redirectAttributes) {
        try {
            for (int i = 0; i < ids.length; i++) {
                Menu menu = new Menu(ids[i]);
                menu.setSort(sorts[i]);
                menuService.updateMenuSort(menu);
            }
            logger.info("菜单排序修改成功");
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "菜单排序修改成功");
        } catch (Exception e) {
            logger.error("菜单排序修改失败", e);
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "菜单排序修改失败");
        }

        return "redirect:/sys/menus";
    }
}
