package com.microwise.terminator.sys.controller;

import com.google.common.base.Strings;
import com.microwise.terminator.sys.util.UserUtils;
import com.microwise.terminator.sys.entity.Menu;
import com.microwise.terminator.sys.service.MenuService;
import com.microwise.terminator.sys.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;


/**
 * 系统管理 Controller
 *
 * @author bai.weixing
 * @since 2017-09-04
 */
@Controller
@RequestMapping("systems")
@SessionAttributes(value = {Constants.Session.CURRENT_SYSTEM, Constants.Session.MENUS, Constants.Session.CURRENT_MENU})

public class SystemController {

    @Autowired
    MenuService menuService;


    @GetMapping("/{systemId}/switch")
    public String switchSystem(Model model, @PathVariable String systemId) {
        model.addAttribute(Constants.Session.CURRENT_SYSTEM, systemId);
        if (systemId.equals("27")) {
            return "redirect:/sys/overviews";
        }
        List<Menu> menus = UserUtils.getMenus();
        Menu currentMenu = null;
        for (Menu menu : menus) {
            if (menu.getParentId().equals(systemId)) {
                currentMenu = menu;
                break;
            }
        }
        model.addAttribute(Constants.Session.CURRENT_MENU, currentMenu);
        model.addAttribute(Constants.Session.MENUS, menus);

        if (menus.size() > 0) {
            String url = currentMenu.getHref();
            if (!Strings.isNullOrEmpty(url)) return "redirect:" + url;
        }
        return "redirect:/index";
    }

    @GetMapping("/{menuId}/menu")
    public String menu(Model model, @PathVariable String menuId) {
        Menu menu = menuService.get(menuId);
        model.addAttribute(Constants.Session.CURRENT_MENU, menu);
        String url = menu.getHref();
        if (!Strings.isNullOrEmpty(url)) {
            return "redirect:" + url;
        }
        return "redirect:/index";
    }
}
