package com.fakie;

import com.fakie.io.input.FakieInputException;
import com.fakie.io.input.dataset.ARFFReader;
import com.fakie.io.input.dataset.DatasetHolder;
import com.fakie.io.input.dataset.DatasetReader;
import com.fakie.io.input.graphloader.Neo4j;
import com.fakie.io.output.FakieOutputException;
import com.fakie.io.output.GraphDumper;
import com.fakie.io.output.GraphToARFF;
import com.fakie.learning.Rule;
import com.fakie.learning.association.Association;
import com.fakie.model.graph.Graph;
import com.fakie.model.processor.ConvertPropertiesToBoolean;
import com.fakie.model.processor.ProcessingException;
import com.fakie.model.processor.Processor;
import com.fakie.utils.exceptions.FakieException;
import weka.associations.Apriori;
import weka.associations.AssociationRulesProducer;
import weka.associations.Associator;
import weka.associations.FPGrowth;
import weka.core.Instances;

import java.nio.file.Path;
import java.util.List;

public class Fakie {
    private Graph graph;

    public void loadGraphFromNeo4jDatabase(Path db) throws FakieInputException {
        try (Neo4j neo4j = new Neo4j(db)) {
            this.graph = neo4j.load();
        }
    }

    public List<Rule> fpGrowth() throws FakieException {
        return association(new FPGrowth());
    }

    public List<Rule> aPriori() throws FakieException {
        return association(new Apriori());
    }

    private <T extends Associator & AssociationRulesProducer> List<Rule> association(T t) throws FakieException {
        convertGraphProperties(new ConvertPropertiesToBoolean());
        Path datasetPath = dumpGraphToFile(new GraphToARFF());
        DatasetHolder<Instances> holder = readDataset(new ARFFReader(), datasetPath);
        Association association = new Association(holder, t, t);
        return association.generateRules();
    }

    private <T> DatasetHolder<T> readDataset(DatasetReader<T> reader, Path datasetPath) throws FakieInputException {
        return reader.readDataset(datasetPath);
    }

    private void convertGraphProperties(Processor processor) throws ProcessingException {
        this.graph = processor.process(graph);
    }

    private Path dumpGraphToFile(GraphDumper graphDumper) throws FakieOutputException {
        return graphDumper.dump(graph);
    }
}
