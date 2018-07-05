package com.fakie.model.processor;

import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RemovePropertiesWithASingleValue implements Processor {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public Graph process(Graph graph) throws ProcessingException {
        logger.info("Removing properties with a single value from %s", graph);
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
            boolean isLabel = property.getKey().startsWith(Keyword.LABEL.toString());
            boolean isCodeSmell = property.getKey().startsWith(Keyword.CODE_SMELL.toString());
            if (!isLabel && !isCodeSmell && property.getValue().size() <= 1) {
                result.add(property.getKey());
            }
        }
        return result;
    }
}
