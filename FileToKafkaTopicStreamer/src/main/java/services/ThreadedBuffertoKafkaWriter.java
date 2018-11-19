package services;

import model.FileEntriesBuffer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;


public class ThreadedBuffertoKafkaWriter implements Runnable {
    private FileEntriesBuffer buffer;
    private Producer producer;
    private String topic;
    private volatile long startTime = System.nanoTime();

    public ThreadedBuffertoKafkaWriter(FileEntriesBuffer buffer, Producer producer, String topic) {
        this.buffer = buffer;
        this.topic = topic;
        this.producer = producer;
    }


    public void run() {
        writeFromBufferToKafkaTopic();
        System.out.println(System.nanoTime() - startTime);
    }

    private void writeFromBufferToKafkaTopic() {
        while (true) {
            synchronized (this) {
                if (buffer.getBuffer().size() > 0)
                    writeToKafkaTopic(buffer.getBuffer().poll());
                else if (!buffer.allTheFileWasStreamedToBuffer()) {
                    try {
                        System.out.println(" HERE SLEEEP by thread " + Thread.currentThread().getId());
                        // logger here
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else break;
            }
        }
    }

    private void writeToKafkaTopic(String message) {
        producer.send(new ProducerRecord(topic, message));
        System.out.println(" Writing by thread " + Thread.currentThread().getId() + "   " + message);
    }

}
