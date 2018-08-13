package com.fakie.model.processor;

import com.fakie.model.graph.Edge;
import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;
import com.fakie.utils.FakieUtils;
import com.fakie.utils.Keyword;
import com.fakie.utils.exceptions.FakieException;
import com.fakie.utils.paprika.Key;
import com.fakie.utils.paprika.Label;
import com.fakie.utils.paprika.Relationship;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OverriddenMethods implements Processor {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public Graph process(Graph graph) throws FakieException {
        logger.info("Compute overridden methods in %s", graph);
        Set<Vertex> vertices = graph.findVerticesByLabel(Label.CLASS.toString());
        Set<String> overridden = new HashSet<>();
        for (Vertex vertex : vertices) {
            if (FakieUtils.containsACodeSmell(vertex)) {
                Set<String> methods = retrieveNames(availableMethods(vertex));
                overridden.addAll(methods);
            }
        }
        for (Vertex vertex : vertices) {
            if (FakieUtils.containsACodeSmell(vertex)) {
                Set<String> methods = retrieveNames(methods(vertex));
                for (String method : overridden) {
                    String key = Keyword.OUTPUT_EDGE.format(Relationship.CLASS_OWNS_METHOD, method);
                    vertex.setProperty(key, methods.contains(method));
                }
            }
        }
        return graph;
    }

    private Set<Vertex> availableMethods(Vertex vertex) {
        Set<Vertex> result = new HashSet<>(methods(vertex));
        for (Edge edge : vertex.outputEdges(Relationship.EXTENDS.toString(), Relationship.IMPLEMENTS.toString())) {
            result.addAll(availableMethods(edge.getDestination()));
        }
        return result;
    }

    private List<Vertex> methods(Vertex vertex) {
        return vertex.outputEdges(Relationship.CLASS_OWNS_METHOD.toString())
                .stream().map(Edge::getDestination).collect(Collectors.toList());
    }

    private Set<String> retrieveNames(Collection<Vertex> vertices) {
        return vertices.stream()
                .map(vertex -> vertex.getProperty(Key.NAME.toString()).toString())
                .collect(Collectors.toSet());
    }
}
