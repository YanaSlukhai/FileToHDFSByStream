package services;

import model.FileEntriesBuffer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;
import sun.plugin2.message.Message;

import java.util.concurrent.LinkedBlockingQueue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@RunWith(PowerMockRunner.class)
public class EntriesBufferReaderTest {
    private FileEntriesBuffer buffer;
    private EntriesBufferReader bufferReader;
    String topicName;
    private Producer producerMock  = mock(Producer.class);

    @Before
    public void setUp(){
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue();
        buffer = new FileEntriesBuffer<String>(queue);
        topicName = "test_topic";
        bufferReader = new EntriesBufferReader(buffer, producerMock, topicName);
    }

    @Test
    public void readFileFromFileToBufferTest(){
        try {
            buffer.put("Message1");
            buffer.put("Message2");
            buffer.streamingFinished = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        bufferReader.run();
        Mockito.verify(producerMock).send(new ProducerRecord("test_topic", "Message1"));
        Mockito.verify(producerMock).send(new ProducerRecord("test_topic", "Message2"));


    }
}

