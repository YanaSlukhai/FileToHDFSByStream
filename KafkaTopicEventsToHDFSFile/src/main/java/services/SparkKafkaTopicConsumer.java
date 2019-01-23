package services;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;


public class SparkKafkaTopicConsumer implements KafkaTopicConsumer {
    private Integer STREAMING_BATCH_INTERVAL = 100000;
    private String HADOOP_FILES_LOCATION_PREFIX = "hdfs://sandbox-hdp.hortonworks.com/user/spark/stream/";

    public void consumeKafkaTopic(String kafkaTopic, Map<String, Object> kafkaParams) {
        SparkConf conf = new SparkConf().setAppName("SparkKafkaTopicConsumer").setMaster("local[*]");
        JavaStreamingContext sparkStreamingContext = new JavaStreamingContext(conf, new Duration(STREAMING_BATCH_INTERVAL));
        connectSparkStreamingToKafka(sparkStreamingContext, kafkaTopic, kafkaParams);
    }

    private void connectSparkStreamingToKafka(JavaStreamingContext streamingContext, String kafkaTopic, Map<String, Object> kafkaParams) {

        Collection<String> topics = Arrays.asList(kafkaTopic);

        JavaInputDStream<ConsumerRecord<String, String>> stream =
                KafkaUtils.createDirectStream(
                        streamingContext,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.<String, String>Subscribe(topics, kafkaParams)
                );

        stream.mapToPair(record -> new Tuple2<>(record.key(), record.value())).count().print();
        stream.mapToPair(record -> new Tuple2<>(record.key(), record.value())).saveAsHadoopFiles(
                HADOOP_FILES_LOCATION_PREFIX, "",
                Text.class, IntWritable.class, TextOutputFormat.class);
        streamingContext.start();
        try {
            streamingContext.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
