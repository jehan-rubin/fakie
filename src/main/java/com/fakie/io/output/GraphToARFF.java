package com.fakie.io.output;

import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class GraphToARFF implements GraphDumper {
    @Override
    public void dump(Path path, Graph graph) throws FakieOutputException {
        List<Attribute> attributes = createAttributes(graph);
        Instances dataSet = new Instances(createName(), new ArrayList<>(attributes), 0);
        dataSet.addAll(createInstances(graph, dataSet));
        save(path, dataSet);
    }

    private String createName() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    private List<Attribute> createAttributes(Graph graph) {
        List<Attribute> attributes = new ArrayList<>();
        Map<String, Set<Object>> properties = graph.getProperties();
        for (Map.Entry<String, Set<Object>> property : properties.entrySet()) {
            List<String> values = property.getValue().stream().map(Object::toString).collect(Collectors.toList());
            Attribute attribute = new Attribute(property.getKey(), values);
            for (Object value : property.getValue()) {
                attribute.addStringValue(value.toString());
            }
            attributes.add(attribute);
        }
        return attributes;
    }

    private List<Instance> createInstances(Graph graph, Instances dataset) {
        List<Instance> instances = new ArrayList<>();
        for (Vertex vertex : graph.getVertices()) {
            DenseInstance instance = new DenseInstance(dataset.numAttributes());
            for (Map.Entry<String, Object> property : vertex.getProperties().entrySet()) {
                Attribute attribute = dataset.attribute(property.getKey());
                instance.setValue(attribute, property.getValue().toString());
            }
            instances.add(instance);
        }
        return instances;
    }

    private void save(Path path, Instances instances) throws FakieOutputException {
        ArffSaver saver = new ArffSaver();
        saver.setInstances(instances);
        try {
            saver.setFile(path.toFile());
            saver.writeBatch();
        }
        catch (IOException e) {
            throw new FakieOutputException(e);
        }

    }
}
