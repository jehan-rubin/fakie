package com.fakie.model.processor;

import com.fakie.model.graph.Graph;

import java.util.List;

public class ApplyCodeSmellOnGraph implements Processor {
    private final List<CodeSmell> codeSmells;

    public ApplyCodeSmellOnGraph(List<CodeSmell> codeSmells) {
        this.codeSmells = codeSmells;
    }

    @Override
    public Graph process(Graph graph) throws ProcessingException {
        Graph processed = graph;
        for (CodeSmell codeSmell : codeSmells) {
            processed = codeSmell.process(processed);
        }
        return processed;
    }
}
