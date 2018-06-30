package com.fakie.model;

import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockedGraph {
    public static Graph wikipedia() {
        Graph graph = new Graph();
        Map<String, Object> properties = new HashMap<>();
        List<String> labels = new ArrayList<>();
        labels.add("app");
        properties.put("name", "Wikipedia");
        properties.put("number_of_classes", 0);
        graph.addVertex(new Vertex(0, labels, properties));
        labels.clear();
        labels.add("class");
        properties.clear();
        properties.put("name", "Main");
        properties.put("number_of_methods", 1);
        graph.addVertex(new Vertex(1, labels, properties));
        labels.clear();
        labels.add("class");
        properties.clear();
        properties.put("name", "SearchEngine");
        properties.put("number_of_methods", 42);
        graph.addVertex(new Vertex(2, labels, properties));
        return graph;
    }
}
