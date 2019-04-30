package com.nged.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RabbitmqApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(RabbitmqApplication.class,args);
        Producer producer = context.getBean(Producer.class);
        for(int i=1;i<=10;i++){
            /**********/
            producer.sendTopic("topic message " +i);
            //广播
            // producer.sendFanout("fanout message"+ i);
            //producer.directSend(i+"Direct message");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

}
