package com.fakie.model.processor;

import com.fakie.model.FakieModelException;
import com.fakie.model.graph.Graph;

public interface Processor {
    Graph process(Graph graph) throws FakieModelException;
}
