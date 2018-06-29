package com.fakie.io.output;

import com.fakie.model.graph.Graph;

import java.nio.file.Path;

public interface GraphDumper {
    void dump(Path path, Graph graph) throws FakieOutputException;
}
