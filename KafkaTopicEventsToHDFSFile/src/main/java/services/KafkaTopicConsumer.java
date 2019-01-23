package services;

import java.util.Map;

public interface KafkaTopicConsumer {
     void consumeKafkaTopic(String kafkaTopic, Map<String, Object> kafkaParams);
}
