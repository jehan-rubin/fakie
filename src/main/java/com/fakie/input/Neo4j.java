package com.fakie.input;

import com.fakie.graph.Edge;
import com.fakie.graph.Graph;
import com.fakie.graph.Vertex;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.nio.file.Path;

public class Neo4j implements InputFormat {
    private final GraphDatabaseService database;

    public Neo4j(Path path) {
        database = new GraphDatabaseFactory().newEmbeddedDatabase(path.toFile());
    }

    @Override
    public Graph convertToGraph() {
        Graph graph = new Graph();
        try (Transaction transaction = database.beginTx()) {
            for (Node node : database.getAllNodes()) {
                graph.addVertex(new Vertex(node.getAllProperties()));
            }
            for (Relationship rs : database.getAllRelationships()) {
                graph.addEdge(new Edge(rs.getStartNode(), rs.getEndNode(), rs.getAllProperties()));
            }
        }
        return graph;
    }

    @Override
    public void close() {
        database.shutdown();
    }
}
