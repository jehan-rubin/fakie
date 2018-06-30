package com.fakie.model.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public String shortRepresentation() {
        return "Node(" + id + ')';
    }
}
