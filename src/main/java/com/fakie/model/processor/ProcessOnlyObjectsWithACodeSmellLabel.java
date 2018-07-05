package com.fakie.model.processor;

import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Set;

public class ProcessOnlyObjectsWithACodeSmellLabel implements Processor {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public Graph process(Graph graph) throws ProcessingException {
        logger.info("Keep only objects in %s with the same label than a code smell", graph);
        Graph temp = new Graph();
        for (Vertex vertex : graph.getVertices()) {
            if (containsACodeSmell(vertex)) {
                temp.addVertex(vertex);
            }
        }
        for (String label : temp.getLabels()) {
            Set<Vertex> verticesWithLabel = graph.getVerticesWithLabel(label);
            for (Vertex vertex : verticesWithLabel) {
                temp.addVertex(vertex);
            }
        }
        return temp;
    }

    private boolean containsACodeSmell(Vertex vertex) {
        for (Map.Entry<String, Object> property : vertex.getProperties().entrySet()) {
            if (isACodeSmell(property.toString())) {
                return true;
            }
        }
        return false;
    }

    private boolean isACodeSmell(String string) {
        return string.startsWith(Keyword.CODE_SMELL.toString());
    }
}
