package com.fakie.model.processor;

import com.fakie.model.graph.Vertex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class CodeSmell {
    private static final Logger logger = LogManager.getFormatterLogger();
    private final List<String> labels;
    private final Map<String, Object> properties;
    private final String name;

    public CodeSmell(List<String> labels, Map<String, Object> properties, String name) {
        this.labels = new ArrayList<>(labels);
        this.properties = new HashMap<>(properties);
        this.name = name;
    }

    public List<Vertex> process(List<Vertex> vertices) {
        Set<Vertex> bestMatches = bestMatches(vertices);
        int size = bestMatches.size();
        if (size > 1) {
            logger.debug("Find too many matches (" + size + ") for \'" + name + "\' " + labels + " " + properties);
            return vertices;
        }
        else if (bestMatches.isEmpty()) {
            logger.debug("Could not find a match for \'" + name + "\' " + labels + " " + properties);
            return vertices;
        }
        Vertex match = bestMatches.iterator().next();
        vertices.remove(match);
        Map<String, Object> matchProperties = match.getProperties();
        matchProperties.put(formatName(), Boolean.TRUE);
        vertices.add(new Vertex(match.getId(), match.getLabels(), matchProperties));
        logger.debug("Successfully applied %s on %s", this, match);
        return vertices;
    }

    private Set<Vertex> bestMatches(List<Vertex> vertices) {
        int max = 0;
        Set<Vertex> matches = new HashSet<>();
        for (Vertex vertex : vertices) {
            int match = 0;
            List<String> vertexLabels = vertex.getLabels();
            for (String label : this.labels) {
                if (vertexLabels.contains(label)) {
                    match += 1;
                }
            }
            List<String> values = vertex.getProperties().values().stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
            for (Object o : properties.values()) {
                if (values.contains(o.toString())) {
                    match += 1;
                }
            }
            if (match == max) {
                matches.add(vertex);
            } else if (match > max) {
                max = match;
                matches = new HashSet<>();
                matches.add(vertex);
            }
        }
        return matches;
    }

    private String formatName() {
        return Keyword.CODE_SMELL.toString()
                .concat(Keyword.SEPARATOR.toString())
                .concat(name.replace(" ", Keyword.SEPARATOR.toString()));
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
