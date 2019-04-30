package com.nged.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: dengk
 * @Date: 2019/4/30 12:03
 * @Description:
 */
@Component
public class Producer {
    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void sendTopic(String message){
        rabbitTemplate.convertAndSend(RabbitConfig.exchange_name,RabbitConfig.routing_topic_key2,message);
    }


    public void sendDirect(String message){
        rabbitTemplate.convertAndSend(RabbitConfig.exchange_direct,RabbitConfig.routing_direct_key1,message);
    }

    public void sendFanout(String message){
        rabbitTemplate.convertAndSend(RabbitConfig.exchange_fanout,null,message);
    }

}
