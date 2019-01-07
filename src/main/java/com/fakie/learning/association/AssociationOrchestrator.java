package com.fakie.learning.association;

import com.fakie.io.input.codesmell.CodeSmells;
import com.fakie.io.input.dataset.ARFFReader;
import com.fakie.io.output.graphdumper.GraphToARFF;
import com.fakie.learning.Orchestrator;
import com.fakie.learning.Rule;
import com.fakie.learning.filter.*;
import com.fakie.model.graph.Graph;
import com.fakie.model.processor.*;
import com.fakie.utils.exceptions.FakieException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import weka.associations.AssociationRulesProducer;
import weka.associations.Associator;
import weka.core.Instances;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AssociationOrchestrator<T extends Associator & AssociationRulesProducer> extends Orchestrator<Instances> {
    private static final Logger logger = LogManager.getFormatterLogger();
    private final CodeSmells codeSmells;
    private final Graph parent;
    private final T associator;

    AssociationOrchestrator(Graph graph, CodeSmells codeSmells, T associator) {
        this.parent = graph;
        this.codeSmells = codeSmells;
        this.associator = associator;
        setup();
    }

    private void setup() {
        useGraphDumper(new GraphToARFF());
        useDatasetReader(new ARFFReader());
        useFilters(
                new FilterNonCodeSmellRule(),
                new RemoveNonCodeSmellConsequences(),
                new ManyToOne(),
               // new SimplifyExpression(),
                new FilterRedundantRule(),
                new RestoreRulesAttributes()
        );
    }

    @Override
    public List<Rule> generateRules() throws FakieException {
        List<Rule> result = new ArrayList<>();
        for (String name : this.codeSmells.names()) {
            logger.info("Generating rules for code smell %s", name);
            CodeSmells group = this.codeSmells.groupByName(name);
            try {
                result.addAll(generateRules(group, parent));
            }
            catch (FakieException e) {
                logger.warn(e);
            }
        }
        return result;
    }

    private List<Rule> generateRules(CodeSmells codeSmells, Graph graph) throws FakieException {
        useGraph(graph);
        useProcessors(
                new ConvertLabelsToProperties(),
                new ConvertArraysToNominal(),
                new DeleteName(),
                new ApplyCodeSmellOnGraph(codeSmells),
                new NatureOfParentClass(),
               // new OwnCloseableObject(),
               // new UseTreeParser(),
                new MethodWhiteList(),
                new OverriddenMethods(),
                new CallWhiteList(),
                new Calls(),
                new InterfaceWhiteList(),
                new Implement(),
                new KeepOnlyVertexWithCodesmellLabel(),
                new RemoveEdges(),
                new ConvertNumericToBoolean(),
                new ProcessOnlyVerticesWithACodeSmell(),
                new ConvertNominalToBoolean(),
                new KeepOnlyBooleanProperties()
               // new SequentialAssociation()
        );
        applyProcessors();
        Path datasetPath = dumpGraph();
        Instances dataset = readDataset(datasetPath);
        Association association = new Association(dataset, associator, associator);
        List<Rule> rules = association.generateRules();
        logGeneratedRules(rules);
        List<Rule> filtered = applyFilters(rules);
        logFilteredRules(filtered);
        return filtered;
    }
}
