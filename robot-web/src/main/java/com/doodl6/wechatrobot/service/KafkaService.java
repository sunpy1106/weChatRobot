package com.doodl6.wechatrobot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


import java.io.IOException;

import org.springframework.kafka.core.KafkaTemplate;


/**
 * @author sunpy
 */
@Slf4j
@Service
public class KafkaService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;




    public void sendMessage(String topic ,String message) {
        log.info(String.format("#### -> Producing message -> %s", message));
        this.kafkaTemplate.send(topic, message);
    }






}
