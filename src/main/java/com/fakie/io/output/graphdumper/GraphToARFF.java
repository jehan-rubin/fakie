package com.fakie.io.output.graphdumper;

import com.fakie.io.output.FakieOutputException;
import com.fakie.model.converter.InstancesConverter;
import com.fakie.model.graph.Graph;
import com.fakie.utils.exceptions.FakieException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

import java.io.IOException;
import java.nio.file.Path;

public class GraphToARFF implements GraphDumper {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public void dump(Path path, Graph graph) throws FakieException {
        logger.info("Dumping graph %s as ARFF file at path \'%s\'", graph, path);
        Instances dataset = new InstancesConverter().dump(graph);
        save(path, dataset);
    }

    private void save(Path path, Instances instances) throws FakieOutputException {
        logger.trace("Saving dataset");
        ArffSaver saver = new ArffSaver();
        saver.setInstances(instances);
        try {
            saver.setFile(path.toFile());
            saver.writeBatch();
        }
        catch (IOException e) {
            throw new FakieOutputException(e);
        }
        logger.info("Successfully saved dataset to \'%s\'", path);
    }
}
