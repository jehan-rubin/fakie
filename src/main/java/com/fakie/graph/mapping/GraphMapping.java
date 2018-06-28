package com.fakie.graph.mapping;

import com.fakie.graph.model.Graph;

public interface GraphMapping extends AutoCloseable {
    Graph convertToGraph();
}
