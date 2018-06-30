package com.fakie.io.input;

import com.fakie.io.input.graphloader.Neo4j;
import com.fakie.model.graph.Edge;
import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;
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
    private static final String name = "onlowmemory";
    private File dir;

    @Before
    public void setUp() throws URISyntaxException, ZipException {
        URL db = getClass().getClassLoader().getResource(Paths.get("db").toString());
        assert db != null : "Could not locate the db directory";
        Path path = new File(db.toURI()).toPath();
        ZipFile zipFile = new ZipFile(path.resolve(name + ".zip").toFile());
        dir = path.resolve(name).toFile();
        zipFile.extractAll(path.toString());
    }

    @After
    public void tearDown() throws IOException {
        FileUtils.deletePathRecursively(dir.toPath());
    }

    @Test
    public void noEdgesAndOneVertex() {
        Neo4j neo4j = new Neo4j();
        Graph graph = neo4j.load(dir.toPath());
        List<Vertex> vertices = graph.getVertices();
        List<Edge> edges = graph.getEdges();
        assertEquals(15, vertices.size());
        assertEquals(16, edges.size());
    }
}
