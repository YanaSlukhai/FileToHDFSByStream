package services;

import model.FileEntriesBuffer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;


public class EntriesBufferReader implements Runnable {
    private FileEntriesBuffer<String> buffer;
    private Producer producer;
    private String topic;
    private Integer PROCESSED_LINES_COUNT = 0;

    public EntriesBufferReader(FileEntriesBuffer<String> buffer, Producer producer, String topic) {
        this.buffer = buffer;
        this.topic = topic;
        this.producer = producer;
    }

    public void run() {
        readEntry();
        System.out.println(" COUNT = " + PROCESSED_LINES_COUNT);
    }

    private void readEntry() {
        while (!readIsOver()) {
            if (buffer.isEmpty()) {
                try {
                    Thread.sleep(100);
                    System.out.println("reader sleeps");
                } catch (InterruptedException ignored) {
                }
            } else {
                synchronized (this) {
                    if (!buffer.isEmpty()) {
                        String fileEntry = buffer.poll();
                        writeToKafkaTopic(fileEntry);
                    }
                }
            }
        }
    }

    private Boolean readIsOver() {
        return buffer.streamingFinished && buffer.isEmpty();
    }

    private void writeToKafkaTopic(String message) {
        producer.send(new ProducerRecord(topic, message));
        PROCESSED_LINES_COUNT++;
        //System.out.println(" Writing by thread " + Thread.currentThread().getId() + "   " + message);
    }

}
