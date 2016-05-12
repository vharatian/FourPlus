package com.anashidgames.gerdoo.core.service;

import java.util.Objects;

/**
 * Created by psycho on 5/12/16.
 */
public abstract class PsychoRunnable<T> implements Runnable {
    private T[] inputs;

    public PsychoRunnable(T... inputs) {
        this.inputs = inputs;
    }

    public abstract void run(T[] inputs);

    @Override
    public void run() {
        run(inputs);
    }
}
