package services;

import model.FileEntriesBuffer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;


public class EntriesBufferReader implements Runnable {
    private FileEntriesBuffer<String> buffer;
    private Producer producer;
    private String topic;
    private volatile Integer LINES_PROCESSED = 0;

    public Integer getProcessedLinesCount() {
        return LINES_PROCESSED;
    }

    public EntriesBufferReader(FileEntriesBuffer<String> buffer, Producer producer, String topic) {
        this.buffer = buffer;
        this.topic = topic;
        this.producer = producer;
    }

    public void run() {
        System.out.println("File reading from buffer is started via thread "+ Thread.currentThread().getId());
        readEntry();
    }

    private void readEntry() {
        while (!readIsOver()) {
            if (buffer.isEmpty()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
            } else {
                synchronized (this) {
                    if (!buffer.isEmpty()) {
                        String fileEntry = buffer.poll();
                       // writeToKafkaTopic(fileEntry);
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
        synchronized (this) {
            LINES_PROCESSED++;
        }
        //System.out.println(" Writing by thread " + Thread.currentThread().getId() + "   " + message);
    }

}
