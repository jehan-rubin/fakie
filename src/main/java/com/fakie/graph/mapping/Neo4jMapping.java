package com.fakie.graph.mapping;

import com.fakie.graph.model.Edge;
import com.fakie.graph.model.Graph;
import com.fakie.graph.model.Vertex;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.nio.file.Path;

public class Neo4jMapping implements GraphMapping {
    private final GraphDatabaseService neo4j;

    public Neo4jMapping(Path path) {
        neo4j = new GraphDatabaseFactory().newEmbeddedDatabase(path.toFile());
    }

    @Override
    public Graph convertToGraph() {
        Graph graph = new Graph();
        try (Transaction transaction = neo4j.beginTx()) {
            for (Node node : neo4j.getAllNodes()) {
                graph.addVertex(new Vertex(node.getAllProperties()));
            }
            for (Relationship rs : neo4j.getAllRelationships()) {
                graph.addEdge(new Edge(rs.getStartNode(), rs.getEndNode(), rs.getAllProperties()));
            }
        }
        return graph;
    }

    @Override
    public void close() {
        neo4j.shutdown();
    }
}
