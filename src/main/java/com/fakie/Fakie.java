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
    private Path db = IOPath.DB.asPath();
    private Path queries = IOPath.CODE_SMELL.asPath();
    private Path cypherFolder = IOPath.CYPHER_FOLDER.asPath();
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
        if (db != null) {
            this.db = db;
        }
        logger.info("Running Paprika analyse on %s", apk);
        new PaprikaAccessor().analyse(androidJars, apk, info, this.db);
    }

    public void runPaprikaQuery(Path db, String suffix) {
        if (db != null) {
            this.db = db;
        }
        logger.info("Running Paprika query on %s", this.db);
        this.queries = new PaprikaAccessor().fuzzyQuery(this.db, suffix);
    }

    public void loadGraphFromNeo4jDatabase(Path db) throws FakieInputException {
        if (db != null) {
            this.db = db;
        }
        logger.info("Loading Neo4j Database from %s", this.db);
        try (Neo4j neo4j = new Neo4j(this.db)) {
            this.graph = neo4j.load();
        }
        logger.info("Correctly loaded %s from neo4j database", graph);
    }

    public void addCodeSmellToGraph(Path codesmell) throws FakieInputException {
        if (codesmell != null) {
            this.queries = codesmell;
        }
        codeSmells = parserFactory.createInstance(this.queries.toFile()).parse(this.queries.toFile());
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
                new RemoveEdges(),
                new ApplyCodeSmellOnGraph(codeSmells),
                new ConvertLabelsToProperties(),
                new ConvertArraysToNominal(),
                new ConvertNumericToBoolean(),
                new ProcessOnlyVerticesWithACodeSmell(),
                new ConvertNominalToBoolean(),
                new KeepOnlyBooleanProperties()
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
        if (path != null) {
            cypherFolder = path;
        }
        logger.info("Exporting rules as Cypher queries in \'" + this.cypherFolder + '\'');
        Cypher cypher = new Cypher();
        cypher.exportRulesAsQueries(this.cypherFolder, rules);
        logger.info("Successfully exported rules as Cypher queries");
    }
}
