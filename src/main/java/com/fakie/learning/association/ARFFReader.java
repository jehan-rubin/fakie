package com.fakie.learning.association;

import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

public class ARFFReader implements InstancesCreator {
    private final Path filepath;

    public ARFFReader(Path filepath) {
        this.filepath = filepath;
    }

    public Instances createInstances() throws CreatingInstancesException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath.toFile()))) {
            return new Instances(bufferedReader);
        }
        catch (IOException e) {
            throw new CreatingInstancesException("Could not read ARFF file at '" + filepath.toString() + '\'', e);
        }
    }
}
