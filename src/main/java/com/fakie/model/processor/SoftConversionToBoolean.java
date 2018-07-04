package com.fakie.model.processor;

import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class SoftConversionToBoolean implements Processor {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public Graph process(Graph graph) {
        logger.info("Softly converting %s properties to boolean", graph);
        Graph converted = new Graph();
        for (Vertex vertex : graph.getVertices()) {
            if (containsACodeSmell(vertex)) {
                converted.addVertex(convertVertex(vertex));
            }
        }
        Map<String, Set<Object>> properties = converted.getProperties();
        properties.put(Keyword.LABEL.toString(), new HashSet<>(converted.getLabels()));
        fillProperties(converted, properties);
        return converted;
    }

    private void fillProperties(Graph graph, Map<String, Set<Object>> properties) {
        for (Vertex vertex : graph.getVertices()) {
            for (Map.Entry<String, Set<Object>> property : properties.entrySet()) {
                for (Object value : property.getValue()) {
                    vertex.addProperty(format(property.getKey(), value), Boolean.FALSE);
                }
            }
        }
    }

    private Vertex convertVertex(Vertex vertex) {
        logger.debug("Converting " + vertex);
        Map<String, Boolean> booleanProperties = mapNominalToBoolean(vertex);
        addLabelsToProperties(booleanProperties, vertex.getLabels());
        return new Vertex(vertex.getId(), vertex.getLabels(), booleanProperties);
    }

    private void addLabelsToProperties(Map<String, Boolean> booleanProperties, List<String> labels) {
        logger.trace("add labels to properties");
        for (String label : labels) {
            booleanProperties.put(format(Keyword.LABEL.toString(), label), Boolean.TRUE);
        }
    }

    private Map<String, Boolean> mapNominalToBoolean(Vertex vertex) {
        Map<String, Boolean> booleanProperties = new HashMap<>();
        for (Map.Entry<String, Object> property : vertex.getProperties().entrySet()) {
            booleanProperties.put(format(property.getKey(), property.getValue()), Boolean.TRUE);
        }
        return booleanProperties;
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

    private String format(String property, Object value) {
        return String.format(Keyword.IS.toString(), property, value);
    }
}
