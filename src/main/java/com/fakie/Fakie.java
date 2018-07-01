package com.fakie;

import com.fakie.io.input.FakieInputException;
import com.fakie.io.input.dataset.ARFFReader;
import com.fakie.io.input.dataset.DatasetReader;
import com.fakie.io.input.graphloader.Neo4j;
import com.fakie.io.output.FakieOutputException;
import com.fakie.io.output.graphdumper.GraphDumper;
import com.fakie.io.output.graphdumper.GraphToARFF;
import com.fakie.io.output.queries.Cypher;
import com.fakie.learning.Rule;
import com.fakie.learning.association.Association;
import com.fakie.model.graph.Graph;
import com.fakie.model.processor.ConvertPropertiesToBoolean;
import com.fakie.model.processor.ProcessingException;
import com.fakie.model.processor.Processor;
import com.fakie.utils.exceptions.FakieException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import weka.associations.Apriori;
import weka.associations.AssociationRulesProducer;
import weka.associations.Associator;
import weka.associations.FPGrowth;
import weka.core.Instances;

import java.nio.file.Path;
import java.util.List;

public class Fakie {
    private static final Logger logger = LogManager.getFormatterLogger();

    private Graph graph;
    private List<Rule> rules;

    public void loadGraphFromNeo4jDatabase(Path db) throws FakieInputException {
        logger.info("Loading Neo4j Database");
        try (Neo4j neo4j = new Neo4j(db)) {
            this.graph = neo4j.load();
        }
        logger.info("Correctly loaded Graph from neo4j database");
    }

    public void fpGrowth() throws FakieException {
        logger.debug("Applying FPGrowth algorithm to dataset...");
        rules = association(new FPGrowth());
        logRules(rules);
    }

    public void apriori() throws FakieException {
        logger.debug("Applying Apriori algorithm to dataset...");
        rules = association(new Apriori());
        logRules(rules);
    }

    private <T extends Associator & AssociationRulesProducer> List<Rule> association(T t) throws FakieException {
        convertGraphProperties(new ConvertPropertiesToBoolean());
        Path datasetPath = dumpGraphToFile(new GraphToARFF());
        Instances dataset = readDataset(new ARFFReader(), datasetPath);
        Association association = new Association(dataset, t, t);
        return association.generateRules();
    }

    private <T> T readDataset(DatasetReader<T> reader, Path datasetPath) throws FakieInputException {
        return reader.readDataset(datasetPath);
    }

    private void convertGraphProperties(Processor processor) throws ProcessingException {
        this.graph = processor.process(graph);
    }

    private Path dumpGraphToFile(GraphDumper graphDumper) throws FakieOutputException {
        return graphDumper.dump(graph);
    }

    private void logRules(List<Rule> rules) {
        logger.info("Generated rules : ");
        for (Rule rule : rules) {
            logger.info("\t %s", rule);
        }
    }

    public void saveRulesAsCypherQueries() {
        Cypher cypher = new Cypher();
        cypher.saveRulesAsQueries(rules);
    }
}
