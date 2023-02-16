package com.doodl6.wechatrobot.service;

import com.doodl6.wechatrobot.config.AppConfig;
import com.doodl6.wechatrobot.domain.UserInfo;
import com.doodl6.wechatrobot.response.BaseMessage;
import com.doodl6.wechatrobot.response.TextMessage;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
public class OpenAIService {

    @Resource
    private AppConfig appConfig;


    private Map userMap = new HashMap<String,UserInfo>();
    private Map questionList = new HashMap<String, ArrayList<String>>();


    public BaseMessage getOpenAIResponse(String content, String fromUserName, String toUserName,Long askts){
        log.debug(" the content:" + content + ",fromUserName :" + fromUserName + ",askts:"+ askts.toString());
        //注册用户信息，统计访问次数
        registerUserInfo(fromUserName,askts);
        //执行实际的查询
        return  askOpenAIEntry(content,fromUserName,askts);
    }

    private String registerUserInfo(String fromUserName, Long createTime){
        if(! userMap.containsKey(fromUserName)){
            UserInfo user = new UserInfo();
            user.setFromUserName(fromUserName);
            user.setFirstTime(createTime);
            user.setTotalTimes(new Long(0));
        }else{
            UserInfo user = (UserInfo) userMap.get(fromUserName);
            Long curTimes = user.getTotalTimes()+1;
            user.setTotalTimes(curTimes);
            userMap.replace(fromUserName,user);
        }
        return "success";
    }

    private BaseMessage askOpenAIEntry(String content, String fromUserName, Long createTime){
        content = "Human: " + content;
        if(!questionList.containsKey(fromUserName)){
            ArrayList<String> questionArr = new ArrayList<String>();
            questionArr.add(content);
            questionList.put(fromUserName,questionArr);
        }else{
            ArrayList<String> cur = (ArrayList<String>) questionList.get(fromUserName);
            cur.add(content);
            questionList.replace(fromUserName,cur);
        }
        ArrayList<String> questionArr = (ArrayList<String>)  questionList.get(fromUserName);
        String prompt = buildQuestion(fromUserName,questionArr);
        String res = getReponseFromOpenAI(fromUserName,prompt);
        log.debug("get Response:" + res);
        if( res != null && res.length()!=0) {
            questionArr.add(res);
        }
        return new TextMessage(res);
    }

    private String  buildQuestion(String fromUserName, ArrayList<String> questionArr) {
        String prompt="";
        for( int i = 0;i<questionArr.size();i++){
            prompt=prompt + "\n" + questionArr.get(i);
        }
        prompt = prompt +"\n";

        return prompt;
    }

    private String getReponseFromOpenAI(String fromUserName, String prompt) {
        String token =appConfig.getOpenaiKey();

        OpenAiService service = new OpenAiService(token,  Duration.ofSeconds(120));

        log.info("Creating completion of the follow:" + prompt);
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model("text-davinci-003")
                .maxTokens(2000)
                .temperature(0.9)
                .prompt(prompt)
                .echo(true)
                .user(fromUserName)
                .build();

        String response = service.createCompletion(completionRequest).getChoices().get(0).getText().trim();
        return response;
    }


}
