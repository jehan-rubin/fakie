package com.fakie.model.processor;

import com.fakie.model.FakieModelException;
import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;
import com.fakie.utils.FakieUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class KeepOnlyVertexWithCodesmellLabel implements Processor {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public Graph process(Graph graph) throws FakieModelException {
        logger.info("Remove vertices without a codesmell label in %s", graph);
        Set<String> labels = graph.labels();
        labels.removeAll(findLabelOfCodesmell(graph));
        for (String label : labels) {
            for (Vertex vertex : graph.findVerticesByLabel(label)) {
               graph.remove(vertex);
            }
        }
        return graph;
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
}
