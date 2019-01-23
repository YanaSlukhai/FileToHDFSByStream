package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.elasticsearch.spark.streaming.api.java.JavaEsSparkStreaming;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class SparkElasticSearchConsumer implements KafkaTopicConsumer {
    private Integer STREAMING_BATCH_INTERVAL = 10000;

    public void consumeKafkaTopic(String kafkaTopic, Map<String, Object> kafkaParams) {

        connectSparkStreamingToElasticSearch(kafkaTopic, kafkaParams);
        //connectSparkStreamingToElasticSearch();

    }

    public static boolean isJSONValid(String jsonInString) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(jsonInString);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void connectSparkStreamingToElasticSearch(String kafkaTopic, Map<String, Object> kafkaParams) {
        Collection<String> topics = Arrays.asList(kafkaTopic);
        JavaSparkContext jsc = new JavaSparkContext(new SparkConf());
        JavaStreamingContext jssc = new JavaStreamingContext(jsc, new Duration(STREAMING_BATCH_INTERVAL));

        JavaDStream<ConsumerRecord<String, String>> stream =
                KafkaUtils.createDirectStream(
                        jssc,
                        LocationStrategies.PreferBrokers(),
                        ConsumerStrategies.<String, String>Subscribe(topics, kafkaParams)
                );
        stream.count().print();
        JavaDStream<String> values = stream.map(record -> record.value());
        JavaEsSparkStreaming.saveJsonToEs(values, "spark1/json-trips");

        jssc.start();
        try {
            jssc.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
