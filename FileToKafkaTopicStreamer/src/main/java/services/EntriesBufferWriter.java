package services;

import model.FileEntriesBuffer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class EntriesBufferWriter implements Runnable {
    private String filePath;
    private FileEntriesBuffer buffer;

    public EntriesBufferWriter(String filePath, FileEntriesBuffer buffer) {
        this.buffer = buffer;
        this.filePath = filePath;
    }

    public void readFileFromFileToBuffer() {

        try (BufferedReader b = new BufferedReader(new FileReader(new File(filePath)))) {
            System.out.println("Reading file using Buffered Reader");

            String readLine;
            while ((readLine = b.readLine()) != null) {
                if (!buffer.isFull())
                    buffer.put(readLine);
                else
                    Thread.sleep(1);
            }

            buffer.streamingFinished = true;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        readFileFromFileToBuffer();
    }
}
