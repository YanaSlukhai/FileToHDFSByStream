package controllers;

import model.FileEntriesBuffer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import services.EntriesBufferWriter;
import services.EntriesBufferReader;

import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

public class Controller {
    public static void main(String[]args){
        EntriesBufferWriter writer = new EntriesBufferWriter();
        FileEntriesBuffer buffer = new FileEntriesBuffer(new LinkedBlockingQueue<>());

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "sandbox-hdp.hortonworks.com:6667");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = new KafkaProducer<>(props);


        Thread tReader = new Thread(
                ()-> writer.readFileFromFileToBuffer(args[0], buffer));
        tReader.start();

        EntriesBufferReader buffertoKafkaWriter = new EntriesBufferReader(buffer, producer, "test");

        ArrayList<Thread>  bufferToKafkaWriterthreads = new ArrayList<>();
        for(int i = 0; i <4; i++ ) {
            bufferToKafkaWriterthreads.add(new Thread(buffertoKafkaWriter));
            bufferToKafkaWriterthreads.get(i).start();
        }
    }
}
