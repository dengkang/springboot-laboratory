package com.nged.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.jta.JtaTransactionManager;


import static javax.jms.Session.CLIENT_ACKNOWLEDGE;

/**
 * @author: dengk
 * @Date: 2019/4/18 15:05
 * @Description:
 */

@Configuration
public class ActiveMQConfig {
    /**
     * 只有activeMQ支持的方式  单条消息确认
     * AUTO_ACKNOWLEDGE = 1    自动确认
     * CLIENT_ACKNOWLEDGE = 2    客户端手动确认
     * DUPS_OK_ACKNOWLEDGE = 3    自动批量确认
     */
    private static final int INDIVIDUAL_ACKNOWLEDGE = 4 ;

    @Bean
    public RedeliveryPolicy redeliveryPolicy(){
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();

        //是否在每次尝试重新发送失败后,增长这个等待时间
        redeliveryPolicy.setUseExponentialBackOff(true);
        //重发次数,默认为6次   这里设置为10次
        redeliveryPolicy.setMaximumRedeliveries(10);
        //重发时间间隔,默认为1秒
        redeliveryPolicy.setInitialRedeliveryDelay(1);
        //第一次失败后重新发送之前等待500毫秒,第二次失败再等待500 * 2毫秒,这里的2就是value
        redeliveryPolicy.setBackOffMultiplier(2);
        //是否避免消息碰撞
        redeliveryPolicy.setUseCollisionAvoidance(false);
        //设置重发最大拖延时间-1 表示没有拖延只有UseExponentialBackOff(true)为true时生效
        redeliveryPolicy.setMaximumRedeliveryDelay(-1);

        return redeliveryPolicy;

    }

    /**
     * 消息事务管理器
     * @param activeMQConnectionFactory
     * @return
     */
    @Bean
    public JmsTransactionManager jmsTransactionManager(ActiveMQConnectionFactory activeMQConnectionFactory){
        JmsTransactionManager jmsTransactionManager = new JmsTransactionManager();
        jmsTransactionManager.setConnectionFactory(activeMQConnectionFactory);

        return jmsTransactionManager;
    }

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory(@Value("${activemq.url}")String url,@Value("${activemq.user}")String userName,@Value("${activemq.password}")String password, RedeliveryPolicy redeliveryPolicy){
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(userName,password,url);
        factory.setRedeliveryPolicy(redeliveryPolicy);



        System.out.println(factory+"\\"+factory.getClass().getClassLoader());
        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate(ActiveMQConnectionFactory activeMQConnectionFactory){
        JmsTemplate jmsTemplate=new JmsTemplate();

        //进行持久化配置 1表示非持久化，2表示持久化
        jmsTemplate.setDeliveryMode(2);

        jmsTemplate.setConnectionFactory(activeMQConnectionFactory);
        //此处可不设置默认，在发送消息时也可设置队列
        //jmsTemplate.setDefaultDestination(queue);
        //客户端签收模式
        jmsTemplate.setSessionAcknowledgeMode(CLIENT_ACKNOWLEDGE);
        return jmsTemplate;
    }

    /**
     *   定义一个消息监听器连接工厂，这里定义的是点对点模式的监听器连接工厂
     */
    @Bean(name = "jmsQueueListener")
    public DefaultJmsListenerContainerFactory jmsQueueListenerContainerFactory(ActiveMQConnectionFactory activeMQConnectionFactory) {
        DefaultJmsListenerContainerFactory containerFactory =
                new DefaultJmsListenerContainerFactory();
        containerFactory.setConnectionFactory(activeMQConnectionFactory);
        //设置连接数
        containerFactory.setConcurrency("2");
        //重连间隔时间
        containerFactory.setRecoveryInterval(2000L);
        containerFactory.setSessionAcknowledgeMode(CLIENT_ACKNOWLEDGE);
        containerFactory.setClientId("queue1");
        return containerFactory;
    }

    @Bean(name = "jmsQueueWithTransactionListener")
    public DefaultJmsListenerContainerFactory jmsQueueListenerContainerFactoryWithTransaction(ActiveMQConnectionFactory activeMQConnectionFactory,JmsTransactionManager jmsTransactionManager) {
        DefaultJmsListenerContainerFactory containerFactory =
                new DefaultJmsListenerContainerFactory();
        containerFactory.setConnectionFactory(activeMQConnectionFactory);
        //设置连接数
        containerFactory.setConcurrency("1");
        //重连间隔时间
        containerFactory.setRecoveryInterval(1000L);
        containerFactory.setSessionAcknowledgeMode(CLIENT_ACKNOWLEDGE);
        //对listener绑定事务管理
        containerFactory.setTransactionManager(jmsTransactionManager);

        containerFactory.setClientId("queue01");
        return containerFactory;
    }

    @Bean(name = "jmsTopicListener")
    public DefaultJmsListenerContainerFactory jmsTopicListenerContainerFactory(ActiveMQConnectionFactory activeMQConnectionFactory){
        DefaultJmsListenerContainerFactory containerFactory = new DefaultJmsListenerContainerFactory();
        containerFactory.setPubSubDomain(true);

        containerFactory.setSessionAcknowledgeMode(CLIENT_ACKNOWLEDGE);
        containerFactory.setConnectionFactory(activeMQConnectionFactory);
        return containerFactory;
    }

    @Bean(name = "jmsTopicListenerWithTransaction")
    public DefaultJmsListenerContainerFactory jmsTopicListenerContainerFactoryWithTransaction(ActiveMQConnectionFactory activeMQConnectionFactory,JmsTransactionManager jmsTransactionManager){
        DefaultJmsListenerContainerFactory containerFactory = new DefaultJmsListenerContainerFactory();
        containerFactory.setPubSubDomain(true);
        containerFactory.setSessionTransacted(true);
        containerFactory.setSubscriptionDurable(true);
        containerFactory.setSessionAcknowledgeMode(CLIENT_ACKNOWLEDGE);
        containerFactory.setConnectionFactory(activeMQConnectionFactory);
        containerFactory.setTransactionManager(jmsTransactionManager);
        containerFactory.setClientId("topic1");
        return containerFactory;
    }


    @Bean(name = "jmsTopicListenerWithDbTransaction")
    public DefaultJmsListenerContainerFactory jmsTopicListenerWithDbTransaction(ActiveMQConnectionFactory activeMQConnectionFactory,JtaTransactionManager jtaTransactionManager){
        DefaultJmsListenerContainerFactory containerFactory = new DefaultJmsListenerContainerFactory();
        containerFactory.setPubSubDomain(true);

        containerFactory.setSessionAcknowledgeMode(CLIENT_ACKNOWLEDGE);
        containerFactory.setConnectionFactory(activeMQConnectionFactory);
        //开启外部事务
        containerFactory.setTransactionManager(jtaTransactionManager);

        return containerFactory;
    }




    //外部事务管理器
    @Bean
    public JtaTransactionManager jtaTransactionManager(){
        return new JtaTransactionManager();
    }



}
