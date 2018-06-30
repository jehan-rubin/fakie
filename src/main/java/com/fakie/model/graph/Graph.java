package com.fakie.model.graph;

import java.util.*;

public class Graph {
    private final List<Vertex> vertices = new ArrayList<>();
    private final List<Edge> edges = new ArrayList<>();
    private final Map<String, Set<Object>> properties = new HashMap<>();
    private final Map<String, Set<Vertex>> labels = new HashMap<>();

    public void addVertex(Vertex vertex) {
        vertices.add(vertex);
        addLabels(vertex);
        addProperties(vertex);
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
        addProperties(edge);
    }

    public Set<Object> getVerticesWithLabel(String label) {
        return new HashSet<>(labels.get(label));
    }

    public List<Vertex> getVertices() {
        return new ArrayList<>(vertices);
    }

    public List<Edge> getEdges() {
        return new ArrayList<>(edges);
    }

    public Map<String, Set<Object>> getProperties() {
        Map<String, Set<Object>> result = new HashMap<>();
        for (Map.Entry<String, Set<Object>> property : properties.entrySet()) {
            result.put(property.getKey(), new HashSet<>(property.getValue()));
        }
        return result;
    }

    public Set<String> getLabels() {
        return new HashSet<>(labels.keySet());
    }

    private void addLabels(Vertex vertex) {
        for (String label : vertex.getLabels()) {
            labels.putIfAbsent(label, new HashSet<>());
            labels.get(label).add(vertex);
        }
    }

    private void addProperties(Element element) {
        for (Map.Entry<String, Object> property : element.getProperties().entrySet()) {
            properties.putIfAbsent(property.getKey(), new HashSet<>());
            properties.get(property.getKey()).add(property.getValue());
        }
    }
}
