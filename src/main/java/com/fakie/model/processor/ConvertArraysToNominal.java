package com.fakie.model.processor;

import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class ConvertArraysToNominal implements Processor {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public Graph process(Graph graph) throws ProcessingException {
        logger.info("Converting %s arrays to nominal", graph);
        Graph result = new Graph();
        for (Vertex vertex : graph.getVertices()) {
            Map<String, Object> properties = new HashMap<>(vertex.getProperties());
            for (Map.Entry<String, Object> property : vertex.getProperties().entrySet()) {
                Object value = property.getValue();
                if (value instanceof Collection) {
                    properties.remove(property.getKey());
                    Object[] objects = ((Collection) value).toArray(new Object[0]);
                    for (int i = 0; i < objects.length; i++) {
                        properties.put(format(property.getKey(), i), objects[i]);
                    }
                }
            }
            result.addVertex(new Vertex(vertex.getId(), vertex.getLabels(), properties));
        }
        return result;
    }

    private String format(String key, int position) {
        return String.format(Keyword.SPLIT.toString(), key, position);
    }
}
