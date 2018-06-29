package com.fakie.io.input.dataset;

import com.fakie.io.input.FakieInputException;

import java.nio.file.Path;

public interface DataSetReader<T> {
    DatasetHolder<T> readDataSet(Path path) throws FakieInputException;
}
