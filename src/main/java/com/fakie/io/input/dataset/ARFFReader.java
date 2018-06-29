package com.fakie.io.input.dataset;

import com.fakie.io.input.FakieInputException;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

public class ARFFReader implements DataSetReader<Instances> {
    @Override
    public DatasetHolder<Instances> readDataSet(Path path) throws FakieInputException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path.toFile()))) {
            return new DatasetHolder<>(new Instances(bufferedReader));
        }
        catch (IOException e) {
            throw new FakieInputException("Could not read ARFF file at '" + path.toString() + '\'', e);
        }
    }
}
