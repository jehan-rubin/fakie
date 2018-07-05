package com.fakie.model.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Vertex extends Element {
    private final List<String> labels;

    public Vertex(long id, List<String> labels, Map<String, ?> properties) {
        super(id, properties);
        this.labels = new ArrayList<>(labels);
    }

    public List<String> getLabels() {
        return new ArrayList<>(labels);
    }

    @Override
    public String toString() {
        return shortRepresentation() + labels + getProperties();
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

    public String shortRepresentation() {
        return "Node(" + getId() + ')';
    }
}
