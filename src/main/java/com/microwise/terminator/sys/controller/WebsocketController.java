package com.microwise.terminator.sys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by lijianfei on 2017/12/12.
 *
 * @author li.jianfei
 * @since 2017/12/12
 */
@Controller
@RequestMapping("websocket")
public class WebsocketController {

    @GetMapping
    public String websocket() {
        return "websocket";
    }
}
