package com.fakie;

import com.fakie.io.input.FakieInputException;
import com.fakie.io.input.codesmell.JsonCodeSmellParser;
import com.fakie.io.input.dataset.ARFFReader;
import com.fakie.io.input.dataset.DatasetReader;
import com.fakie.io.input.graphloader.Neo4j;
import com.fakie.io.output.FakieOutputException;
import com.fakie.io.output.graphdumper.GraphDumper;
import com.fakie.io.output.graphdumper.GraphToARFF;
import com.fakie.io.output.queryexporter.Cypher;
import com.fakie.learning.LearningException;
import com.fakie.learning.Rule;
import com.fakie.learning.association.Association;
import com.fakie.learning.filter.*;
import com.fakie.model.processor.CodeSmell;
import com.fakie.model.graph.Graph;
import com.fakie.model.processor.*;
import com.fakie.utils.exceptions.FakieException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import weka.associations.Apriori;
import weka.associations.AssociationRulesProducer;
import weka.associations.Associator;
import weka.associations.FPGrowth;
import weka.core.Instances;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class Fakie {
    private static final Logger logger = LogManager.getFormatterLogger();
    private Graph graph;
    private List<Rule> rules;
    private List<CodeSmell> codeSmells;

    public void loadGraphFromNeo4jDatabase(Path db) throws FakieInputException {
        logger.info("Loading Neo4j Database");
        try (Neo4j neo4j = new Neo4j(db)) {
            this.graph = neo4j.load();
        }
        logger.info("Correctly loaded Graph from neo4j database");
    }

    public void addCodeSmellToGraph(File file) throws FakieInputException {
        codeSmells = new JsonCodeSmellParser().parse(file);
    }

    public void fpGrowth(int n, double support) throws FakieException {
        FPGrowth fpGrowth = new FPGrowth();
        fpGrowth.setNumRulesToFind(n);
        fpGrowth.setMinMetric(support);
        fpGrowth.setLowerBoundMinSupport(support);
        association(fpGrowth);
    }

    public void apriori() throws FakieException {
        association(new Apriori());
    }

    private <T extends Associator & AssociationRulesProducer> void association(T t) throws FakieException {
        if (graph == null) {
            logger.warn("The graph could not be found. Aborting association algorithm");
            return;
        }
        applyProcessors(
                new ApplyCodeSmellOnGraph(codeSmells),
                new ConvertLabelsToProperties(),
                new ConvertArraysToNominal(),
                new ConvertNumericToNominal(),
                new ProcessOnlyObjectsWithACodeSmell(),
                new ConvertNominalToBoolean(),
                new RemovePropertiesWithASingleValue()
        );

        Path datasetPath = dumpGraphToFile(new GraphToARFF());
        Instances dataset = readDataset(new ARFFReader(), datasetPath);
        Association association = new Association(dataset, t, t);
        rules = association.generateRules();
        generatedRules(rules);

        filterRules(
                new FilterNonCodeSmellRule(),
                new RemoveNonCodeSmellConsequences(),
                new ManyToOne(),
                new FilterRedundantRule()
        );

        filteredRules(rules);
    }

    private <T> T readDataset(DatasetReader<T> reader, Path datasetPath) throws FakieInputException {
        return reader.readDataset(datasetPath);
    }

    private void applyProcessors(Processor... processors) throws ProcessingException {
        for (Processor processor : processors) {
            this.graph = processor.process(graph);
        }
    }

    private Path dumpGraphToFile(GraphDumper graphDumper) throws FakieOutputException {
        return graphDumper.dump(graph);
    }

    private void filterRules(Filter... filters) throws LearningException {
        for (Filter filter : filters) {
            rules = filter.filter(rules);
        }
    }

    private void generatedRules(List<Rule> rules) {
        if (rules.isEmpty()) {
            logger.warn("Could not generate rules from the dataset");
        } else {
            logger.info("Generated rules (%d)", rules.size());
            for (Rule rule : rules) {
                logger.debug("\t %s", rule);
            }
        }
    }

    private void filteredRules(List<Rule> rules) {
        if (rules.isEmpty()) {
            logger.warn("No rules left after filtering");
        } else {
            logger.info("Filtered rules (%d)", rules.size());
            for (Rule rule : rules) {
                logger.info("\t %s", rule);
            }
        }
    }

    public void exportRulesAsCypherQueries(Path path) throws FakieOutputException {
        if (rules == null || rules.isEmpty()) {
            logger.warn("No rules found. Aborting export to Cypher");
            return;
        }
        logger.info("Exporting rules as Cypher queries in \'" + path + '\'');
        Cypher cypher = new Cypher();
        cypher.exportRulesAsQueries(path, rules);
        logger.info("Successfully exported rules as Cypher queries");
    }
}
