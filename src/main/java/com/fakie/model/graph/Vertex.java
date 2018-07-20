package com.fakie.model.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Vertex extends Element {
    private final List<String> labels;
    private final List<Edge> inputs;
    private final List<Edge> outputs;

    Vertex(long id, Graph graph, List<String> labels) {
        super(id, graph);
        this.labels = new ArrayList<>(labels);
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
    }

    public void addInputEdge(Edge edge) {
        inputs.add(edge);
    }

    public void addInputEdges(Collection<Edge> edges) {
        inputs.addAll(edges);
    }

    public void addOutputEdge(Edge edge) {
        outputs.add(edge);
    }

    public void addOutputEdges(Collection<Edge> edges) {
        outputs.addAll(edges);
    }

    public List<String> getLabels() {
        return new ArrayList<>(labels);
    }

    public List<Edge> inputEdges() {
        return new ArrayList<>(inputs);
    }

    public List<Edge> outputEdges() {
        return new ArrayList<>(outputs);
    }

    @Override
    public String toString() {
        return shortRepresentation() + labels + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        Vertex vertex = (Vertex) o;
        return Objects.equals(labels, vertex.labels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), labels);
    }

    String shortRepresentation() {
        return "Node(" + getId() + ')';
    }
}
