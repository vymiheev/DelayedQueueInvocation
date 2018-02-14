package com.pixelsoft.task;

import org.junit.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.*;

/**
 * Created by mvj on 14.02.2018.
 */
public class PipelineTest {
    private static final Clock clock = Clock.system(ZoneId.of("Europe/Moscow"));

    @Test
    public void push() throws Exception {
        Pipeline pipeline = new Pipeline();
        try {
            try {
                pushSomeEl(pipeline);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            pipeline.free();
        }
    }

    private void pushSomeEl(Pipeline pipeline) throws Exception {
        List<SomeAction> someActions = new ArrayList<>();

        Callable<Void> first = () -> {
            int sec = 5;
            System.out.println("should delayed for a " + sec + " sec");
            LocalDateTime localDateTime = LocalDateTime.now(clock).plusSeconds(sec);
            SomeAction someAction = spy(new SomeAction(sec));
            someActions.add(someAction);
            pipeline.push(someAction, localDateTime);
            return null;
        };
        Callable<Void> second = () -> {
            int sec = 3;
            System.out.println("should delayed for a " + sec + " sec");
            LocalDateTime localDateTime = LocalDateTime.now(clock).plusSeconds(sec);
            SomeAction someAction = spy(new SomeAction(sec));
            someActions.add(someAction);
            pipeline.push(someAction, localDateTime);
            return null;
        };
        Callable<Void> third = () -> {
            int sec = 1;
            System.out.println("should delayed for a " + sec + " sec");
            LocalDateTime localDateTime = LocalDateTime.now(clock).plusSeconds(sec);
            SomeAction someAction = spy(new SomeAction(sec));
            someActions.add(someAction);
            pipeline.push(someAction, localDateTime);
            return null;
        };
        Callable<Void> four = () -> {
            int sec = 10;
            System.out.println("should delayed for a " + sec + " sec");
            LocalDateTime localDateTime = LocalDateTime.now(clock).plusSeconds(sec);
            SomeAction someAction = spy(new SomeAction(sec));
            someActions.add(someAction);
            pipeline.push(someAction, localDateTime);
            return null;
        };
        Callable<Void> five = () -> {
            int sec = 7;
            System.out.println("should delayed for a " + sec + " sec");
            LocalDateTime localDateTime = LocalDateTime.now(clock).plusSeconds(sec);
            SomeAction someAction = spy(new SomeAction(sec));
            someActions.add(someAction);
            pipeline.push(someAction, localDateTime);
            return null;
        };


        List<Callable<Void>> callableList = new ArrayList<>();
        callableList.add(first);
        callableList.add(second);
        callableList.add(third);
        callableList.add(four);
        callableList.add(five);

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        executorService.invokeAll(callableList);
        Thread.currentThread().sleep(10_000);
        for (SomeAction someAction : someActions) {
            verify(someAction, times(1)).call();
        }
    }

}