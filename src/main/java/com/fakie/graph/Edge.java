package com.fakie.graph;

import org.neo4j.graphdb.Node;

import java.util.HashMap;
import java.util.Map;

public class Edge {
    private final Node source;
    private final Node destination;
    private final Map<String, Object> properties;

    public Edge(Node source, Node destination, Map<String, Object> properties) {
        this.source = source;
        this.destination = destination;
        this.properties = new HashMap<>(properties);
    }

    public Node getSource() {
        return source;
    }

    public Node getDestination() {
        return destination;
    }

    public Map<String, Object> getProperties() {
        return new HashMap<>(properties);
    }
}
