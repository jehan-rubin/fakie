package com.fakie.model.processor;

import com.fakie.model.graph.Graph;
import com.fakie.utils.exceptions.FakieException;

public interface Processor {
    Graph process(Graph graph) throws FakieException;
}
