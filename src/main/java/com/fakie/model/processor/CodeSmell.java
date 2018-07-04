package com.fakie.model.processor;

import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;

import java.util.*;

public class CodeSmell implements Processor {
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
        List<Vertex> bestMatches = graph.bestMatches(labels, properties);
        int size = bestMatches.size();
        if (size > 1) {
            throw new ProcessingException("Find too many matches (" + size + ") for " + labels + " " + properties);
        }
        else if (bestMatches.isEmpty()) {
            throw new ProcessingException("Could not find a match for " + labels + " " + properties);
        }
        Vertex match = bestMatches.get(0);
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
        return processed;
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
