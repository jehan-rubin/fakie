package com.fakie.learning.association;

import com.fakie.io.input.dataset.ARFFReader;
import com.fakie.io.output.graphdumper.GraphToARFF;
import com.fakie.learning.Orchestrator;
import com.fakie.learning.Rule;
import com.fakie.learning.filter.FilterNonCodeSmellRule;
import com.fakie.learning.filter.FilterRedundantRule;
import com.fakie.learning.filter.ManyToOne;
import com.fakie.learning.filter.RemoveNonCodeSmellConsequences;
import com.fakie.model.graph.Graph;
import com.fakie.model.processor.*;
import com.fakie.utils.exceptions.FakieException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import weka.associations.AssociationRulesProducer;
import weka.associations.Associator;
import weka.core.Instances;

import java.nio.file.Path;
import java.util.List;

public class AssociationOrchestrator<T extends Associator & AssociationRulesProducer> extends Orchestrator<T> {
    private static final Logger logger = LogManager.getFormatterLogger();

    AssociationOrchestrator(Graph graph, List<CodeSmell> codeSmells, T associator) {
        setup(graph, codeSmells, associator);
    }

    private void setup(Graph graph, List<CodeSmell> codeSmells, T associator) {
        useGraph(graph);
        useAssociationAlgorithm(associator);
        useProcessors(
                new RemoveEdges(),
                new ApplyCodeSmellOnGraph(codeSmells),
                new ConvertLabelsToProperties(),
                new ConvertArraysToNominal(),
                new ConvertNumericToBoolean(),
                new ProcessOnlyVerticesWithACodeSmell(),
                new ConvertNominalToBoolean(),
                new KeepOnlyBooleanProperties()
        );
        useGraphDumper(new GraphToARFF());
        useDatasetReader(new ARFFReader());
        useFilters(
                new FilterNonCodeSmellRule(),
                new RemoveNonCodeSmellConsequences(),
                new ManyToOne(),
                new FilterRedundantRule()
        );
    }

    @Override
    public List<Rule> generateRules() throws FakieException {
        applyProcessors();
        Path datasetPath = graphDumper.dump(graph);
        Instances dataset = datasetReader.readDataset(datasetPath);
        Association association = new Association(dataset, associator, associator);
        rules = association.generateRules();
        logGeneratedRules(rules);
        applyFilters();
        logFilteredRules(rules);
        return rules;
    }
}
