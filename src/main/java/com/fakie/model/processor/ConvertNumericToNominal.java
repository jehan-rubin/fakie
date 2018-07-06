package com.fakie.model.processor;

import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class ConvertNumericToNominal implements Processor {
    private static final Logger logger = LogManager.getFormatterLogger();
    private static final int MIN_VALUES = 4;

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
        Map<String, Object> nominalProperties = mapNumericToNominal(properties, vertex);
        return new Vertex(vertex.getId(), vertex.getLabels(), nominalProperties);
    }

    private Map<String, Object> mapNumericToNominal(Map<String, Set<Object>> properties, Vertex vertex) {
        Map<String, Object> nominalProperties = new HashMap<>();
        for (Map.Entry<String, Object> property : vertex.getProperties().entrySet()) {
            if (property.getValue() instanceof Number) {
                Object value = covertNumericToNominal(properties.get(property.getKey()), (Number) property.getValue());
                nominalProperties.put(property.getKey(), value);
            }
            else {
                nominalProperties.put(property.getKey(), property.getValue());
            }
        }
        return nominalProperties;
    }

    private Object covertNumericToNominal(Set<Object> values, Number value) {
        List<Number> numbers = new ArrayList<>();
        for (Object o : values) {
            if (o instanceof Number) {
                numbers.add(((Number) o));
            }
        }
        numbers.sort((n1, n2) -> Double.compare(n1.doubleValue(), n2.doubleValue()));
        return partition(value, numbers);
    }

    private String partition(Number value, List<Number> numbers) {
        int i = numbers.indexOf(value);
        int n = numbers.size();
        if (n < MIN_VALUES) {
            return value.toString();
        }
        int m = (int) Math.sqrt(n);
        int low = n / m;
        if (i < low) {
            return String.format(Keyword.BELOW.toString(), numbers.get(low));
        }
        for (int j = 2; j < m - 1; j++) {
            int q = n * j / m;
            if (i < q) {
                return String.format(Keyword.BETWEEN.toString(), numbers.get(j - 1), numbers.get(j));
            }
        }
        int high = n * (m - 1) / m;
        return String.format(Keyword.ABOVE.toString(), numbers.get(high));
    }
}
