package com.fakie.io.input.graphloader;

import com.fakie.io.input.FakieInputException;
import com.fakie.model.graph.Graph;

import java.nio.file.Path;

public interface GraphLoader extends AutoCloseable {
    Graph load(Path path) throws FakieInputException;

    @Override
    void close() throws FakieInputException;
}
