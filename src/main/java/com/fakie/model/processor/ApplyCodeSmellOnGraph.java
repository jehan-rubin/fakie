package com.fakie.model.processor;

import com.fakie.io.input.codesmell.CodeSmells;
import com.fakie.model.graph.Graph;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ApplyCodeSmellOnGraph implements Processor {
    private static final Logger logger = LogManager.getFormatterLogger();
    private final CodeSmells codeSmells;

    public ApplyCodeSmellOnGraph(CodeSmells codeSmells) {
        this.codeSmells = codeSmells;
    }

    @Override
    public Graph process(Graph graph) throws ProcessingException {
        logger.info("Adding code smells to %s", graph);
        for (CodeSmell codeSmell : codeSmells) {
            graph = codeSmell.process(graph);
        }
        return graph;
    }
}
