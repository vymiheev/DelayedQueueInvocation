package com.pixelsoft.task;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Consumer {
    public <T> void enoughGoldenAntilope(Callable<T> callable) {
        try {
            T result = new FutureTask<T>(callable).get();
            System.out.println(result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
