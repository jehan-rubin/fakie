package com.fakie.model.processor;

import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class ConvertNumericToThreshold implements Processor {
    private static final Logger logger = LogManager.getFormatterLogger();
    private static final int MIN_VALUES = 3;

    @Override
    public Graph process(Graph graph) throws ProcessingException {
        logger.info("Converting %s numeric to threshold", graph);
        Graph converted = new Graph();
        Map<String, Set<Object>> properties = graph.getProperties();
        for (Vertex vertex : graph.getVertices()) {
            converted.addVertex(convertVertex(properties, vertex));
        }
        return converted;
    }

    private Vertex convertVertex(Map<String, Set<Object>> properties, Vertex vertex) {
        logger.debug("Converting " + vertex);
        Map<String, Object> thresholdProperties = mapNumericToThreshold(properties, vertex);
        return new Vertex(vertex.getId(), vertex.getLabels(), thresholdProperties);
    }

    private Map<String, Object> mapNumericToThreshold(Map<String, Set<Object>> properties, Vertex vertex) {
        Map<String, Object> thresholdProperties = new HashMap<>();
        for (Map.Entry<String, Object> property : vertex.getProperties().entrySet()) {
            if (canBeConverted(properties, property)) {
                addNumericAsThreshold(thresholdProperties, property, properties.get(property.getKey()));
            }
            else {
                thresholdProperties.put(property.getKey(), property.getValue());
            }
        }
        return thresholdProperties;
    }

    private boolean canBeConverted(Map<String, Set<Object>> properties, Map.Entry<String, Object> property) {
        return property.getValue() instanceof Number && properties.get(property.getKey()).size() >= MIN_VALUES;
    }

    private void addNumericAsThreshold(Map<String, Object> thresholdProperties, Map.Entry<String, Object> property,
                                       Set<Object> values) {
        List<Pair> thresholds = covertNumericToThreshold(values, (Number) property.getValue());
        for (Pair pair : thresholds) {
            thresholdProperties.put(Keyword.IS.format(property.getKey(), pair.keyword), pair.number);
        }
    }

    private List<Pair> covertNumericToThreshold(Set<Object> values, Number value) {
        List<Number> numbers = new ArrayList<>();
        for (Object o : values) {
            if (o instanceof Number) {
                numbers.add(((Number) o));
            }
        }
        numbers.sort((n1, n2) -> Double.compare(n1.doubleValue(), n2.doubleValue()));
        return partition(value, numbers);
    }

    private List<Pair> partition(Number value, List<Number> numbers) {
        List<Pair> keys = new ArrayList<>();
        int i = numbers.indexOf(value);
        int n = numbers.size();
        int m = (int) Math.sqrt(n);
        for (int j = 1; j < m; j++) {
            int q = n * j / m;
            keys.add(new Pair(numbers.get(q), i <= q ? Keyword.BELOW : Keyword.ABOVE));
        }
        return keys;
    }

    private class Pair {
        private final Number number;
        private final Keyword keyword;

        public Pair(Number number, Keyword keyword) {
            this.number = number;
            this.keyword = keyword;
        }
    }
}
