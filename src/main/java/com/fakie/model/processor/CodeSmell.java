package com.fakie.model.processor;

import com.fakie.model.graph.Element;
import com.fakie.model.graph.Graph;
import com.fakie.utils.Keyword;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class CodeSmell implements Processor {
    private static final Logger logger = LogManager.getFormatterLogger();
    private final List<String> labels;
    private final Map<String, Object> properties;
    private final String name;

    public CodeSmell(List<String> labels, Map<String, Object> properties, String name) {
        this.labels = new ArrayList<>(labels);
        this.properties = new HashMap<>(properties);
        this.name = name.replace(" ", Keyword.SPLIT.toString());
    }

    @Override
    public Graph process(Graph graph) throws ProcessingException {
        Set<Element> bestMatches = filterElements(graph);
        int size = bestMatches.size();
        if (size > 1) {
            logger.debug("Find too many matches (" + size + ") for \'" + name + "\' " + labels + " " + properties);
            return graph;
        }
        else if (bestMatches.isEmpty()) {
            logger.debug("Could not find a match for \'" + name + "\' " + labels + " " + properties);
            return graph;
        }
        Element match = bestMatches.iterator().next();
        match.setProperty(Keyword.CODE_SMELL.toString(), name);
        logger.debug("Successfully applied %s on %s", this, match);
        return graph;
    }

    private Set<Element> filterElements(Graph graph) {
        Set<Element> elements = null;
        for (Map.Entry<String, Object> property : properties.entrySet()) {
            Set<Element> temp;
            switch (graph.type(property.getKey())) {
                case BOOLEAN:
                    temp = graph.find(property.getKey(), Boolean.valueOf(property.getValue().toString()));
                    break;
                case DOUBLE:
                    temp = graph.find(property.getKey(), Double.valueOf(property.getValue().toString()));
                    break;
                case INTEGER:
                    temp = graph.find(property.getKey(), Integer.valueOf(property.getValue().toString()));
                    break;
                default:
                    temp = graph.find(property.getKey(), property.getValue());
            }
            if (!temp.isEmpty()) {
                if (elements == null) {
                    elements = temp;
                } else {
                    elements.retainAll(temp);
                }
            }
            if (elements != null && elements.isEmpty()) {
                return elements;
            }
        }
        return elements == null ? new HashSet<>() : elements;
    }

    @Override
    public String toString() {
        return "CodeSmell{labels=" + labels + ", properties=" + properties + ", name='" + name + "\'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CodeSmell codeSmell = (CodeSmell) o;
        return Objects.equals(labels, codeSmell.labels) &&
                Objects.equals(properties, codeSmell.properties) &&
                Objects.equals(name, codeSmell.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(labels, properties, name);
    }
}
