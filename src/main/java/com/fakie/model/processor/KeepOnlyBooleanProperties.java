package com.fakie.model.processor;

import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Property;
import com.fakie.model.graph.Type;
import com.fakie.model.graph.Vertex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class KeepOnlyBooleanProperties implements Processor {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public Graph process(Graph graph) throws ProcessingException {
        logger.info("Keep only boolean properties");
        for (Vertex vertex : graph.getVertices()) {
            for (Property property : vertex) {
                if (property.getType() != Type.BOOLEAN) {
                    vertex.removeProperty(property);
                }
            }
        }
        return graph;
    }
}
