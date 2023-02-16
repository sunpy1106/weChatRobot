package com.doodl6.wechatrobot.domain;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MsgItem {

    private String fromUserName ;
    private String toUserName ;
    private String content  ;
    private Long createTime ;


    public MsgItem(String fromUserName, String toUserName, String content, Long createTime) {
        this.fromUserName = fromUserName;
        this.toUserName = toUserName;
        this.content = content;
        this.createTime = createTime;
    }
}
