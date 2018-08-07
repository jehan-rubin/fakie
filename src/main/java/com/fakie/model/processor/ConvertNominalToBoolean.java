package com.fakie.model.processor;

import com.fakie.model.graph.Element;
import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Property;
import com.fakie.model.graph.Type;
import com.fakie.utils.Keyword;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConvertNominalToBoolean implements Processor {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public Graph process(Graph graph) {
        logger.info("Converting %s properties to boolean", graph);
        for (Element element : graph.getElements()) {
            for (Property property : element) {
                if (property.getType() == Type.STRING) {
                    for (Object o : property.values()) {
                        element.setProperty(Keyword.EQUAL.format(property.getKey(), o), property.getValue().equals(o));
                    }
                    element.removeProperty(property);
                }
            }
        }
        return graph;
    }
}
