package com.nged.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@SpringBootApplication
public class KafkaApplication {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(KafkaApplication.class,args);
        KafkaProducer producer = context.getBean(KafkaProducer.class);
        for(int i=0;i<1000;i++){
            producer.send();
            Thread.sleep(500);

        }


    }

    public void test(){
        Properties properties = new Properties();
        properties.put("bootstrap.servers","127.0.0.1:9092");
        properties.put("group.id","testGroup");
        properties.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(properties);
        //订阅主题testTopic 也可以使用匹配的方式
        consumer.subscribe(Collections.singletonList("testTopic"));

        //轮询服务器
        Map<String,Integer> message = new HashMap<>();
        try{
            while (true){
                Duration duration = Duration.ofMillis(100);
                //遇到问题，报错显示连接失败i-wz9e89ueuxxxx:9092 ,这串字符是我的阿里云标识，应该是ip才行，修改
                ConsumerRecords<String,String> records = consumer.poll(duration);
                for(ConsumerRecord<String,String> record:records){
                    int updateConut = 1;
                    if(message.containsValue(record.value())){
                        updateConut = message.get(record.value())+1;
                    }
                    message.put(record.value(),updateConut);
                }
            }
        }finally {
            consumer.close();
        }

    }
}
