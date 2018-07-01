package com.fakie.io.input.dataset;

import com.fakie.io.input.FakieInputException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

public class ARFFReader implements DatasetReader<Instances> {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public Instances readDataset(Path path) throws FakieInputException {
        logger.info("Reading dataset at \'%s\'", path);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path.toFile()))) {
            Instances instances = new Instances(bufferedReader);
            logger.info("Dataset has been successfully loaded");
            logger.debug("Instances = %s", instances);
            return instances;
        }
        catch (IOException e) {
            throw new FakieInputException("Could not read ARFF file at '" + path.toString() + '\'', e);
        }
    }
}
