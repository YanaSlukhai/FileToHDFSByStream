package services;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.StreamingContext;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class SparkKafkaTopicConsumer implements KafkaTopicConsumer{
    public void consumeKafkaTopic(String kafkaTopic) {
        SparkConf conf = new SparkConf().setAppName("SparkKafkaTopicConsumer").setMaster("local[*]");
        JavaStreamingContext sparkStreamingContext = new JavaStreamingContext(conf, new Duration(100000));
        connectSparkStreamingToKafka(sparkStreamingContext, kafkaTopic);
    }

    private void connectSparkStreamingToKafka(JavaStreamingContext streamingContext, String kafkaTopic){

        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers","sandbox-hdp.hortonworks.com:6667");
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", StringDeserializer.class);
        kafkaParams.put("group.id", "1");
        kafkaParams.put("auto.offset.reset","latest");
        kafkaParams.put("enable.auto.commit", false);

        Collection<String> topics = Arrays.asList(kafkaTopic);

        JavaInputDStream<ConsumerRecord<String, String>> stream =
                KafkaUtils.createDirectStream(
                        streamingContext,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.<String, String>Subscribe(topics, kafkaParams)
                );
        System.out.println(" Printing before");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stream.mapToPair(record -> new Tuple2<>(record.key(), record.value())).saveAsHadoopFiles(
                "hdfs://sandbox-hdp.hortonworks.com:6667/user/spark/stream/", "txt",
                Text.class, IntWritable.class, TextOutputFormat.class);
        stream.foreachRDD(rdd -> myFunction(rdd));


        System.out.println(" Printing after");
        streamingContext.start();
        try {
            streamingContext.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void myFunction(org.apache.spark.api.java.JavaRDD<ConsumerRecord<String, String>> rdd) {
        System.out.print("s");
    }
}
