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
        Graph temp = new Graph();
        for (Vertex vertex : graph.getVertices()) {
            if (containsACodeSmell(vertex)) {
                temp.addVertex(convertVertex(vertex));
            }
        }
        for (String label : temp.getLabels()) {
            Set<Vertex> verticesWithLabel = graph.getVerticesWithLabel(label);
            for (Vertex vertex : verticesWithLabel) {
                temp.addVertex(convertVertex(vertex));
            }
        }
        Graph complete = fillProperties(temp);
        logger.debug("Complete graph " + graph);
        Graph clean = removeUselessProperties(complete);
        logger.debug("Cleaned graph " + graph);
        return clean;
    }

    private Graph removeUselessProperties(Graph graph) {
        Map<String, Set<Object>> properties = graph.getProperties();
        Set<String> uselessProperties = findUselessProperties(properties);
        Graph result = new Graph();
        for (Vertex vertex : graph.getVertices()) {
            Map<String, Object> normalized = new HashMap<>(vertex.getProperties());
            for (String uselessProperty : uselessProperties) {
                normalized.remove(uselessProperty);
            }
            result.addVertex(new Vertex(vertex.getId(), vertex.getLabels(), normalized));
        }
        return result;
    }

    private Set<String> findUselessProperties(Map<String, Set<Object>> properties) {
        Set<String> result = new HashSet<>();
        for (Map.Entry<String, Set<Object>> property : properties.entrySet()) {
            if (property.getValue().size() <= 1) {
                result.add(property.getKey());
            }
        }
        return result;
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
