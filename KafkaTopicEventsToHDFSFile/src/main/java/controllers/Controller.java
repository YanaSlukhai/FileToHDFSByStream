package controllers;

import org.apache.kafka.common.serialization.StringDeserializer;
import services.KafkaTopicConsumer;
import services.SparkElasticSearchConsumer;

import java.util.HashMap;
import java.util.Map;

public class Controller {
    public static void main(String[] args) {
        KafkaTopicConsumer consumer = new SparkElasticSearchConsumer();

        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", "localhost:9092");
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", StringDeserializer.class);
        kafkaParams.put("group.id", "1");
        kafkaParams.put("auto.offset.reset", "earliest");
        kafkaParams.put("enable.auto.commit", false);

        String topicName = args[0];

        consumer.consumeKafkaTopic(topicName, kafkaParams);
    }
}
