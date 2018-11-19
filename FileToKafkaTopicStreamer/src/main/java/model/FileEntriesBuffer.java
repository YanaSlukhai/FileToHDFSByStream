package model;

import java.util.concurrent.LinkedBlockingQueue;

public class FileEntriesBuffer {
    private LinkedBlockingQueue<String> buffer;
    private volatile Boolean allTheFileWasStreamedToBuffer = false;

    public Boolean getAllTheFileWasStreamedToBuffer() {
        return allTheFileWasStreamedToBuffer;
    }

    public Boolean allTheFileWasStreamedToBuffer() {
        return allTheFileWasStreamedToBuffer;
    }

    public void setAllTheFileWasStreamedToBuffer(Boolean allTheFileWasStreamedToBuffer) {
        this.allTheFileWasStreamedToBuffer = allTheFileWasStreamedToBuffer;
    }

    public FileEntriesBuffer(LinkedBlockingQueue<String> buffer) {
        this.buffer = buffer;
    }

    public LinkedBlockingQueue<String> getBuffer() {
        return buffer;
    }
}
