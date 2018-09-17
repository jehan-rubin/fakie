package com.fakie;

import com.fakie.cli.Std;
import com.fakie.io.IOPath;
import com.fakie.io.input.FakieInputException;
import com.fakie.io.input.codesmell.CodeSmells;
import com.fakie.io.input.codesmell.JsonCodeSmellParser;
import com.fakie.io.input.codesmell.PaprikaDetectionParser;
import com.fakie.io.input.codesmell.ParserFactory;
import com.fakie.io.input.graphloader.Neo4j;
import com.fakie.io.output.FakieOutputException;
import com.fakie.io.output.queryexporter.Cypher;
import com.fakie.learning.Algorithm;
import com.fakie.learning.Rule;
import com.fakie.learning.association.AprioriAlgorithm;
import com.fakie.learning.association.FPGrowthAlgorithm;
import com.fakie.model.graph.Graph;
import com.fakie.utils.exceptions.FakieException;
import com.fakie.utils.paprika.PaprikaAccessor;
import com.fakie.utils.paprika.PaprikaException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private CodeSmells codeSmells;

    public Fakie() {
        parserFactory = new ParserFactory(
                new JsonCodeSmellParser(),
                new PaprikaDetectionParser()
        );
    }

    public void runPaprikaAnalyse(Std std, File androidJars, File apk, File info, Path db) throws FakieException {
        if (db != null) {
            this.db = db;
        }
        logger.info("Running Paprika analyse on %s", apk);
        new PaprikaAccessor(std).analyse(androidJars, apk, info, this.db);
        logger.info("Successfully analysed the apk in %s", this.db);
    }

    public void runPaprikaQuery(Std std, Path db, String suffix) throws PaprikaException {
        if (db != null) {
            this.db = db;
        }
        logger.info("Running Paprika query on %s", this.db);
        this.queries = new PaprikaAccessor(std).query(this.db, suffix);
        logger.info("Successfully saved queries in %s", this.queries);
    }

    public void runPaprikaCustomQuery(Std std, Path db, String suffix, String query) throws PaprikaException{
        if (db != null) {
            this.db = db;
        }
        logger.info("Running Paprika custom query on %s", this.db);
        this.queries = new PaprikaAccessor(std).customQuery(this.db, suffix, query);
        logger.info("Successfully saved queries in %s", this.queries);
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

    public void readCodeSmellFile(Path codesmell) throws FakieInputException {
        if (codesmell != null) {
            this.queries = codesmell;
        }
        codeSmells = parserFactory.createInstance(this.queries.toFile()).parse(this.queries.toFile());
    }

    public void fpGrowth(int n, double support) throws FakieException {
        logger.info("Applying FPGrowth algorithm on %s with %d rules and a min support of %f", graph, n, support);
        FPGrowthAlgorithm fpGrowth = new FPGrowthAlgorithm(graph, codeSmells, n, support);
        applyingAlgorithm(fpGrowth);
    }

    public void apriori(int n, double support) throws FakieException {
        logger.info("Applying Apriori on dataset with %d rules and a min support of %f", n, support);
        AprioriAlgorithm aprioriAlgorithm = new AprioriAlgorithm(graph, codeSmells, n, support);
        applyingAlgorithm(aprioriAlgorithm);
    }

    private void applyingAlgorithm(Algorithm algorithm) throws FakieException {
        if (graph == null) {
            logger.warn("The graph could not be found. Aborting association algorithm");
            return;
        }
        rules = algorithm.generateRules();
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
