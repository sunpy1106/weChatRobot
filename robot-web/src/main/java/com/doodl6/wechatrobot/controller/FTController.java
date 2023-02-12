package com.doodl6.wechatrobot.controller;

import com.doodl6.wechatrobot.service.WeChatService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/test")
public class FTController {

    @Resource
    private WeChatService weChatService;

    /**
     * 接收来至微信服务器的消息
     **/
    @RequestMapping(value = "receiveMessage")
    public String receiveMessage( HttpServletRequest request) {
        log.info("hello world");
        return "{key:value}";
    }

}
