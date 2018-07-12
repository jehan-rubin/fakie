package com.fakie.io.input;

import com.fakie.io.input.graphloader.Neo4j;
import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.io.fs.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Neo4jTest {
    private static final Label appLabel = Label.label("App");
    private GraphDatabaseService graphDatabaseService;
    private File dir;

    @Before
    public void setUp() throws URISyntaxException {
        URL db = getClass().getClassLoader().getResource("db");
        assert db != null : "Could not locate the db directory";
        Path neo4j = new File(db.toURI()).toPath().resolve("neo4j");
        dir = neo4j.toFile();
        assert dir.mkdirs() : "Could not create neo4j resource folder";
        graphDatabaseService = new GraphDatabaseFactory()
                .newEmbeddedDatabaseBuilder(dir)
                .newGraphDatabase();
        try (Transaction transaction = graphDatabaseService.beginTx()) {
            Node node = graphDatabaseService.createNode(appLabel);
            node.setProperty("name", "wikipedia");
            transaction.success();
        }
        graphDatabaseService.shutdown();
    }

    @After
    public void tearDown() throws IOException {
        FileUtils.deletePathRecursively(dir.toPath());
    }

    @Test
    public void noEdgesAndOneVertex() throws FakieInputException {
        try (Neo4j neo4j = new Neo4j(dir.toPath())) {
            Graph graph = neo4j.load();
            List<Vertex> vertices = graph.getVertices();
            assertEquals(1, vertices.size());
            assertEquals("wikipedia", vertices.get(0).getProperty("name"));
            assertTrue(graph.getEdges().isEmpty());
        }
    }
}