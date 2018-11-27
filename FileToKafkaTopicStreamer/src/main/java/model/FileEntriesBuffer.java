package model;

import java.util.concurrent.LinkedBlockingQueue;

public class FileEntriesBuffer {
    private LinkedBlockingQueue<String> buffer;
    public volatile Boolean streamingFinished = false;

    public FileEntriesBuffer(LinkedBlockingQueue<String> buffer) {
        this.buffer = buffer;
    }
    public Boolean isEmpty(){
        return buffer.size() == 0;
    }
    public LinkedBlockingQueue<String> getBuffer() {
        return buffer;
    }
}
