package com.fakie.graph.model;

import java.util.HashMap;
import java.util.Map;

public class Vertex {
    private final Map<String, Object> properties;

    public Vertex(Map<String, Object> properties) {
        this.properties = new HashMap<>(properties);
    }

    public Map<String, Object> getProperties() {
        return new HashMap<>(properties);
    }
}
