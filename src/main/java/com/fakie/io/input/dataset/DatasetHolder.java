package com.fakie.io.input.dataset;

public class DatasetHolder<T> {
    private final T t;

    DatasetHolder(T t) {
        this.t = t;
    }

    public T getDataset() {
        return t;
    }
}
