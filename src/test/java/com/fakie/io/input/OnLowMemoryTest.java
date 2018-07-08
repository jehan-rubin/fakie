package com.fakie.io.input;

import com.fakie.io.input.graphloader.Neo4j;
import com.fakie.model.graph.Edge;
import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;
import com.fakie.utils.Zipper;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.io.fs.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OnLowMemoryTest {
    private static final String NAME = "onlowmemory";
    private File dir;

    @Before
    public void setUp() throws URISyntaxException, ZipException {
        dir = Zipper.unzip(NAME);
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
            List<Edge> edges = graph.getEdges();
            assertEquals(15, vertices.size());
            assertEquals(16, edges.size());
        }
    }
}
