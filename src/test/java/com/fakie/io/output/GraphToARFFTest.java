package com.fakie.io.output;

import com.fakie.io.IOPath;
import com.fakie.io.input.FakieInputException;
import com.fakie.io.input.dataset.ARFFReader;
import com.fakie.io.input.dataset.DatasetHolder;
import com.fakie.model.MockedGraph;
import com.fakie.model.graph.Graph;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GraphToARFFTest {
    private Graph graph;
    private Path path;

    @Before
    public void setUp() throws URISyntaxException {
        graph = MockedGraph.wikipedia();
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
        DatasetHolder<Instances> holder = arffReader.readDataset(path);
        Instances instances = holder.getDataset();
        Instance app = instances.get(0);
        assertEquals(3, instances.numInstances());
        assertEquals(3, app.numAttributes());
        assertEquals("?", app.toString(0));
        assertEquals("0", app.toString(1));
        assertEquals("Wikipedia", app.toString(2));
        Instance main = instances.get(1);
        assertEquals("1", main.toString(0));
        assertEquals("?", main.toString(1));
        assertEquals("Main", main.toString(2));
        Instance searchEngine = instances.get(2);
        assertEquals("42", searchEngine.toString(0));
        assertEquals("?", searchEngine.toString(1));
        assertEquals("SearchEngine", searchEngine.toString(2));
    }
}