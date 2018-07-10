package com.fakie.model.graph;

import java.util.*;
import java.util.stream.Collectors;

public class Graph {
    private final Set<Vertex> vertices;
    private final Set<Edge> edges;
    private final Map<String, Set<Object>> properties;
    private final Map<String, Set<Vertex>> labels;

    public Graph() {
        this(new HashSet<>(), new HashSet<>(), new HashMap<>(), new HashMap<>());
    }

    private Graph(Set<Vertex> v, Set<Edge> e, Map<String, Set<Object>> p, Map<String, Set<Vertex>> l) {
        this.vertices = new HashSet<>(v);
        this.edges = new HashSet<>(e);
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

    public void addVertices(List<Vertex> vertices) {
        for (Vertex vertex : vertices) {
            addVertex(vertex);
        }
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
        addProperties(edge);
    }

    public Set<Vertex> getVerticesWithLabel(String label) {
        return new HashSet<>(labels.get(label));
    }

    public List<Vertex> matches(List<String> labels, Map<String, Object> properties) {
        List<Vertex> bestMatches = new ArrayList<>();
        for (Vertex vertex : getVertices()) {
            if (vertex.getLabels().containsAll(labels) &&
                    vertex.getProperties().entrySet().containsAll(properties.entrySet())) {
                bestMatches.add(vertex);
            }
        }
        return bestMatches;
    }

    public List<Vertex> getVertices() {
        return sortById(vertices);
    }

    public List<Edge> getEdges() {
        return sortById(edges);
    }

    private <T extends Element> List<T> sortById(Set<T> ts) {
        return ts.stream()
                .sorted((t1, t2) -> Long.compare(t1.getId(), t2.getId()))
                .collect(Collectors.toList());
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
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Graph graph = (Graph) o;
        return Objects.equals(vertices, graph.vertices) &&
                Objects.equals(edges, graph.edges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertices, edges);
    }

    @Override
    public String toString() {
        return "Graph{vertices=" + vertices.size() + ", edges=" + edges.size() + "}";
    }
}
