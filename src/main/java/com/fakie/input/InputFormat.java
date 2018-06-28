package com.fakie.input;

import com.fakie.graph.Graph;

public interface InputFormat extends AutoCloseable {
    Graph convertToGraph();
}
