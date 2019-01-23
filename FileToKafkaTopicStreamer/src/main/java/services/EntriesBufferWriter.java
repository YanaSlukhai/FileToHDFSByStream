package services;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import model.Booking;
import model.FileEntriesBuffer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class EntriesBufferWriter implements Runnable {
    private String filePath;
    private FileEntriesBuffer buffer;
    private Integer LINES_PROCESSED = 0;

    public EntriesBufferWriter(String filePath, FileEntriesBuffer buffer) {
        this.buffer = buffer;
        this.filePath = filePath;
    }

    public void writeToBuffer() {

        try (BufferedReader b = new BufferedReader(new FileReader(new File(filePath)))) {
            String readLine;
            while ((readLine = b.readLine()) != null) {
                LINES_PROCESSED++;
                if (LINES_PROCESSED == 1)
                        continue;
                while(buffer.isFull())
                    Thread.sleep(1);
                BookingSerializer serializer = new BookingSerializer();
                System.out.println(serializer.serializeBookingCSV(readLine));
                buffer.put(serializer.serializeBookingCSV(readLine));
            }

            buffer.streamingFinished = true;
            System.out.println("File to buffer streaming is finished  via thread "+  Thread.currentThread().getId());
            System.out.println("Buffer received  " + LINES_PROCESSED + " messages");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("File to buffer streaming is started via "+ + Thread.currentThread().getId());
        writeToBuffer();
    }
}
