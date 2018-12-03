package controllers;

import services.KafkaTopicConsumer;
import services.SparkKafkaTopicConsumer;

public class Controller {
    public static void main(String[] args){
        KafkaTopicConsumer consumer = new SparkKafkaTopicConsumer();
        consumer.consumeKafkaTopic(args[0]);
    }
}
