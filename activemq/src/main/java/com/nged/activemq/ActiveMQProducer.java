package com.nged.activemq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;


/**
 * @author: dengk
 * @Date: 2019/4/18 14:47
 * @Description:
 */
@Component
public class ActiveMQProducer {
    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMessage(Destination destination, final String message){
        System.out.println(jmsTemplate.getConnectionFactory()+"\\"+jmsTemplate.getConnectionFactory().getClass().getClassLoader());
        jmsTemplate.convertAndSend(destination,message);
    }

}
