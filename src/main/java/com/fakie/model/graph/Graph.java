package com.fakie.model.graph;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Graph extends FastProperties {
    private final List<Vertex> vertices;
    private final List<Edge> edges;
    private final Map<Property, Set<Element>> index;
    private final Map<String, Set<Vertex>> labels;
    private int vertexId = 0;
    private long edgeId = 0;

    public Graph() {
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
        index = new HashMap<>();
        labels = new HashMap<>();
    }

    public Graph(Graph graph) {
        this();
        union(graph);
    }

    public boolean contains(Element element) {
        return getElements().contains(element);
    }

    public Vertex createVertex() {
        return createVertex(new ArrayList<>());
    }

    public Vertex createVertex(List<String> labels) {
        Vertex vertex = new Vertex(vertexId++, this, labels);
        vertices.add(vertex);
        for (String label : labels) {
            this.labels.putIfAbsent(label, new HashSet<>());
            this.labels.get(label).add(vertex);
        }
        return vertex;
    }

    public Vertex createVertex(Vertex vertex) {
        Vertex v = createVertex(vertex.getLabels());
        v.setProperties(vertex);
        return v;
    }

    public void remove(Element element) {
        if (!contains(element)) {
            return;
        }
        for (Property property : element) {
            removeProperty(element, property);
        }
        element.detach();
    }

    void removeVertex(Vertex vertex) {
        if (!vertices.contains(vertex)) {
            return;
        }
        for (String label : vertex.getLabels()) {
            labels.get(label).remove(vertex);
            if (labels.get(label).isEmpty()) {
                labels.remove(label);
            }
        }
        for (Edge edge : vertex.edges()) {
            removeEdge(edge);
        }
        vertices.remove(vertex);
    }

    public Edge createEdge(Vertex source, Vertex destination, String type) {
        Edge edge = new Edge(edgeId++, this, source, destination, type);
        edges.add(edge);
        source.addOutputEdge(edge);
        destination.addInputEdge(edge);
        return edge;
    }

    void removeEdge(Edge edge) {
        if (!edges.contains(edge)) {
            return;
        }
        edges.remove(edge);
        edge.getSource().removeOutputEdge(edge);
        edge.getDestination().removeInputEdge(edge);
    }

    public List<Element> getElements() {
        return Stream.of(vertices, edges).flatMap(List::stream).collect(Collectors.toList());
    }

    public boolean isEmpty() {
        return getElements().isEmpty();
    }

    public List<Vertex> getVertices() {
        return new ArrayList<>(vertices);
    }

    public List<Edge> getEdges() {
        return new ArrayList<>(edges);
    }

    public Set<String> labels() {
        return new HashSet<>(labels.keySet());
    }

    public Set<Element> find(String key, Object value) {
        Property property = new Property(this, key, value);
        if (index.containsKey(property)) {
            return new HashSet<>(index.get(property));
        }
        return new HashSet<>();
    }

    public Set<Vertex> matchAllVertices(Properties properties) {
        Set<Vertex> result = new HashSet<>(getVertices());
        for (Property property : properties) {
            Set<Vertex> match = findVertices(property.getKey(), property.getValue());
            if (!match.isEmpty()) {
                result.retainAll(match);
            }
        }
        return result;
    }

    public Set<Vertex> findVertices(String key, Object value) {
        Set<Vertex> result = new HashSet<>(this.vertices);
        result.retainAll(find(key, value));
        return result;
    }

    public Set<Vertex> findVerticesByLabel(String label, String... labels) {
        Set<Vertex> result = new HashSet<>(this.labels.getOrDefault(label, new HashSet<>()));
        for (String l : labels) {
            Set<Vertex> temp = this.labels.getOrDefault(l, new HashSet<>());
            result.retainAll(temp);
        }
        return result;
    }

    public Set<Edge> findEdges(String key, Object value) {
        Set<Edge> result = new HashSet<>(this.edges);
        result.retainAll(find(key, value));
        return result;
    }

    public void union(Graph graph) {
        Map<Vertex, Vertex> mapping = new HashMap<>();
        for (Vertex vertex : graph.getVertices()) {
            mapping.put(vertex, createVertex(vertex));
        }
        for (Edge edge : graph.getEdges()) {
            Edge e = createEdge(mapping.get(edge.getSource()), mapping.get(edge.getDestination()), edge.getType());
            e.setProperties(edge);
        }
    }

    public int numberOfVertices() {
        return vertices.size();
    }

    @Override
    public int size() {
        return getElements().stream().mapToInt(Element::size).sum();
    }

    public int numberOfEdges() {
        return edges.size();
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
        return "Graph{vertices=" + numberOfVertices() + ", edges=" + numberOfEdges()
                + ", size=" + size() + "}";
    }

    void setProperty(Element element, String key, Object value) {
        addElement(element, key, value);
        addValue(key, value);
        addType(key, value);
    }

    void removeProperty(Element element, Property property) {
        index.get(property).remove(element);
        if (index.get(property).isEmpty()) {
            index.remove(property);
        }
        removeProperty(property);
    }

    private void addElement(Element element, String key, Object value) {
        Property property = new Property(this, key, value);
        index.putIfAbsent(property, new HashSet<>());
        index.get(property).add(element);
    }
}
