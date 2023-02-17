package com.doodl6.wechatrobot.handle;

import com.doodl6.wechatrobot.config.KeywordConfig;
import com.doodl6.wechatrobot.domain.MsgItem;
import com.doodl6.wechatrobot.domain.WeChatMessage;
import com.doodl6.wechatrobot.response.BaseMessage;
import com.doodl6.wechatrobot.response.TextMessage;
import com.doodl6.wechatrobot.service.KafkaService;
import com.doodl6.wechatrobot.service.OpenAIService;
import com.doodl6.wechatrobot.service.TulingService;
import com.doodl6.wechatrobot.util.LogUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 文本类型消息处理类
 */
@Service
@Slf4j
public class TextMessageHandle implements WeChatMessageHandle {

    @Resource
    private TulingService tulingService;

    @Resource
    private OpenAIService openAIService;

    @Autowired(required = false)
    private KeywordConfig keywordConfig;


    @Autowired
    private KafkaService kafkaService;

    @Override
    public BaseMessage processMessage(WeChatMessage weChatMessage) {

        log.info(LogUtil.buildLog("收到用户文本信息", weChatMessage));

        String fromUserName = weChatMessage.getFromUserName();
        String toUserName = weChatMessage.getToUserName();
        String content = weChatMessage.getContent();
        Long  askts = weChatMessage.getCreateTime();

        BaseMessage message = null;
        //匹配了关键字
        if (keywordConfig != null) {
            message = keywordConfig.getMessageByKeyword(content);
        }

//        if (message == null) {
//            message = tulingService.getTulingResponse(content, fromUserName);
//        }
        if(message == null ){
            message = new TextMessage("success");
            MsgItem msg = new MsgItem(fromUserName,toUserName,content,askts);
            String msgContent = new Gson().toJson(msg);
            log.info("Write Msg \" "+ msgContent +" \" to Kafka topic msg_content" );
            kafkaService.sendMessage("msg_content",msgContent);
            //openAIService.getOpenAIResponse(content,fromUserName,toUserName,askts);
        }

        if (message != null) {
            message.setFromUserName(toUserName);
            message.setToUserName(fromUserName);
            message.setCreateTime(System.currentTimeMillis());
        }
        return message;
    }
}
