package com.fakie.model.graph;

import java.util.*;

public abstract class AbstractProperties implements Properties {
    private final Map<String, Object> data;

    AbstractProperties() {
        data = new HashMap<>();
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean hasProperty(String key) {
        return data.containsKey(key);
    }

    @Override
    public Object getProperty(String key) {
        return data.get(key);
    }

    @Override
    public void setProperty(String key, Object value) {
        data.put(key, value);
    }

    @Override
    public void removeProperty(Property property) {
        data.remove(property.getKey(), property.getValue());
    }

    @Override
    public Set<String> keys() {
        return data.keySet();
    }

    @Override
    public Collection<Object> values() {
        return data.values();
    }

    @Override
    public Iterator<Property> iterator() {
        return new HashSet<>(data.entrySet()).stream()
                .map(e -> new Property(this, e.getKey(), e.getValue()))
                .iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AbstractProperties that = (AbstractProperties) o;
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
