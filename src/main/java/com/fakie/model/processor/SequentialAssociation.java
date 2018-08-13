package com.fakie.model.processor;

import com.fakie.learning.Rule;
import com.fakie.learning.association.PreProcessing;
import com.fakie.model.graph.Element;
import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Property;
import com.fakie.model.graph.Vertex;
import com.fakie.utils.FakieUtils;
import com.fakie.utils.exceptions.FakieException;
import com.fakie.utils.expression.BinaryOperator;
import com.fakie.utils.expression.Expression;
import com.fakie.utils.expression.UnaryOperator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SequentialAssociation implements Processor {
    @Override
    public Graph process(Graph graph) throws FakieException {
        List<String> keys = new ArrayList<>(graph.keys());
        List<String> codesmells = keys.stream().filter(FakieUtils::isACodeSmell).collect(Collectors.toList());
        Set<String> finalProperties = new HashSet<>();
        int size = keys.size();
        int maxSize = numberOfInstances(graph);
        for (int i = 0; i < size; i += maxSize) {
            finalProperties.addAll(preProcess(graph, keys, codesmells, i, size, maxSize));
        }
        for (Element element : graph.getElements()) {
            for (Property property : element) {
                if (!finalProperties.contains(property.getKey())) {
                    element.removeProperty(property);
                }
            }
        }
        return graph;
    }

    private Set<String> preProcess(Graph graph, List<String> keys, List<String> codesmells,
                                   int i, int size, int maxSize) throws FakieException {
        Set<String> result = new HashSet<>();
        Graph copy = new Graph();
        for (Vertex vertex : graph.getVertices()) {
            Vertex v = copy.createVertex(vertex.getLabels());
            for (int j = i; j < Math.min(size, i + maxSize); j++) {
                String key = keys.get(j);
                if (vertex.hasProperty(key)) {
                    v.setProperty(key, vertex.getProperty(key));
                }
            }
            for (String codesmell : codesmells) {
                if (vertex.hasProperty(codesmell)) {
                    v.setProperty(codesmell, vertex.getProperty(codesmell));
                }
            }
        }
        PreProcessing preProcessing = new PreProcessing(copy, 100000, 0.1, 0.1);
        List<Rule> rules = preProcessing.generateRules();
        for (Rule rule : rules) {
            extractProperties(result, rule.premises());
            extractProperties(result, rule.consequences());
        }
        return result;
    }

    private void extractProperties(Set<String> result, Expression expression) {
        if (expression.getType().isVariable()) {
            result.add(expression.eval().toString());
        } else if (expression.getType().isUnaryOperator()) {
            extractProperties(result, expression.cast(UnaryOperator.class).getExpression());
        } else if (expression.getType().isBinaryOperator()) {
            BinaryOperator op = expression.cast(BinaryOperator.class);
            extractProperties(result, op.getLeft());
            extractProperties(result, op.getRight());
        }
    }

    private int numberOfInstances(Graph graph) {
        int result = 0;
        for (Vertex vertex : graph.getVertices()) {
            if (FakieUtils.containsACodeSmell(vertex)) {
                result += 1;
            }
        }
        return result;
    }
}
