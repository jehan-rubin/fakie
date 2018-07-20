package com.fakie.model.processor;

import com.fakie.model.FakieModelException;
import com.fakie.model.graph.Edge;
import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;
import com.fakie.utils.FakieUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class KeepOnlyVertexWithCodesmellLabel implements Processor {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public Graph process(Graph graph) throws FakieModelException {
        logger.info("Remove vertices without a codesmell label in %s", graph);
        Graph result = new Graph();
        Map<Vertex, Vertex> mapping = new HashMap<>();
        Set<String> labels = findLabelOfCodesmell(graph);
        addVertices(graph, result, mapping, labels);
        addEdges(graph, result, mapping);
        return result;
    }

    private Set<String> findLabelOfCodesmell(Graph graph) {
        Set<String> labels = new HashSet<>();
        for (Vertex vertex : graph.getVertices()) {
            if (FakieUtils.containsACodeSmell(vertex)) {
                labels.addAll(vertex.getLabels());
            }
        }
        return labels;
    }

    private void addVertices(Graph graph, Graph result, Map<Vertex, Vertex> mapping, Set<String> labels) {
        for (Vertex vertex : graph.getVertices()) {
            for (String label : vertex.getLabels()) {
                if (labels.contains(label)) {
                    mapping.put(vertex, result.createVertex(vertex));
                    break;
                }
            }
        }
    }

    private void addEdges(Graph graph, Graph result, Map<Vertex, Vertex> mapping) {
        for (Edge edge : graph.getEdges()) {
            if (!mapping.containsKey(edge.getSource()) && !mapping.containsKey(edge.getDestination())) {
                continue;
            } else if (!mapping.containsKey(edge.getDestination())) {
                mapping.put(edge.getDestination(), result.createVertex(edge.getDestination()));
            } else if (!mapping.containsKey(edge.getSource())) {
                mapping.put(edge.getSource(), result.createVertex(edge.getSource()));
            }
            Vertex source = mapping.get(edge.getSource());
            Vertex destination = mapping.get(edge.getDestination());
            Edge e = result.createEdge(source, destination, edge.getType());
            e.setProperties(edge);
        }
    }
}
