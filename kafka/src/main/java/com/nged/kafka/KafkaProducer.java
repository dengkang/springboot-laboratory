package com.nged.kafka;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * @author: dengk
 * @Date: 2019/4/26 15:36
 * @Description:
 */
@Component
public class KafkaProducer {
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    private Gson gson = new GsonBuilder().create();

    public void send(){
        MyMessage myMessage = new MyMessage();
        myMessage.setId(System.currentTimeMillis());
        myMessage.setMsg(UUID.randomUUID().toString());
        myMessage.setSendTime(new Date());
        kafkaTemplate.send("testTopic",gson.toJson(myMessage));
    }

}
