package com.fakie.model.processor;

import com.fakie.model.graph.Element;
import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Property;
import com.fakie.model.graph.Type;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class KeepOnlyBooleanProperties implements Processor {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public Graph process(Graph graph) throws ProcessingException {
        logger.info("Keep only boolean properties");
        for (Element element : graph.getElements()) {
            for (Property property : element) {
                if (property.getType() != Type.BOOLEAN) {
                    element.removeProperty(property);
                }
            }
        }
        fillProperties(graph);
        return graph;
    }

    private void fillProperties(Graph graph) {
        for (Element element : graph.getElements()) {
            for (String key : graph.keys()) {
                if (!element.hasProperty(key)) {
                    element.setProperty(key, false);
                }
            }
        }
    }
}
