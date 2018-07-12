package com.fakie.model.processor;

import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;

public class RemoveEdges implements Processor {
    @Override
    public Graph process(Graph graph) throws ProcessingException {
        Graph result = new Graph();
        for (Vertex vertex : graph.getVertices()) {
            result.createVertex(vertex);
        }
        return result;
    }
}
