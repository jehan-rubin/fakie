package com.fakie.model.processor;

import com.fakie.model.graph.Graph;

public interface Processor {
    Graph process(Graph graph) throws ProcessingException;
}
