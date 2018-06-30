package com.fakie.io.output;

import com.fakie.io.IOPath;
import com.fakie.io.input.FakieInputException;
import com.fakie.io.input.dataset.ARFFReader;
import com.fakie.io.input.dataset.DatasetHolder;
import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.io.fs.FileUtils;
import weka.core.Instance;
import weka.core.Instances;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GraphToARFFTest {
    private Graph graph;
    private Path path;

    @Before
    public void setUp() throws URISyntaxException {
        graph = new Graph();
        Map<String, Object> properties = new HashMap<>();
        properties.put("wikipedia", "Wikipedia");
        properties.put("number_of_classes", 0);
        graph.addVertex(new Vertex(0, new ArrayList<>(), properties));
        Path graphPath = IOPath.GRAPH_DIRECTORY.asPath().resolve(IOPath.GRAPH_FILENAME.asPath());

        URL url = getClass().getClassLoader().getResource(".");
        assert url != null : "Could not locate resources folder";
        path = new File(url.toURI()).toPath().resolve(graphPath);
    }

    @After
    public void tearDown() {
        FileUtils.deleteFile(path.toFile());
    }

    @Test
    public void oneLineARFF() throws FakieOutputException, FakieInputException {
        GraphToARFF graphToARFF = new GraphToARFF();
        graphToARFF.dump(path, graph);
        assertTrue(path.toFile().exists());
        ARFFReader arffReader = new ARFFReader();
        DatasetHolder<Instances> holder = arffReader.readDataSet(path);
        Instances instances = holder.getDataset();
        Instance wikipedia = instances.get(0);
        assertEquals(1, instances.numInstances());
        assertEquals(2, wikipedia.numAttributes());
        assertEquals("0", wikipedia.toString(0));
        assertEquals("Wikipedia", wikipedia.toString(1));
    }
}