package com.pixelsoft.task;

/**
 * Created by mvj on 14.02.2018.
 */
public class EntryPoint {
    public static void main(String[] args) {
        new EntryPoint().start();
    }

    private void start() {
        Pipeline pipeline = new Pipeline();
        Producer producer = new Producer(500, 10_000, pipeline);
        try {
            producer.runGoldenAntilope();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
