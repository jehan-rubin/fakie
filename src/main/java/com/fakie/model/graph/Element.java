package com.fakie.model.graph;

import java.util.HashMap;
import java.util.Map;

public class Element {
    private final Map<String, Object> properties;

    Element(Map<String, ?> properties) {
        this.properties = new HashMap<>(properties);
    }

    public Map<String, Object> getProperties() {
        return new HashMap<>(properties);
    }

    public void addProperty(String key, Object value) {
        properties.putIfAbsent(key, value);
    }
}
