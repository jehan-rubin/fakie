package com.fakie.learning.association;

import com.fakie.io.input.dataset.ARFFReader;
import com.fakie.io.output.graphdumper.GraphToARFF;
import com.fakie.learning.Orchestrator;
import com.fakie.learning.Rule;
import com.fakie.learning.filter.*;
import com.fakie.model.graph.Graph;
import com.fakie.model.processor.Processor;
import com.fakie.utils.exceptions.FakieException;
import weka.associations.FPGrowth;
import weka.core.Instances;

import java.nio.file.Path;
import java.util.List;

public class PreProcessing extends Orchestrator<Instances> {
    private Graph graph;
    private final int n;
    private final double support;
    private final double confidence;

    public PreProcessing(Graph graph, int n, double support, double confidence) {
        this.graph = graph;
        this.n = n;
        this.support = support;
        this.confidence = confidence;
        setup();
    }

    private void setup() {
        useGraph(graph);
        useGraphDumper(new GraphToARFF());
        useDatasetReader(new ARFFReader());
        useFilters(
                new FilterNonCodeSmellRule(),
                new RemoveNonCodeSmellConsequences(),
                new ManyToOne(),
                new SimplifyExpression(),
                new FilterRedundantRule()
        );
    }

    @Override
    public void useProcessors(Processor... processors) {
        super.useProcessors(processors);
    }

    @Override
    public List<Rule> generateRules() throws FakieException {
        applyProcessors();
        Path datasetPath = dumpGraph();
        Instances dataset = readDataset(datasetPath);
        FPGrowth fpGrowth = new FPGrowth();
        fpGrowth.setNumRulesToFind(n);
        fpGrowth.setMinMetric(support);
        fpGrowth.setLowerBoundMinSupport(confidence);
        Association association = new Association(dataset, fpGrowth, fpGrowth);
        List<Rule> rules = association.generateRules();
        logGeneratedRules(rules);
        List<Rule> filtered = applyFilters(rules);
        logFilteredRules(filtered);
        return filtered;
    }
}
