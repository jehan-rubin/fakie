package com.fakie.learning;

import com.fakie.io.input.FakieInputException;
import com.fakie.io.input.dataset.DatasetReader;
import com.fakie.io.output.graphdumper.GraphDumper;
import com.fakie.learning.filter.Filter;
import com.fakie.model.graph.Graph;
import com.fakie.model.processor.Processor;
import com.fakie.utils.exceptions.FakieException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Orchestrator<D> implements Algorithm {
    private static final Logger logger = LogManager.getFormatterLogger();
    private Graph graph;
    private GraphDumper graphDumper;
    private DatasetReader<D> datasetReader;
    private List<Processor> processors = new ArrayList<>();
    private List<Filter> filters = new ArrayList<>();

    protected void useGraph(Graph graph) {
        this.graph = graph;
    }

    protected void useGraphDumper(GraphDumper graphDumper) {
        this.graphDumper = graphDumper;
    }

    protected void useDatasetReader(DatasetReader<D> datasetReader) {
        this.datasetReader = datasetReader;
    }

    protected void useProcessors(Processor... processors) {
        this.processors.addAll(Arrays.asList(processors));
    }

    protected void useFilters(Filter... filters) {
        this.filters.addAll(Arrays.asList(filters));
    }

    protected void applyProcessors() throws FakieException {
        for (Processor processor : processors) {
            this.graph = processor.process(graph);
        }
    }

    protected List<Rule> applyFilters(List<Rule> rules) throws LearningException {
        for (Filter filter : filters) {
            rules = filter.filter(rules);
        }
        return rules;
    }

    protected Path dumpGraph() throws FakieException {
        return graphDumper.dump(graph);
    }

    protected D readDataset(Path datasetPath) throws FakieInputException {
        return datasetReader.readDataset(datasetPath);
    }

    protected void logGeneratedRules(List<Rule> rules) {
        if (rules.isEmpty()) {
            logger.warn("Could not generate rules from the dataset");
        } else {
            logger.info("Generated rules (%d)", rules.size());
            for (Rule rule : rules) {
                logger.debug("\t %s", rule);
            }
        }
    }

    protected void logFilteredRules(List<Rule> rules) {
        if (rules.isEmpty()) {
            logger.warn("No rules left after filtering");
        } else {
            logger.info("Filtered rules (%d)", rules.size());
            for (Rule rule : rules) {
                logger.info("\t %s", rule);
            }
        }
    }
}
