package com.fakie.io.input.graphloader;

import com.fakie.io.input.FakieInputException;
import com.fakie.model.graph.Edge;
import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Neo4j implements GraphLoader {
    private static final Logger logger = LogManager.getFormatterLogger();
    private final GraphDatabaseService database;
    private final Graph graph;

    public Neo4j(Path path) {
        logger.debug("Opening database at \'%s\'", path);
        this.database = new GraphDatabaseFactory().newEmbeddedDatabase(path.toFile());
        this.graph = new Graph();
    }

    @Override
    public Graph load() {
        logger.trace("Loading graph");
        try (Transaction transaction = database.beginTx()) {
            populateGraph();
        }
        logger.debug("Successfully loaded graph in %s", graph);
        return graph;
    }

    @Override
    public void close() throws FakieInputException {
        logger.trace("Shutting down database");
        database.shutdown();
        logger.debug("The database has been successfully shutdown");
    }

    private void populateGraph() {
        Map<Node, Vertex> mapping = addVertices();
        addEdges(mapping);
    }

    private Map<Node, Vertex> addVertices() {
        Map<Node, Vertex> mapping = new HashMap<>();
        for (Node node : sortById(database.getAllNodes())) {
            Vertex vertex = graph.createVertex(retrieveLabels(node));
            vertex.setProperties(node.getAllProperties());
            logger.debug(vertex);
            mapping.put(node, vertex);
        }
        return mapping;
    }

    private <T extends Entity> List<T> sortById(ResourceIterable<T> collection) {
        return collection.stream()
                .sorted((n1, n2) -> Long.compare(n1.getId(), n2.getId()))
                .collect(Collectors.toList());
    }

    private List<String> retrieveLabels(Node node) {
        List<String> labels = new ArrayList<>();
        for (Label label : node.getLabels()) {
            labels.add(label.name());
        }
        return labels;
    }

    private void addEdges(Map<Node, Vertex> mapping) {
        for (Relationship rs : sortById(database.getAllRelationships())) {
            Vertex source = mapping.get(rs.getStartNode());
            Vertex destination = mapping.get(rs.getEndNode());
            Edge edge = graph.createEdge(source, destination, rs.getType().name());
            edge.setProperties(rs.getAllProperties());
            logger.debug(edge);
        }
    }
}
