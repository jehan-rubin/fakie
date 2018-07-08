package com.fakie;

import com.fakie.io.IOPath;
import com.fakie.io.input.FakieInputException;
import com.fakie.io.input.codesmell.JsonCodeSmellParser;
import com.fakie.io.input.codesmell.PaprikaDetectionParser;
import com.fakie.io.input.codesmell.ParserFactory;
import com.fakie.io.input.dataset.ARFFReader;
import com.fakie.io.input.graphloader.Neo4j;
import com.fakie.io.output.FakieOutputException;
import com.fakie.io.output.graphdumper.GraphToARFF;
import com.fakie.io.output.queryexporter.Cypher;
import com.fakie.learning.Rule;
import com.fakie.learning.association.Orchestrator;
import com.fakie.learning.filter.FilterNonCodeSmellRule;
import com.fakie.learning.filter.FilterRedundantRule;
import com.fakie.learning.filter.ManyToOne;
import com.fakie.learning.filter.RemoveNonCodeSmellConsequences;
import com.fakie.model.graph.Graph;
import com.fakie.model.processor.*;
import com.fakie.utils.exceptions.FakieException;
import com.fakie.utils.paprika.PaprikaAccessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import weka.associations.Apriori;
import weka.associations.FPGrowth;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class Fakie {
    private static final Logger logger = LogManager.getFormatterLogger();
    private final ParserFactory parserFactory;
    private Graph graph;
    private List<Rule> rules;
    private List<CodeSmell> codeSmells;

    public Fakie() {
        parserFactory = new ParserFactory(
                new JsonCodeSmellParser(),
                new PaprikaDetectionParser()
        );
    }

    public void runPaprikaAnalyse(File androidJars, File apk, File info, Path db) throws FakieException {
        logger.info("Running Paprika analyse on %s", db);
        new PaprikaAccessor().analyse(androidJars, apk, info, db);
    }

    public void runPaprikaQuery(Path db, String suffix) {
        logger.info("Running Paprika query on %s", db);
        new PaprikaAccessor().query(db, suffix);
    }

    public void loadGraphFromNeo4jDatabase(Path db) throws FakieInputException {
        logger.info("Loading Neo4j Database");
        try (Neo4j neo4j = new Neo4j(db)) {
            this.graph = neo4j.load();
        }
        logger.info("Correctly loaded Graph from neo4j database");
    }

    public void addCodeSmellToGraph(File file) throws FakieInputException {
        codeSmells = parserFactory.createInstance(file).parse(file);
    }

    public void fpGrowth(int n, double support) throws FakieException {
        logger.info("Applying FPGrowth on dataset with %d rules and a min support of %f", n, support);
        FPGrowth fpGrowth = new FPGrowth();
        fpGrowth.setNumRulesToFind(n);
        fpGrowth.setMinMetric(support);
        fpGrowth.setLowerBoundMinSupport(support);
        Orchestrator fpGrowthOrchestrator = new Orchestrator();
        fpGrowthOrchestrator.useAssociationAlgorithm(fpGrowth);
        orchestrateLearning(fpGrowthOrchestrator);
    }

    public void apriori(int n, double support) throws FakieException {
        logger.info("Applying Apriori on dataset with %d rules and a min support of %f", n, support);
        Apriori apriori = new Apriori();
        apriori.setNumRules(n);
        apriori.setMinMetric(support);
        apriori.setLowerBoundMinSupport(support);
        Orchestrator aprioriOrchestrator = new Orchestrator();
        aprioriOrchestrator.useAssociationAlgorithm(apriori);
        orchestrateLearning(aprioriOrchestrator);
    }

    private void orchestrateLearning(Orchestrator orchestrator) throws FakieException {
        if (graph == null) {
            logger.warn("The graph could not be found. Aborting association algorithm");
            return;
        }
        orchestrator.useGraph(graph);
        orchestrator.useProcessors(
                new ApplyCodeSmellOnGraph(codeSmells),
                new ConvertLabelsToProperties(),
                new ConvertArraysToNominal(),
                new ConvertNumericToNominal(),
                new ProcessOnlyVerticesWithACodeSmell(),
                new ConvertNominalToBoolean(),
                new RemovePropertiesWithASingleValue()
        );
        orchestrator.useGraphDumper(new GraphToARFF());
        orchestrator.useDatasetReader(new ARFFReader());
        orchestrator.useFilters(
                new FilterNonCodeSmellRule(),
                new RemoveNonCodeSmellConsequences(),
                new ManyToOne(),
                new FilterRedundantRule()
        );
        rules = orchestrator.orchestrate();
        logRules(rules);
    }

    private void logRules(List<Rule> rules) {
        if (rules.isEmpty()) {
            logger.warn("No rules left after filtering");
        }
        else {
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
