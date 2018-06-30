package com.fakie.io.input.graphloader;

import com.fakie.io.input.FakieInputException;
import com.fakie.model.graph.Edge;
import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Neo4j implements GraphLoader {
    private final GraphDatabaseService database;
    private final Graph graph;

    public Neo4j(Path path) {
        this.database = new GraphDatabaseFactory().newEmbeddedDatabase(path.toFile());
        this.graph = new Graph();
    }

    @Override
    public Graph load() {
        try (Transaction transaction = database.beginTx()) {
            populateGraph();
        }
        return graph;
    }

    @Override
    public void close() throws FakieInputException {
        database.shutdown();
    }

    private void populateGraph() {
        Map<Node, Vertex> mapping = addVertices();
        addEdges(mapping);
    }

    private Map<Node, Vertex> addVertices() {
        Map<Node, Vertex> mapping = new HashMap<>();
        for (Node node : database.getAllNodes()) {
            Vertex vertex = new Vertex(node.getId(), retrieveLabels(node), node.getAllProperties());
            mapping.put(node, vertex);
            graph.addVertex(vertex);
        }
        return mapping;
    }

    private List<String> retrieveLabels(Node node) {
        List<String> labels = new ArrayList<>();
        for (Label label : node.getLabels()) {
            labels.add(label.name());
        }
        return labels;
    }

    private void addEdges(Map<Node, Vertex> mapping) {
        for (Relationship rs : database.getAllRelationships()) {
            Vertex source = mapping.get(rs.getStartNode());
            Vertex destination = mapping.get(rs.getEndNode());
            graph.addEdge(new Edge(source, destination, rs.getType().name(), rs.getAllProperties()));
        }
    }
}
