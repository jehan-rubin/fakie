package com.fakie.model.processor;

import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;
import com.fakie.utils.Keyword;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConvertLabelsToProperties implements Processor {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public Graph process(Graph graph) throws ProcessingException {
        logger.info("Converting %s labels to properties", graph);
        for (Vertex vertex : graph.getVertices()) {
            vertex.setProperty(Keyword.LABEL.toString(), vertex.getLabels());
        }
        return graph;
    }
}
