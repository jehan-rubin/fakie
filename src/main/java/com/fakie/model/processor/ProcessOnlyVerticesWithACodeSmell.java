package com.fakie.model.processor;

import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;
import com.fakie.utils.FakieUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProcessOnlyVerticesWithACodeSmell implements Processor {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public Graph process(Graph graph) throws ProcessingException {
        logger.info("Keep only objects in %s with a code smell", graph);
        for (Vertex vertex : graph.getVertices()) {
            if (!FakieUtils.containsACodeSmell(vertex)) {
                graph.remove(vertex);
            }
        }
        return graph;
    }
}
