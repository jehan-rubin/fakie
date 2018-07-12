package com.fakie.model.graph;

import java.util.List;
import java.util.Objects;

public class Property {
    private final Properties properties;
    private final String key;
    private final Object value;

    Property(Properties properties, String key, Object value) {
        this.properties = properties;
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public <T> T getValue() {
        return (T) value;
    }

    public Type getType() {
        return properties.type(key);
    }

    public List<Object> values() {
        return properties.values(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Property property = (Property) o;
        return Objects.equals(key, property.key) &&
                Objects.equals(value, property.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }
}
