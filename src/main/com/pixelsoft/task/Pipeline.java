package com.pixelsoft.task;

import java.time.LocalDateTime;
import java.util.PriorityQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class Pipeline {
    private static final int DEFAULT_CONSUMER_THREAD_COUNT = 20;
    private final ReentrantLock lock = new ReentrantLock();
    private final PriorityQueue<DelayedTask> q = new PriorityQueue<>();
    private final Condition available = lock.newCondition();
    private ExecutorService executorService;
    private Thread checkingThread;

    public Pipeline(int consumerThreadCount) {
        executorService = Executors.newFixedThreadPool(consumerThreadCount);
        checkingThread = new Thread(() -> {
            try {
                startGoldenAntilope();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        checkingThread.start();
    }

    public Pipeline() {
        this(DEFAULT_CONSUMER_THREAD_COUNT);
    }

    public boolean push(Callable callable, LocalDateTime localDateTime) {
        DelayedTask delayedTask = new DelayedTask(localDateTime, callable);
        lock.lock();
        try {
            q.offer(delayedTask);
            available.signal();
            return true;
        } finally {
            lock.unlock();
        }
    }

    private void startGoldenAntilope() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            while (true) {
                DelayedTask first = q.peek();
                if (first == null) {
                    available.await();
                } else {
                    long delay = first.getDelay(NANOSECONDS);
                    if (delay <= 0) {
                        DelayedTask delayedTask = q.poll();
                        executorService.submit(delayedTask.getCallable());
                    } else {
                        available.awaitNanos(delay);
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void free() {
        checkingThread.interrupt();
        executorService.shutdown();
    }


}
