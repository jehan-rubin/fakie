package com.fakie.model.processor;

import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class CodeSmell implements Processor {
    private static final Logger logger = LogManager.getFormatterLogger();
    private final List<String> labels;
    private final Map<String, Object> properties;
    private final String name;

    public CodeSmell(List<String> labels, Map<String, Object> properties, String name) {
        this.labels = new ArrayList<>(labels);
        this.properties = new HashMap<>(properties);
        this.name = name;
    }

    @Override
    public Graph process(Graph graph) throws ProcessingException {
        Set<Vertex> bestMatches = bestMatches(graph);
        int size = bestMatches.size();
        if (size > 1) {
            logger.warn("Find too many matches (" + size + ") for \'" + name + "\' " + labels + " " + properties);
            return graph;
        }
        else if (bestMatches.isEmpty()) {
            logger.warn("Could not find a match for \'" + name + "\' " + labels + " " + properties);
            return graph;
        }
        Vertex match = bestMatches.iterator().next();
        Graph processed = new Graph();
        for (Vertex vertex : graph.getVertices()) {
            if (vertex.equals(match)) {
                Map<String, Object> p = vertex.getProperties();
                p.put(formatName(), Boolean.TRUE);
                processed.addVertex(new Vertex(vertex.getId(), vertex.getLabels(), p));
            }
            else {
                processed.addVertex(vertex);
            }
        }
        logger.info("Successfully applied %s on %s", this, match);
        return processed;
    }

    private Set<Vertex> bestMatches(Graph graph) {
        Map<Integer, Set<Vertex>> matches = new HashMap<>();
        for (Vertex vertex : graph.getVertices()) {
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
            matches.putIfAbsent(match, new HashSet<>());
            matches.get(match).add(vertex);
        }
        Optional<Integer> max = matches.keySet().stream().max(Integer::compareTo);
        if (max.isPresent()) {
            return matches.get(max.get());
        }
        return new HashSet<>();
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
