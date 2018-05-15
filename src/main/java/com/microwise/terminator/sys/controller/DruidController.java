package com.microwise.terminator.sys.controller;

import com.microwise.terminator.sys.util.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 连接监测Controller
 *
 * @author bai.weixing
 * @since 2017/08/21
 */
@Controller
@RequestMapping("/sys/druids")
public class DruidController {

    @GetMapping
    public String index(Model model, HttpServletRequest request) {
        model.addAttribute(Constants.PAGE_PATH, "druid/index");
        return "index";
    }

}
