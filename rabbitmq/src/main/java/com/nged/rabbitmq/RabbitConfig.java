package com.nged.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
/**
 * @author: dengk
 * @Date: 2019/4/30 11:42
 * @Description:
 */
@Configuration
@EnableRabbit
public class RabbitConfig {

    public final static String topicQueue = "testQueue";
    public final static String topicQueue2 = "testQueue2";
    public final static String exchange_name ="textExchange";
    /**
     *  topic 根据routkey通配符匹配
     */
    public final static String routing_topic_key ="testKey.test.1";
    public final static String routing_topic_key2 ="testKey.test1";

    public final static String binding_topic_key ="testKey.*";
    public final static String binding_topic_key2 ="testKey.#";

    public final static String exchange_direct ="myDirect";
    public final static String direct_queue="directQueue";
    //direct 根据routkey全文匹配
    public final static String binding_direct_key1="testKey_direct";
    public final static String routing_direct_key1 = "testKey_direct";


    public final static String exchange_fanout="myFanout";
    public final static String queue_fanout_1 = "fqueue1";
    public final static String queue_fanout_2 = "fqueue2";


    @Bean
    public Queue queueFanout1(){
        Queue queueFanout1 = new Queue(queue_fanout_1);

        return queueFanout1;
    }

    @Bean
    public Queue queueFanout2(){
        Queue queueFanout2 = new Queue(queue_fanout_2);

        return queueFanout2;
    }

    @Bean
    public Queue queue(){
       Queue queue1 = new Queue(topicQueue);

        return queue1;
    }
    @Bean
    public Queue topicQueue2(){
        Queue topicQueue = new Queue(topicQueue2);

        return topicQueue;
    }

    @Bean
    public Queue queue2(){
        Queue queue2 = new Queue(direct_queue);

        return queue2;
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(exchange_direct);
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(exchange_name);
    }

    @Bean
    public FanoutExchange exchangeFanout(){
        return new FanoutExchange(exchange_fanout);
    }
    @Bean
    public Binding binding(Queue queue, TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(binding_topic_key);
    }
    @Bean
    public Binding binding3(Queue topicQueue2, TopicExchange exchange){
        return BindingBuilder.bind(topicQueue2).to(exchange).with(binding_topic_key2);
    }

    @Bean
    public Binding binding2(Queue queue2, DirectExchange directExchange){
        return BindingBuilder.bind(queue2).to(directExchange).with(binding_direct_key1);
    }

    @Bean
    public Binding bindingqueueFanout2(Queue queueFanout2, FanoutExchange fanoutExchange){
        return BindingBuilder.bind(queueFanout2).to(fanoutExchange);
    }
    @Bean
    public Binding bindingqueueFanout1(Queue queueFanout1, FanoutExchange fanoutExchange){
        return BindingBuilder.bind(queueFanout1).to(fanoutExchange);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("127.0.0.1", 5672);
        connectionFactory.setUsername("springcloud");
        connectionFactory.setPassword("123456");
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }


}
