package controllers;

import model.FileEntriesBuffer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import services.EntriesBufferWriter;
import services.EntriesBufferReader;

import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Controller {

    public static void main(String[] args) {
        String fileName = /*"C:\\Users\\Yana\\Downloads\\all\\train.csv"; */ args[0];
        String topicName = /*"top";*/ args[1];
        Integer readerThreadsCount =  Integer.parseInt(args[2]);

        FileEntriesBuffer buffer = new FileEntriesBuffer(new LinkedBlockingQueue<>());
        EntriesBufferWriter bufferWriter = new EntriesBufferWriter(fileName, buffer);

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = new KafkaProducer<>(props);

        Thread tReader = new Thread(bufferWriter);
        Long startTime = System.currentTimeMillis();
        tReader.start();

        EntriesBufferReader bufferReader = new EntriesBufferReader(buffer, producer, topicName);

       // ArrayList<Thread> bufferReaderThreads = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(readerThreadsCount);
        for (int i = 0; i < readerThreadsCount; i++) {
           // bufferReaderThreads.add(new Thread(bufferReader));
            executor.execute(bufferReader);
//           // bufferReaderThreads.get(i).start();
//            try {
//                bufferReaderThreads.get(i).join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        executor.shutdown();
        producer.close();
        System.out.println("All the " + bufferReader.getProcessedLinesCount() + " lines have been sent to kafka during " + (System.currentTimeMillis() - startTime) / 1000 + " seconds");

    }
}
