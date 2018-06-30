package com.fakie.model.graph;

import java.util.Map;

public class Edge extends Element {
    private final Vertex source;
    private final Vertex destination;
    private final String type;

    public Edge(Vertex source, Vertex destination, String type, Map<String, ?> properties) {
        super(properties);
        this.source = source;
        this.destination = destination;
        this.type = type;
    }

    public Vertex getSource() {
        return source;
    }

    public Vertex getDestination() {
        return destination;
    }

    @Override
    public String toString() {
        return source.shortRepresentation() + " --" + type + "-> " + destination.shortRepresentation() +
                ", properties=" + getProperties();
    }
}
