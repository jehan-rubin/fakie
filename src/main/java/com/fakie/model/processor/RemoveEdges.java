package com.fakie.model.processor;

import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RemoveEdges implements Processor {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public Graph process(Graph graph) throws ProcessingException {
        logger.info("Removing edges from %s", graph);
        Graph result = new Graph();
        for (Vertex vertex : graph.getVertices()) {
            result.createVertex(vertex);
        }
        return result;
    }
}
