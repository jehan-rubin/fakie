package com.fakie.learning.association;

import com.fakie.io.input.codesmell.CodeSmells;
import com.fakie.learning.Algorithm;
import com.fakie.learning.Orchestrator;
import com.fakie.learning.Rule;
import com.fakie.model.graph.Graph;
import com.fakie.utils.exceptions.FakieException;
import weka.associations.Apriori;

import java.util.List;

public class AprioriAlgorithm implements Algorithm {
    private final Orchestrator orchestrator;

    public AprioriAlgorithm(Graph graph, CodeSmells codeSmells, int n, double support) {
        Apriori apriori = new Apriori();
        apriori.setNumRules(n);
        apriori.setMinMetric(support);
        apriori.setLowerBoundMinSupport(support);
        orchestrator = new AssociationOrchestrator<>(graph, codeSmells, apriori);
    }

    @Override
    public List<Rule> generateRules() throws FakieException {
        return orchestrator.generateRules();
    }
}
