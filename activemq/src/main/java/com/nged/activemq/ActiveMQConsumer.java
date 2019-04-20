package com.nged.activemq;

import org.apache.activemq.ActiveMQQueueSession;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.logging.Logger;

/**
 * @author: dengk
 * @Date: 2019/4/18 14:58
 * @Description:
 */
@Component
public class ActiveMQConsumer {
    private final Logger logger = Logger.getLogger("ActiveMQConsumer");

    //@JmsListener(destination = "test.queue",containerFactory = "jmsQueueListener")
//@JmsListener(destination = "test.queue")
    public void receiveTest(final TextMessage message, Session session) throws JMSException {

        try {
            System.out.println("处理消息"+message.getText());
            message.acknowledge();//使用手动签收模式，需要手动的调用，如果不在catch中调用session.recover()消息只会在重启服务后重发
        } catch (JMSException e) {
            e.printStackTrace();
            session.recover();
        }
    }

    //带事务管理的队列消费
    @JmsListener(destination = "test.queueTransaction")
    public void queueTransactionTest(final TextMessage message, Session session) throws JMSException {

            logger.info(message.getText());
            System.out.println(Thread.currentThread().getName()+"处理消息"+message.getText());
            message.acknowledge();
            session.rollback();
            //message.acknowledge();//使用手动签收模式，需要手动的调用，如果不在catch中调用session.recover()消息只会在重启服务后重发


    }





    @JmsListener(destination = "topic1",containerFactory = "jmsTopicListenerWithTransaction")
    public void topicHandler(final TextMessage message, Session session){

        try {
            System.out.println("handler topic message"+message.getText());
            message.acknowledge();
            //session.rollback();
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }


}
