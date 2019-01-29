package model;

import java.util.concurrent.LinkedBlockingQueue;

public class FileEntriesBuffer<T> {
    private LinkedBlockingQueue<T> buffer;
    public volatile Boolean streamingFinished = false;
    private static final Integer MAX_SIZE = 100000;
    public FileEntriesBuffer(LinkedBlockingQueue<T> buffer) {
        this.buffer = buffer;
    }

    public Boolean isEmpty() {
        return buffer.size() == 0;
    }

    public Boolean isFull() {
        return buffer.size() >= MAX_SIZE;
    }

    public T poll() {
        return buffer.poll();
    }

    public void put(T entry) throws InterruptedException {
        if (!this.isFull())
            buffer.put(entry);
    }

    public Integer size(){
        return buffer.size();
    }

}
