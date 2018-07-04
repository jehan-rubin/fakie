package com.fakie.model.graph;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Element {
    private final Map<String, Object> properties;

    Element(Map<String, ?> properties) {
        this.properties = new HashMap<>(properties);
    }

    public Map<String, Object> getProperties() {
        return new HashMap<>(properties);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Element element = (Element) o;
        return Objects.equals(properties, element.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(properties);
    }
}
