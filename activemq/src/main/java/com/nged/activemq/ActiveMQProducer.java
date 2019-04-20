package com.nged.activemq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.*;


/**
 * @author: dengk
 * @Date: 2019/4/18 14:47
 * @Description:
 */
@Component
public class ActiveMQProducer {
    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ActiveMQConnectionFactory activeMQConnectionFactory;



    public void sendMessage(Destination destination, final String message){
        System.out.println(jmsTemplate.getConnectionFactory()+"\\"+jmsTemplate.getConnectionFactory().getClass().getClassLoader());
        jmsTemplate.convertAndSend(destination,message);
    }


    //使用事务
    public void sendMessageWithTransaction(Destination destination,final String message ) throws JMSException {
        ActiveMQSession queueSession = null;
        try {
            ActiveMQConnection activeMQConnection = (ActiveMQConnection) activeMQConnectionFactory.createConnection();
             queueSession = (ActiveMQSession) activeMQConnection.createSession(true,Session.CLIENT_ACKNOWLEDGE);
             MessageProducer producer =  queueSession.createProducer(destination);

             //这里直接写了，就不作为参数了
             Topic topic = new ActiveMQTopic("topicWWW");
             TopicPublisher publisher = queueSession.createPublisher(topic);

             ActiveMQTextMessage message1 = new ActiveMQTextMessage();
             message1.setText(message);
            //先向queue发10条
            int i=0;
            while(i<10){
                producer.send(message1);
                i++;
            }
            //向topic发一条
            ActiveMQTextMessage topicTextMessage = new ActiveMQTextMessage();
            topicTextMessage.setText("topic wwww");
            publisher.publish(topicTextMessage);
            //写个异常
            //Integer.parseInt("sssssss");
            queueSession.commit();

        } catch (NumberFormatException e){

            e.printStackTrace();
            if(null!=queueSession){
                queueSession.rollback();
            }

        }


    }

}
