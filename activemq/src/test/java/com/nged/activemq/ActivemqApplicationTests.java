package com.nged.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Queue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivemqApplicationTests {

    @Autowired
    private ActiveMQProducer producer;
    @Resource
    private ConnectionFactory factory;


    @Test
    public void contextLoads() {




    }
    @Test
    public void testSendTopic(){
        ActiveMQTopic topic = new ActiveMQTopic("topic1");
        producer.sendMessage(topic,"topic hello");
        System.out.println(factory);

    }
    @Test
    public void testSendQueue(){
        ActiveMQQueue queue = new ActiveMQQueue("test.queue");
        producer.sendMessage(queue,"hello");
    }
    @Test
    public void testQueueWithTransactionSuccess(){
        ActiveMQQueue queue = new ActiveMQQueue("test.queueTransaction");
        producer.sendMessage(queue,"100");
    }
    @Test
    public void testQueueWithTransactionRollback(){
        ActiveMQQueue queue = new ActiveMQQueue("test.queueTransaction");
        producer.sendMessage(queue,"100 15：44");


    }

}
