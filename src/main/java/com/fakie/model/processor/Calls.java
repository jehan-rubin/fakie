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

import java.util.Set;

public class Calls implements Processor {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public Graph process(Graph graph) throws FakieException {
        logger.info("Compute overridden methods in %s", graph);
        Set<Vertex> vertices = graph.findVerticesByLabel(Label.METHOD.toString());
        for (Vertex vertex : vertices) {
            if (FakieUtils.containsACodeSmell(vertex)) {
                for (Edge edge : vertex.outputEdges(Relationship.CALLS.toString())) {
                    String methodName = edge.getDestination().getProperty(Key.FULL_NAME.toString()).toString();
                    String key = Keyword.OUTPUT_EDGE.format(Relationship.CALLS, methodName);
                    vertex.setProperty(key, true);
                }
            }
        }
        return graph;
    }
}
