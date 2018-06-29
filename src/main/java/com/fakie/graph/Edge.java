package com.fakie.graph;

import java.util.HashMap;
import java.util.Map;

public class Edge {
    private final Vertex source;
    private final Vertex destination;
    private final String type;
    private final Map<String, Object> properties;

    public Edge(Vertex source, Vertex destination, String type, Map<String, Object> properties) {
        this.source = source;
        this.destination = destination;
        this.type = type;
        this.properties = new HashMap<>(properties);
    }

    public Vertex getSource() {
        return source;
    }

    public Vertex getDestination() {
        return destination;
    }

    public Map<String, Object> getProperties() {
        return new HashMap<>(properties);
    }

    @Override
    public String toString() {
        return source.shortRepresentation() + " --" + type + "-> " + destination.shortRepresentation() +
                ", properties=" + properties;
    }
}
