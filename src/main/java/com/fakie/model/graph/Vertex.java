package com.fakie.model.graph;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Vertex extends Element {
    private final List<String> labels;
    private final Map<String, Set<Edge>> inputs;
    private final Map<String, Set<Edge>> outputs;

    Vertex(long id, Graph graph, List<String> labels) {
        super(id, graph);
        this.labels = new ArrayList<>(labels);
        this.inputs = new HashMap<>();
        this.outputs = new HashMap<>();
    }

    public boolean hasEdges() {
        return !inputs.isEmpty() || !outputs.isEmpty();
    }

    public void addInputEdge(Edge edge) {
        inputs.putIfAbsent(edge.getType(), new HashSet<>());
        inputs.get(edge.getType()).add(edge);
    }

    public void removeInputEdge(Edge edge) {
        inputs.get(edge.getType()).remove(edge);
    }

    public void addInputEdges(Collection<Edge> edges) {
        for (Edge edge : edges) {
            addInputEdge(edge);
        }
    }

    public void addOutputEdge(Edge edge) {
        outputs.putIfAbsent(edge.getType(), new HashSet<>());
        outputs.get(edge.getType()).add(edge);
    }

    public void removeOutputEdge(Edge edge) {
        outputs.get(edge.getType()).remove(edge);
    }

    public void addOutputEdges(Collection<Edge> edges) {
        for (Edge edge : edges) {
            addOutputEdge(edge);
        }
    }

    public List<String> getLabels() {
        return new ArrayList<>(labels);
    }

    public Collection<Edge> inputEdges() {
        return inputs.values().stream().flatMap(Set::stream).collect(Collectors.toList());
    }

    public Collection<Edge> inputEdges(String... types) {
        Set<Edge> result = new HashSet<>();
        for (String type : types) {
            result.addAll(inputs.getOrDefault(type, new HashSet<>()));
        }
        return result;
    }

    public Collection<Edge> outputEdges() {
        return outputs.values().stream().flatMap(Set::stream).collect(Collectors.toList());
    }

    public Collection<Edge> outputEdges(String... types) {
        Set<Edge> result = new HashSet<>();
        for (String type : types) {
            result.addAll(outputs.getOrDefault(type, new HashSet<>()));
        }
        return result;
    }

    public Collection<Edge> edges() {
        return Stream.of(inputEdges(), outputEdges()).flatMap(Collection::stream).collect(Collectors.toList());
    }

    @Override
    public void detach() {
        getGraph().removeVertex(this);
        super.detach();
    }

    @Override
    public String toString() {
        return shortRepresentation() + labels + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        Vertex vertex = (Vertex) o;
        return Objects.equals(labels, vertex.labels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), labels);
    }

    String shortRepresentation() {
        return "Node(" + getId() + ')';
    }
}
