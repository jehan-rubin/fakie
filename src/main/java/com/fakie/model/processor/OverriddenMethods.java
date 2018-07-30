package com.fakie.model.processor;

import com.fakie.model.graph.Edge;
import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;
import com.fakie.utils.FakieUtils;
import com.fakie.utils.Keyword;
import com.fakie.utils.exceptions.FakieException;
import com.fakie.utils.paprika.Label;

import java.util.*;
import java.util.stream.Collectors;

public class OverriddenMethods implements Processor {
    @Override
    public Graph process(Graph graph) throws FakieException {
        Set<Vertex> vertices = graph.findVerticesByLabel(Label.CLASS.toString());
        Set<String> overridden = retrieveNames(graph.findVerticesByLabel(Label.METHOD.toString()));
        for (Vertex vertex : vertices) {
            if (FakieUtils.containsACodeSmell(vertex)) {
                overridden.retainAll(retrieveNames(availableMethods(vertex)));
            }
        }
        for (Vertex vertex : vertices) {
            if (FakieUtils.containsACodeSmell(vertex)) {
                Set<String> methods = retrieveNames(methods(vertex));
                for (String method : overridden) {
                    vertex.setProperty(
                            Keyword.OUTPUT_EDGE.format(Label.CLASS_OWNS_METHOD, method),
                            methods.contains(method));
                }
            }
        }
        return graph;
    }

    private Collection<Vertex> availableMethods(Vertex vertex) {
        Set<Vertex> result = new HashSet<>(methods(vertex));
        for (Edge edge : vertex.outputEdges()) {
            if (edge.getType().equals(Label.EXTENDS.toString()) || edge.getType().equals(Label.IMPLEMENTS.toString())) {
                result.addAll(availableMethods(edge.getDestination()));
            }
        }
        return result;
    }

    private List<Vertex> methods(Vertex vertex) {
        List<Vertex> result = new ArrayList<>();
        for (Edge edge : vertex.outputEdges()) {
            if (edge.getType().equals(Label.CLASS_OWNS_METHOD.toString())) {
                result.add(edge.getDestination());
            }
        }
        return result;
    }

    private Set<String> retrieveNames(Collection<Vertex> vertices) {
        return vertices.stream()
                .map(vertex -> vertex.getProperty(Label.NAME.toString()).toString())
                .collect(Collectors.toSet());
    }
}
