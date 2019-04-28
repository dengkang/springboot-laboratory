package com.nged.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

/**
 * @author: dengk
 * @Date: 2019/4/26 15:40
 * @Description:
 */
@Component
public class KafkaConsumer {

    @KafkaListener(topics = "testTopic")
    public void listen(List<ConsumerRecord<?,?>> records){
        for(ConsumerRecord<?,?> record:records){
            Optional<?> kafkaMessge = Optional.ofNullable(record.value());
            System.out.println(records);
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis())+kafkaMessge.get());
        }
    }


    /* public void listen(ConsumerRecord<?,?> records){
        Optional<?> kafkaMessge = Optional.ofNullable(records.value());
        System.out.println(records);
        System.out.println(kafkaMessge.get());
    }*/

}
