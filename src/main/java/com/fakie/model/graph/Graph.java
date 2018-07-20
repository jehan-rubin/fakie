package com.fakie.model.graph;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Graph extends FastProperties {
    private final List<Vertex> vertices;
    private final List<Edge> edges;
    private final Map<Property, Set<Element>> index;
    private int vertexId = 0;
    private long edgeId = 0;

    public Graph() {
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
        index = new HashMap<>();
    }

    public Graph(Graph graph) {
        this();
        union(graph);
    }

    public Vertex createVertex() {
        return createVertex(new ArrayList<>());
    }

    public Vertex createVertex(List<String> labels) {
        Vertex vertex = new Vertex(vertexId++, this, labels);
        vertices.add(vertex);
        return vertex;
    }

    public Vertex createVertex(Vertex vertex) {
        Vertex v = createVertex(vertex.getLabels());
        v.setProperties(vertex);
        return v;
    }

    public Edge createEdge(Vertex source, Vertex destination, String type) {
        Edge edge = new Edge(edgeId++, this, source, destination, type);
        edges.add(edge);
        source.addOutputEdge(edge);
        destination.addInputEdge(edge);
        return edge;
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

    public Set<Element> find(String key, Object value) {
        Property property = new Property(this, key, value);
        if (index.containsKey(property)) {
            return new HashSet<>(index.get(property));
        }
        return new HashSet<>();
    }

    public Set<Vertex> findVertices(String key, Object value) {
        Set<Vertex> result = new HashSet<>(this.vertices);
        result.retainAll(find(key, value));
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

    void setProperty(Element element, String key, Object value) {
        addElement(element, key, value);
        addValue(key, value);
        addType(key, value);
    }

    private void addElement(Element element, String key, Object value) {
        Property property = new Property(this, key, value);
        index.putIfAbsent(property, new HashSet<>());
        index.get(property).add(element);
    }
}
