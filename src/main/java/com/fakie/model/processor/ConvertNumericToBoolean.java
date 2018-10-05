package com.fakie.model.processor;

import com.fakie.model.graph.Element;
import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Property;
import com.fakie.utils.Keyword;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConvertNumericToBoolean implements Processor {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public Graph process(Graph graph) {
        logger.info("Converting %s numeric to nominal", graph);
        Map<String, List<Threshold>> thresholdMap = createThresholdMap(graph);
        for (Element element : graph.getElements()) {
            for (Property property : element) {
                if (property.getType().isNumber()) {
                    convertNumericToNominal(thresholdMap, element, property);
                    element.removeProperty(property);
                }
            }
        }
        return graph;
    }

    private void convertNumericToNominal(Map<String, List<Threshold>> thresholdMap, Element element, Property
            property) {
        for (Threshold threshold : thresholdMap.get(property.getKey())) {
            element.setProperty(threshold.format(property), threshold.test(property));
        }
    }

    private Map<String, List<Threshold>> createThresholdMap(Graph graph) {
        Map<String, List<Threshold>> thresholdMap = new HashMap<>();
        for (String key : graph.keys()) {
            if (graph.type(key).isNumber()) {
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

    private List<Threshold> computeThreshold(double... values) {
        List<Threshold> thresholds = new ArrayList<>();
        double q1 = StatUtils.percentile(values, 25);
        double q3 = StatUtils.percentile(values, 75);
        //thresholds.add(new Threshold(q3 + .75 * (q3 - q1)));
        thresholds.add(new Threshold(q3 + 1.5 * (q3 - q1)));
        //thresholds.add(new Threshold(q3 + 3 * (q3 - q1)));
        //thresholds.add(new Threshold(q3 + 4.5 * (q3 - q1)));
        return thresholds;
    }

    private static class Threshold {
        private final double value;

        private Threshold(double value) {
            this.value = value;
        }

        public String format(Property property) {
            return Keyword.ABOVE.format(property.getKey(), value);
        }

        public Boolean test(Property property) {
            return property.<Number>getValue().doubleValue() >= value;
        }
    }
}
