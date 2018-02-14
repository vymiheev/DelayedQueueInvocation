package com.pixelsoft.task;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayedTask<P> implements Delayed {
    private static final Clock clock = Clock.system(ZoneId.of("Europe/Moscow"));
    private LocalDateTime dateTime;
    private Callable<P> callable;

    public DelayedTask(LocalDateTime dateTime, Callable<P> callable) {
        this.dateTime = dateTime;
        this.callable = callable;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Callable<P> getCallable() {
        return callable;
    }

    public void setCallable(Callable<P> callable) {
        this.callable = callable;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        LocalDateTime nowLocalDateTime = LocalDateTime.now(clock);
        return unit.convert(Duration.between(nowLocalDateTime, dateTime).toNanos(), TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        if (o instanceof DelayedTask) {
            return dateTime.compareTo(((DelayedTask) o).getDateTime());
        }
        throw new UnsupportedOperationException("Should be instance of " + this.getClass().getSimpleName());
    }
}
