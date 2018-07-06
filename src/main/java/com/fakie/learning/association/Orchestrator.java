package com.fakie.learning.association;

import com.fakie.io.input.dataset.DatasetReader;
import com.fakie.io.output.graphdumper.GraphDumper;
import com.fakie.learning.LearningException;
import com.fakie.learning.Rule;
import com.fakie.learning.filter.Filter;
import com.fakie.model.graph.Graph;
import com.fakie.model.processor.ProcessingException;
import com.fakie.model.processor.Processor;
import com.fakie.utils.exceptions.FakieException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import weka.associations.AssociationRulesProducer;
import weka.associations.Associator;
import weka.core.Instances;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class Orchestrator {
    private static final Logger logger = LogManager.getFormatterLogger();
    private Graph graph;
    private GraphDumper graphDumper;
    private List<Processor> processors;
    private DatasetReader<Instances> datasetReader;
    private List<Rule> rules;
    private List<Filter> filters;
    private Associator associator;
    private AssociationRulesProducer associationRulesProducer;

    public void useGraph(Graph graph) {
        this.graph = graph;
    }

    public void useGraphDumper(GraphDumper graphDumper) {
        this.graphDumper = graphDumper;
    }

    public void useDatasetReader(DatasetReader<Instances> datasetReader) {
        this.datasetReader = datasetReader;
    }

    public void useProcessors(Processor... processors) {
        this.processors = Arrays.asList(processors);
    }

    public void useFilters(Filter... filters) {
        this.filters = Arrays.asList(filters);
    }

    public void useAssociator(Associator associator) {
        this.associator = associator;
    }

    public void useRulesProducer(AssociationRulesProducer associationRulesProducer) {
        this.associationRulesProducer = associationRulesProducer;
    }

    public <T extends Associator & AssociationRulesProducer> void useAssociationAlgorithm(T t) {
        useAssociator(t);
        useRulesProducer(t);
    }

    public List<Rule> orchestrate() throws FakieException {
        applyProcessors();
        Path datasetPath = graphDumper.dump(graph);
        Instances dataset = datasetReader.readDataset(datasetPath);
        Association association = new Association(dataset, associator, associationRulesProducer);
        rules = association.generateRules();
        generatedRules(rules);
        applyFilters();
        return rules;
    }

    private void applyProcessors() throws ProcessingException {
        for (Processor processor : processors) {
            this.graph = processor.process(graph);
        }
    }

    private void applyFilters() throws LearningException {
        for (Filter filter : filters) {
            rules = filter.filter(rules);
        }
    }

    private void generatedRules(List<Rule> rules) {
        if (rules.isEmpty()) {
            logger.warn("Could not generate rules from the dataset");
        }
        else {
            logger.info("Generated rules (%d)", rules.size());
            for (Rule rule : rules) {
                logger.debug("\t %s", rule);
            }
        }
    }
}
