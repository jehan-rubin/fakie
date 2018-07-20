package com.fakie.model.graph;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FastProperties extends AbstractProperties {
    private final Map<String, List<Object>> values;
    private final Map<String, Type> types;

    public FastProperties() {
        values = new HashMap<>();
        types = new HashMap<>();
    }

    @Override
    public Set<String> keys() {
        return values.keySet();
    }

    @Override
    public Collection<Object> values() {
        return Stream.of(values.values())
                .flatMap(Collection::stream)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<Object> values(String key) {
        return values.getOrDefault(key, new ArrayList<>());
    }

    @Override
    public Type type(String key) {
        return types.getOrDefault(key, Type.NONE);
    }

    protected void addValue(String key, Object value) {
        values.putIfAbsent(key, new ArrayList<>());
        values.get(key).add(value);
    }

    protected void addType(String key, Object value) {
        Type type = Type.valueOf(value);
        if (types.containsKey(key)) {
            if (types.get(key) != type) {
                types.put(key, Type.NONE);
            }
        } else {
            types.put(key, type);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        FastProperties that = (FastProperties) o;
        return Objects.equals(values, that.values) &&
                Objects.equals(types, that.types);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), values, types);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
