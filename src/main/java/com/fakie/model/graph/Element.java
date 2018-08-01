package com.fakie.model.graph;

import java.util.List;
import java.util.Objects;

public class Element extends AbstractProperties {
    private final long id;
    private final Graph graph;

    Element(long id, Graph graph) {
        this.id = id;
        this.graph = graph;
    }

    @Override
    public void setProperty(String key, Object value) {
        super.setProperty(key, value);
        graph.setProperty(this, key, value);
    }

    @Override
    public void removeProperty(Property property) {
        super.removeProperty(property);
        graph.removeProperty(this, property);
    }

    @Override
    public List<Object> values(String key) {
        return graph.values(key);
    }

    @Override
    public Type type(String key) {
        return graph.type(key);
    }

    public long getId() {
        return id;
    }

    public Graph getGraph() {
        return graph;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        Element element = (Element) o;
        return id == element.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
