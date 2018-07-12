package com.fakie.model.converter;

import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Property;
import com.fakie.model.graph.Vertex;
import com.fakie.utils.FakieUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class InstancesConverter implements Converter<Instances> {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public Graph load(Instances instances) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Instances dump(Graph graph) {
        List<Attribute> attributes = createAttributes(graph);
        logger.debug("Attributes extracted from graph : %s", attributes);
        Instances dataset = new Instances(FakieUtils.uniqueName(), new ArrayList<>(attributes), 0);
        dataset.addAll(createInstances(graph, dataset));
        return dataset;
    }

    private List<Attribute> createAttributes(Graph graph) {
        List<Attribute> attributes = new ArrayList<>();
        for (String key : graph.keys()) {
            Set<String> values = graph.values(key).stream().map(Object::toString).collect(Collectors.toSet());
            Attribute attribute = new Attribute(key, new ArrayList<>(values));
            attributes.add(attribute);
        }
        return attributes;
    }

    private List<Instance> createInstances(Graph graph, Instances dataset) {
        List<Instance> instances = new ArrayList<>();
        for (Vertex vertex : graph.getVertices()) {
            DenseInstance instance = new DenseInstance(dataset.numAttributes());
            for (Property property : vertex) {
                Attribute attribute = dataset.attribute(property.getKey());
                instance.setValue(attribute, property.getValue().toString());
            }
            instances.add(instance);
        }
        return instances;
    }
}
