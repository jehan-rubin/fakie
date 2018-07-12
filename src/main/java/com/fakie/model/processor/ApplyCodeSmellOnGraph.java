package com.fakie.model.processor;

import com.fakie.model.graph.Graph;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ApplyCodeSmellOnGraph implements Processor {
    private static final Logger logger = LogManager.getFormatterLogger();
    private final List<CodeSmell> codeSmells;

    public ApplyCodeSmellOnGraph(List<CodeSmell> codeSmells) {
        this.codeSmells = codeSmells;
    }

    @Override
    public Graph process(Graph graph) throws ProcessingException {
        logger.info("Adding code smells to the graph");
        for (CodeSmell codeSmell : codeSmells) {
            graph = codeSmell.process(graph);
        }
        return graph;
    }
}
