package com.doodl6.wechatrobot.service;

import com.doodl6.wechatrobot.util.Producer;

public class KafkaService {
    private Producer producer;

    public KafkaService( String bootstrapServer){
        this.producer = new Producer(bootstrapServer);
    }

    public void send(String topic ,String msg){
        producer.produce(topic,msg);
    }




}
