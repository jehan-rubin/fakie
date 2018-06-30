package com.fakie.io.input.dataset;

import com.fakie.io.input.FakieInputException;

import java.nio.file.Path;

public interface DatasetReader<T> {
    DatasetHolder<T> readDataset(Path path) throws FakieInputException;
}
