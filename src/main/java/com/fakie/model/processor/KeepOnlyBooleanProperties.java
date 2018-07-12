package com.fakie.model.processor;

import com.fakie.model.graph.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class KeepOnlyBooleanProperties implements Processor {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public Graph process(Graph graph) throws ProcessingException {
        logger.info("Keep only boolean properties");
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
