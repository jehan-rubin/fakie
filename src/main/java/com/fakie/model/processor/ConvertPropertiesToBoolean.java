package com.fakie.model.processor;

import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConvertPropertiesToBoolean implements Processor {
    private static final String FORMAT = "%s_is_%s";

    @Override
    public Graph process(Graph graph) {
        Graph converted = new Graph();
        Map<String, Set<Object>> properties = graph.getProperties();
        for (Vertex vertex : graph.getVertices()) {
            converted.addVertex(convertVertex(properties, vertex));
        }
        return converted;
    }

    private Vertex convertVertex(Map<String, Set<Object>> properties, Vertex vertex) {
        Map<String, Boolean> booleanProperties = mapNominalToBoolean(properties, vertex);
        fillEmptyProperties(properties, booleanProperties);
        return new Vertex(vertex.getId(), vertex.getLabels(), booleanProperties);
    }

    private Map<String, Boolean> mapNominalToBoolean(Map<String, Set<Object>> properties, Vertex vertex) {
        Map<String, Boolean> booleanProperties = new HashMap<>();
        for (Map.Entry<String, Object> property : vertex.getProperties().entrySet()) {
            Set<Object> values = properties.get(property.getKey());
            for (Object value : values) {
                booleanProperties.put(format(property.getKey(), value), value.equals(property.getValue()));
            }
        }
        return booleanProperties;
    }

    private void fillEmptyProperties(Map<String, Set<Object>> properties, Map<String, Boolean> booleanProperties) {
        for (Map.Entry<String, Set<Object>> property : properties.entrySet()) {
            for (Object value : property.getValue()) {
                booleanProperties.putIfAbsent(format(property.getKey(), value), Boolean.FALSE);
            }
        }
    }

    private String format(String property, Object value) {
        return String.format(FORMAT, property, value);
    }
}
