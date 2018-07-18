package com.fakie.model.converter;

import com.fakie.model.FakieModelException;
import com.fakie.model.graph.Element;
import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Property;
import com.fakie.model.graph.Type;
import com.fakie.utils.FakieUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InstancesConverter implements Converter<Instances> {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public Graph load(Instances instances) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Instances dump(Graph graph) throws FakieModelException {
        if (graph.isEmpty()) {
            throw new FakieModelException("Empty Graph can not be converted to Instances");
        }
        List<Attribute> attributes = createAttributes(graph);
        logger.debug("Attributes extracted from graph : %s", attributes);
        Instances dataset = new Instances(FakieUtils.uniqueName(), new ArrayList<>(attributes), 0);
        dataset.addAll(createInstances(graph, dataset));
        return dataset;
    }

    private List<Attribute> createAttributes(Graph graph) {
        List<Attribute> attributes = new ArrayList<>();
        for (String key : graph.keys()) {
            List<String> values = graph.values(key).stream()
                    .map(Object::toString)
                    .distinct()
                    .collect(Collectors.toList());
            Attribute attribute = graph.type(key).isNumber() ? new Attribute(key) : new Attribute(key, values);
            attributes.add(attribute);
        }
        return attributes;
    }

    private List<Instance> createInstances(Graph graph, Instances dataset) {
        List<Instance> instances = new ArrayList<>();
        for (Element element : graph.getElements()) {
            DenseInstance instance = new DenseInstance(dataset.numAttributes());
            for (Property property : element) {
                Attribute attribute = dataset.attribute(property.getKey());
                if (graph.type(property.getKey()) == Type.INTEGER) {
                    instance.setValue(attribute, property.<Integer>getValue());
                } else if (graph.type(property.getKey()) == Type.DOUBLE) {
                    instance.setValue(attribute, property.<Double>getValue());
                } else {
                    instance.setValue(attribute, property.getValue().toString());
                }
            }
            instances.add(instance);
        }
        return instances;
    }
}
