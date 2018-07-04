package com.fakie.model.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Vertex extends Element {
    private final long id;
    private final List<String> labels;

    public Vertex(long id, List<String> labels, Map<String, ?> properties) {
        super(properties);
        this.id = id;
        this.labels = new ArrayList<>(labels);
    }

    public long getId() {
        return id;
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
        return id == vertex.id && Objects.equals(labels, vertex.labels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, labels);
    }

    public String shortRepresentation() {
        return "Node(" + id + ')';
    }
}
