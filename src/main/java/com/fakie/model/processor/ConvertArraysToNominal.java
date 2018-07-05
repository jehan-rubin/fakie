package com.fakie.model.processor;

import com.fakie.model.graph.Graph;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConvertArraysToNominal implements Processor {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public Graph process(Graph graph) throws ProcessingException {
        logger.info("Converting %s arrays to nominal", graph);
        return graph;
    }
}
