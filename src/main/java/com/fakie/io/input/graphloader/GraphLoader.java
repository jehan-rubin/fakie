package com.fakie.io.input.graphloader;

import com.fakie.io.input.FakieInputException;
import com.fakie.model.graph.Graph;

public interface GraphLoader extends AutoCloseable {
    Graph load() throws FakieInputException;

    @Override
    void close() throws FakieInputException;
}
