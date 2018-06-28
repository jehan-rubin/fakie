package com.fakie.graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final List<Vertex> vertices = new ArrayList<>();
    private final List<Edge> edges = new ArrayList<>();

    public void addVertex(Vertex vertex) {
        vertices.add(vertex);
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public List<Vertex> getVertices() {
        return new ArrayList<>(vertices);
    }

    public List<Edge> getEdges() {
        return new ArrayList<>(edges);
    }
}
