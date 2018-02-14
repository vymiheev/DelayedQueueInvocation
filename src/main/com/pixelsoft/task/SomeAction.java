package com.pixelsoft.task;

import java.util.concurrent.Callable;

/**
 * Created by mvj on 14.02.2018.
 */
public class SomeAction implements Callable {
    private final int idNum;

    public SomeAction(int idNum) {
        this.idNum = idNum;
    }

    public int getIdNum() {
        return idNum;
    }

    @Override
    public Object call() throws Exception {
        System.out.println("Running " + idNum);
        //Thread.currentThread().sleep(1_000L);
        return idNum;
    }
}
