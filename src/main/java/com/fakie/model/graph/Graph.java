package com.fakie.model.graph;

import java.util.*;

public class Graph {
    private final List<Vertex> vertices;
    private final List<Edge> edges;
    private final Map<String, Set<Object>> properties;
    private final Map<String, Set<Vertex>> labels;

    public Graph() {
        this(new ArrayList<>(), new ArrayList<>(), new HashMap<>(), new HashMap<>());
    }

    private Graph(List<Vertex> v, List<Edge> e, Map<String, Set<Object>> p, Map<String, Set<Vertex>> l) {
        this.vertices = new ArrayList<>(v);
        this.edges = new ArrayList<>(e);
        this.properties = new HashMap<>(p);
        this.labels = new HashMap<>(l);
    }

    public Graph(Graph graph) {
        this(graph.vertices, graph.edges, graph.properties, graph.labels);
    }

    public void addVertex(Vertex vertex) {
        vertices.add(vertex);
        addLabels(vertex);
        addProperties(vertex);
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
        addProperties(edge);
    }

    public Set<Vertex> getVerticesWithLabel(String label) {
        return new HashSet<>(labels.get(label));
    }

    public List<Vertex> bestMatches(List<String> labels, Map<String, Object> properties) {
        List<Vertex> bestMatches = new ArrayList<>();
        for (Vertex vertex : vertices) {
            if (vertex.getLabels().containsAll(labels) &&
                    vertex.getProperties().entrySet().containsAll(properties.entrySet())) {
                bestMatches.add(vertex);
            }
        }
        return bestMatches;
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

    @Override
    public String toString() {
        return "Graph{vertices=" + vertices.size() + ", edges=" + edges.size() + "}";
    }
}
