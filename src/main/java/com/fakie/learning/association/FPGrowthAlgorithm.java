package com.fakie.learning.association;

import com.fakie.io.input.codesmell.CodeSmells;
import com.fakie.learning.Algorithm;
import com.fakie.learning.Orchestrator;
import com.fakie.learning.Rule;
import com.fakie.model.graph.Graph;
import com.fakie.utils.exceptions.FakieException;
import weka.associations.FPGrowth;

import java.util.List;

public class FPGrowthAlgorithm implements Algorithm {
    private final Orchestrator orchestrator;

    public FPGrowthAlgorithm(Graph graph, CodeSmells codeSmells, int n, double support) {
        FPGrowth fpGrowth = new FPGrowth();
        fpGrowth.setNumRulesToFind(n);
        fpGrowth.setMinMetric(support);
        fpGrowth.setLowerBoundMinSupport(support);
        fpGrowth.setPositiveIndex(0);
        orchestrator = new AssociationOrchestrator<>(graph, codeSmells, fpGrowth);
    }

    @Override
    public List<Rule> generateRules() throws FakieException {
        return orchestrator.generateRules();
    }
}
