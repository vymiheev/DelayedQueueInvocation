package com.pixelsoft.task;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mvj on 14.02.2018.
 */
public class Producer {
    private static final Clock clock = Clock.system(ZoneId.of("Europe/Moscow"));
    private int threadsNum;
    private int elNum;
    private Pipeline pipeline;

    public Producer(int threads, int elNum, Pipeline pipeline) {
        this.threadsNum = threads;
        this.elNum = elNum;
        this.pipeline = pipeline;
    }

    public void runGoldenAntilope() throws InterruptedException {
        List<Callable<Void>> callables = init();
        ExecutorService executorService = Executors.newFixedThreadPool(threadsNum);
        executorService.invokeAll(callables);
        Thread.currentThread().sleep(6_000);
        executorService.invokeAll(callables);

    }

    private List<Callable<Void>> init() {
        Callable<Void> first = () -> {
            int sec = 5;
            System.out.println("should delayed for a " + sec + " sec");
            LocalDateTime localDateTime = LocalDateTime.now(clock).plusSeconds(sec);
            pipeline.push(new SomeAction(sec), localDateTime);
            return null;
        };
        Callable<Void> second = () -> {
            int sec = 3;
            System.out.println("should delayed for a " + sec + " sec");
            LocalDateTime localDateTime = LocalDateTime.now(clock).plusSeconds(sec);
            pipeline.push(new SomeAction(sec), localDateTime);
            return null;
        };
        Callable<Void> third = () -> {
            int sec = 1;
            System.out.println("should delayed for a " + sec + " sec");
            LocalDateTime localDateTime = LocalDateTime.now(clock).plusSeconds(sec);
            pipeline.push(new SomeAction(sec), localDateTime);
            return null;
        };
        Callable<Void> four = () -> {
            int sec = 10;
            System.out.println("should delayed for a " + sec + " sec");
            LocalDateTime localDateTime = LocalDateTime.now(clock).plusSeconds(sec);
            pipeline.push(new SomeAction(sec), localDateTime);
            return null;
        };
        Callable<Void> five = () -> {
            int sec = 7;
            System.out.println("should delayed for a " + sec + " sec");
            LocalDateTime localDateTime = LocalDateTime.now(clock).plusSeconds(sec);
            pipeline.push(new SomeAction(sec), localDateTime);
            return null;
        };


        List<Callable<Void>> callableList = new ArrayList<>();
        callableList.add(first);
        callableList.add(second);
        callableList.add(third);
        callableList.add(four);
        callableList.add(five);
        return callableList;
    }

    /*private List<Callable<Void>> init() {
        Random random = new Random(60);
        List<Callable<Void>> callableList = new ArrayList<>();
        for (int i = 0; i < elNum; i++) {
            int finalI = i;
            Callable<Void> callable = () -> {
                int sec = random.nextInt();
                System.out.println("should delayed for a " + sec + " sec");
                LocalDateTime localDateTime = LocalDateTime.now(clock).plusSeconds(sec);
                pipeline.push(new SomeAction(finalI), localDateTime);
                return null;
            };

            callableList.add(callable);
        }
        return callableList;
    }*/
}
