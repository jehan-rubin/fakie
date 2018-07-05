package com.fakie.model.graph;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Element {
    private final long id;
    private final Map<String, Object> properties;

    Element(long id, Map<String, ?> properties) {
        this.id = id;
        this.properties = new HashMap<>(properties);
    }

    public long getId() {
        return id;
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
        return id == element.id && Objects.equals(properties, element.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, properties);
    }
}
