package com.nged.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author: dengk
 * @Date: 2019/4/30 12:04
 * @Description:
 */
@Component
public class Consumer {

    @RabbitListener(queues = RabbitConfig.topicQueue)
    public void consumer(String message){
        System.out.println("testQueue处理:"+ "[" + message);
    }

    @RabbitListener(queues = RabbitConfig.topicQueue2)
    public void consumerTopicQueue2(String message){
        System.out.println("testQueue2处理:" +"[" + message);
    }

    @RabbitListener(queues = RabbitConfig.direct_queue)
    public void consumerDirect(String message){

        System.out.println("direct 处理:"+"[" + message);
    }


    @RabbitListener(queues = RabbitConfig.queue_fanout_1)
    public void consumerFanout1(String message){

        System.out.println("fanout1 处理:" +"[" + message);
    }

    @RabbitListener(queues = RabbitConfig.queue_fanout_2)
    public void consumerFanout2(String message){

        System.out.println("fanout2 处理:" +"[" + message);
    }
}
