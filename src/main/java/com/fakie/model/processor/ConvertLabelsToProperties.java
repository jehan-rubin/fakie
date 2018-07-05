package com.fakie.model.processor;

import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ConvertLabelsToProperties implements Processor {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public Graph process(Graph graph) throws ProcessingException {
        logger.info("Converting %s labels to properties", graph);
        Graph result = new Graph();
        for (Vertex vertex : graph.getVertices()) {
            Map<String, Object> labeled = new HashMap<>(vertex.getProperties());
            labeled.put(Keyword.LABEL.toString(), vertex.getLabels());
            result.addVertex(new Vertex(vertex.getId(), vertex.getLabels(), labeled));
        }
        return result;
    }
}
