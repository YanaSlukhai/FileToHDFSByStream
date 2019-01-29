package services;

import model.FileEntriesBuffer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class EntriesBufferWriter implements Runnable {
    private String filePath;
    private FileEntriesBuffer buffer;
    private Integer linesProcessed = 0;

    public EntriesBufferWriter(String filePath, FileEntriesBuffer buffer) {
        this.buffer = buffer;
        this.filePath = filePath;
    }

    public void writeToBuffer() {

        try (BufferedReader b = new BufferedReader(new FileReader(new File(filePath)))) {
            String readLine;
            while ((readLine = b.readLine()) != null) {
                linesProcessed++;
                if (linesProcessed == 1)
                        continue;
                while(buffer.isFull())
                    Thread.sleep(1);
                BookingSerializer serializer = new BookingSerializer();
                System.out.println(serializer.serializeBookingCSV(readLine));
                buffer.put(serializer.serializeBookingCSV(readLine));
            }

            buffer.streamingFinished = true;
            System.out.println("File to buffer streaming is finished  via thread "+  Thread.currentThread().getId());
            System.out.println("Buffer received  " + linesProcessed + " messages");
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
