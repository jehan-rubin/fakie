package com.fakie.model.graph;

import java.util.HashMap;
import java.util.Map;

public class Element {
    private final Map<String, Object> properties;

    Element(Map<String, Object> properties) {
        this.properties = new HashMap<>(properties);
    }

    public Map<String, Object> getProperties() {
        return new HashMap<>(properties);
    }
}
