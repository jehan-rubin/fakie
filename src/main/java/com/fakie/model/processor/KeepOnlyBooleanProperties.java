package com.fakie.model.processor;

import com.fakie.model.graph.*;

public class KeepOnlyBooleanProperties implements Processor {
    @Override
    public Graph process(Graph graph) throws ProcessingException {
        Graph result = new Graph();
        for (Vertex vertex : graph.getVertices()) {
            Vertex v = result.createVertex(vertex.getLabels());
            for (Property property : vertex) {
                if (property.getType() == Type.BOOLEAN) {
                    v.setProperty(property.getKey(), property.getValue());
                }
            }
        }
        return result;
    }
}
