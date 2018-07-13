package com.fakie.model.processor;

import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Property;
import com.fakie.model.graph.Type;
import com.fakie.model.graph.Vertex;
import com.fakie.utils.Keyword;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;

public class ConvertArraysToNominal implements Processor {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public Graph process(Graph graph) throws ProcessingException {
        logger.info("Converting %s arrays to nominal", graph);
        for (Vertex vertex : graph.getVertices()) {
            for (Property property : vertex) {
                if (property.getType() == Type.ARRAY) {
                    convertArrayToNominal(vertex, property);
                }
            }
        }
        return graph;
    }

    private void convertArrayToNominal(Vertex vertex, Property property) {
        ArrayList<Object> objects = new ArrayList<>(property.<Collection<Object>>getValue());
        for (int i = 0; i < objects.size(); i++) {
            vertex.setProperty(Keyword.SPLIT.format(property.getKey(), i), objects.get(i));
        }
    }
}
