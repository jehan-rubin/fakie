package com.fakie.model.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Vertex {
    private final long id;
    private final List<String> labels;
    private final Map<String, Object> properties;

    public Vertex(long id, List<String> labels, Map<String, Object> properties) {
        this.id = id;
        this.labels = new ArrayList<>(labels);
        this.properties = new HashMap<>(properties);
    }

    public List<String> getLabels() {
        return new ArrayList<>(labels);
    }

    public Map<String, Object> getProperties() {
        return new HashMap<>(properties);
    }

    @Override
    public String toString() {
        return shortRepresentation() + labels + properties;
    }

    public String shortRepresentation() {
        return "Node(" + id + ')';
    }
}
