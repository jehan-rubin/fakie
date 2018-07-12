package com.fakie.model.graph;

import java.util.List;

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
}
