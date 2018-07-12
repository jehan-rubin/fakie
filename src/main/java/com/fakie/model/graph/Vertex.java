package com.fakie.model.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Vertex extends Element {
    private final List<String> labels;

    Vertex(long id, Graph graph, List<String> labels) {
        super(id, graph);
        this.labels = new ArrayList<>(labels);
    }

    public List<String> getLabels() {
        return new ArrayList<>(labels);
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
