package com.fakie.model.processor;

import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class ConvertNominalToBoolean implements Processor {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public Graph process(Graph graph) {
        logger.info("Converting %s properties to boolean", graph);
        Graph temp = new Graph();
        for (Vertex vertex : graph.getVertices()) {
            temp.addVertex(convertVertex(vertex));
        }
        Graph complete = fillProperties(temp);
        logger.debug("Complete graph " + graph);
        return complete;
    }

    private Graph fillProperties(Graph graph) {
        Map<String, Set<Object>> properties = graph.getProperties();
        properties.put(Keyword.LABEL.toString(), new HashSet<>(graph.getLabels()));
        Graph result = new Graph();
        for (Vertex vertex : graph.getVertices()) {
            Map<String, Object> normalized = new HashMap<>(vertex.getProperties());
            for (Map.Entry<String, Set<Object>> property : properties.entrySet()) {
                normalized.putIfAbsent(property.getKey(), Boolean.FALSE);
            }
            result.addVertex(new Vertex(vertex.getId(), vertex.getLabels(), normalized));
        }
        return result;
    }

    private Vertex convertVertex(Vertex vertex) {
        logger.debug("Converting " + vertex);
        Map<String, Boolean> booleanProperties = mapNominalToBoolean(vertex);
        return new Vertex(vertex.getId(), vertex.getLabels(), booleanProperties);
    }

    private Map<String, Boolean> mapNominalToBoolean(Vertex vertex) {
        Map<String, Boolean> booleanProperties = new HashMap<>();
        for (Map.Entry<String, Object> property : vertex.getProperties().entrySet()) {
            if (property.getValue() instanceof Boolean) {
                booleanProperties.put(property.getKey(), ((Boolean) property.getValue()));
            } else {
                booleanProperties.put(format(property.getKey(), property.getValue()), Boolean.TRUE);
            }
        }
        return booleanProperties;
    }

    private String format(String property, Object value) {
        return String.format(Keyword.IS.toString(), property, value);
    }
}
