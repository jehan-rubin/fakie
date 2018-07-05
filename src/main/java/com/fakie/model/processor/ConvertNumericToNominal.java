package com.fakie.model.processor;

import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.Key;
import java.util.*;

public class ConvertNumericToNominal implements Processor {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public Graph process(Graph graph) throws ProcessingException {
        logger.info("Converting %s properties to nominal", graph);
        Graph converted = new Graph();
        Map<String, Set<Object>> properties = graph.getProperties();
        for (Vertex vertex : graph.getVertices()) {
            converted.addVertex(convertVertex(properties, vertex));
        }
        return converted;
    }

    private Vertex convertVertex(Map<String, Set<Object>> properties, Vertex vertex) {
        logger.debug("Converting " + vertex);
        Map<String, String> nominalProperties = mapNumericToNominal(properties, vertex);
        return new Vertex(vertex.getId(), vertex.getLabels(), nominalProperties);
    }

    private Map<String, String> mapNumericToNominal(Map<String, Set<Object>> properties, Vertex vertex) {
        Map<String, String> nominalProperties = new HashMap<>();
        for (Map.Entry<String, Object> property : vertex.getProperties().entrySet()) {
            if (property.getValue() instanceof Number) {
                String value = covertNumericToNominal(properties.get(property.getKey()), (Number) property.getValue());
                nominalProperties.put(property.getKey(), value);
            }
            else {
                nominalProperties.put(property.getKey(), property.getValue().toString());
            }
        }
        return nominalProperties;
    }

    private String covertNumericToNominal(Set<Object> values, Number value) {
        List<Number> numbers = new ArrayList<>();
        for (Object o : values) {
            if (o instanceof Number) {
                numbers.add(((Number) o));
            }
        }
        numbers.sort((n1, n2) -> Double.compare(n1.doubleValue(), n2.doubleValue()));
        int i = numbers.indexOf(value);
        int n = numbers.size();
        return i > n / 2 ? Keyword.HIGH.toString() : Keyword.LOW.toString();
    }
}
