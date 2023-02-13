package com.doodl6.wechatrobot.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfo {

    private String toUserName;
    private String fromUserName;
    private Long firstTime;
    private Long lastTime;
    private Long totalTimes;
}
