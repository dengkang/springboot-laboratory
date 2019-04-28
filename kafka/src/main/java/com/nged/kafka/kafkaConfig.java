package com.nged.kafka;

import com.sun.javafx.collections.MappingChange;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;
/**
 * @author: dengk
 * @Date: 2019/4/26 16:13
 * @Description:
 */
@EnableKafka
@Configuration
public class kafkaConfig {
    /**
     * kafka地址
     */
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    /**
     * 默认组
     */
    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;
    /**
     * 自动消费设定
     */
    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;
    /**
     * 一次批量处理的数据量
     */
    @Value("${spring.kafka.consumer.max-poll-records}")
    private int maxPollRecords;
    /**
     * 一次批量处理的时间间隔
     */
    @Value("${spring.kafka.consumer.max-poll-interval-ms}")
    private int maxPollIntervalMs;

    @Bean
    public Map<String, Object> consumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        /**
         * maxPollRecords 与 maxPollIntervalMs 都是批量消费数据
         * maxPollIntervalMs为间隔时间 maxPollRecords为最大处理的数据量
         * 在间隔时间（maxPollIntervalMs参数）内（目前为3秒）kafka会根据最大处理的数据量(maxPollRecords参数，目前为5条)一次性获取对应数量，若大于5条，按照5条来消费;若小于5条，按实际消费。
         */
        //每一批数
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        //每一批读取间隔时间
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollIntervalMs);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        return props;
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setBatchListener(true);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }


    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        KafkaTemplate<String,String> kafkaTemplate = new KafkaTemplate<>(producerFactory());
        return kafkaTemplate;
    }
}
