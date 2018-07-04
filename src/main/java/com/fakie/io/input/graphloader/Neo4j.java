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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        for (Node node : database.getAllNodes()) {
            Vertex vertex = new Vertex(node.getId(), retrieveLabels(node), node.getAllProperties());
            logger.debug(vertex);
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
            Edge edge = new Edge(source, destination, rs.getType().name(), rs.getAllProperties());
            logger.debug(edge);
            graph.addEdge(edge);
        }
    }
}
