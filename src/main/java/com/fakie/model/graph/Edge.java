package com.fakie.model.graph;

import java.util.Objects;

public class Edge extends Element {
    private final Vertex source;
    private final Vertex destination;
    private final String type;

    Edge(long id, Graph graph, Vertex source, Vertex destination, String type) {
        super(id, graph);
        this.source = source;
        this.destination = destination;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public Vertex getSource() {
        return source;
    }

    public Vertex getDestination() {
        return destination;
    }

    @Override
    public void detach() {
        getGraph().removeEdge(this);
        super.detach();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        Edge edge = (Edge) o;
        return Objects.equals(source, edge.source) &&
                Objects.equals(destination, edge.destination) &&
                Objects.equals(type, edge.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), source, destination, type);
    }

    @Override
    public String toString() {
        return source.shortRepresentation() + " --" + type + "[" + getId() + "]-> " +
                destination.shortRepresentation() + ", properties=" + super.toString();
    }

    public String longRepresentation() {
        return source.toString() + " --" + type + "[" + getId() + "]-> " +
                destination.toString() + ", properties=" + super.toString();
    }
}
