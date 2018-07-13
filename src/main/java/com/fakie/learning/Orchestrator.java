package com.fakie.learning;

import com.fakie.io.input.dataset.DatasetReader;
import com.fakie.io.output.graphdumper.GraphDumper;
import com.fakie.learning.filter.Filter;
import com.fakie.model.graph.Graph;
import com.fakie.model.processor.ProcessingException;
import com.fakie.model.processor.Processor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import weka.core.Instances;

import java.util.Arrays;
import java.util.List;

public abstract class Orchestrator<T> implements Algorithm {
    private static final Logger logger = LogManager.getFormatterLogger();
    protected T associator;
    protected Graph graph;
    protected GraphDumper graphDumper;
    protected DatasetReader<Instances> datasetReader;
    protected List<Rule> rules;
    private List<Processor> processors;
    private List<Filter> filters;

    protected void useGraph(Graph graph) {
        this.graph = graph;
    }

    protected void useGraphDumper(GraphDumper graphDumper) {
        this.graphDumper = graphDumper;
    }

    protected void useDatasetReader(DatasetReader<Instances> datasetReader) {
        this.datasetReader = datasetReader;
    }

    protected void useProcessors(Processor... processors) {
        this.processors = Arrays.asList(processors);
    }

    protected void useFilters(Filter... filters) {
        this.filters = Arrays.asList(filters);
    }

    protected void useAssociationAlgorithm(T associator) {
        this.associator = associator;
    }

    protected void applyProcessors() throws ProcessingException {
        for (Processor processor : processors) {
            this.graph = processor.process(graph);
        }
    }

    protected void applyFilters() throws LearningException {
        for (Filter filter : filters) {
            rules = filter.filter(rules);
        }
    }

    protected void logGeneratedRules(List<Rule> rules) {
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

    protected void logFilteredRules(List<Rule> rules) {
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
}
