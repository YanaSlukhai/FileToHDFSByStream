package services;

import model.FileEntriesBuffer;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class EntriesBufferWriterTest {
    private FileEntriesBuffer buffer;
    private EntriesBufferWriter bufferWriter;

    @Before
    public void setUp() throws Exception {
        String filePath = "src/test/resources/ex_train.csv";
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue();
        buffer = new FileEntriesBuffer(queue);
        bufferWriter = new EntriesBufferWriter(filePath, buffer);
    }

    @Test
    public void readFileFromFileToBufferTest(){
        bufferWriter.run();
        assertEquals(true, buffer.streamingFinished);
        assertEquals(new Integer(100), buffer.size());
    }
}