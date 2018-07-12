package com.fakie.model.processor;

import com.fakie.model.graph.Element;
import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Property;
import com.fakie.model.graph.Type;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConvertNumericToBoolean implements Processor {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public Graph process(Graph graph) throws ProcessingException {
        logger.info("Converting %s numeric to nominal", graph);
        Map<String, Double> thresholdMap = createThresholdMap(graph);
        for (Element element : graph.getElements()) {
            for (Property property : element) {
                if (property.getType() == Type.NUMBER) {
                    convertNumericToNominal(thresholdMap, element, property);
                }
            }
        }
        return graph;
    }

    private void convertNumericToNominal(Map<String, Double> thresholdMap, Element element, Property property) {
        double threshold = thresholdMap.get(property.getKey());
        String key = Keyword.ABOVE.format(property.getKey(), threshold);
        element.setProperty(key, property.<Number>getValue().doubleValue() >= threshold);
    }

    private Map<String, Double> createThresholdMap(Graph graph) {
        Map<String, Double> thresholdMap = new HashMap<>();
        for (String key : graph.keys()) {
            if (graph.type(key) == Type.NUMBER) {
                thresholdMap.put(key, computeThreshold(valuesToDoubleArray(graph.values(key))));
            }
        }
        return thresholdMap;
    }

    private double[] valuesToDoubleArray(List<Object> values) {
        return values.stream()
                .map(o -> (Number) o).mapToDouble(Number::doubleValue)
                .sorted().toArray();
    }

    private Double computeThreshold(double... values) {
        double q1 = StatUtils.percentile(values, 25);
        double q3 = StatUtils.percentile(values, 75);
        return q3 + 1.5 * (q3 - q1);
    }
}
