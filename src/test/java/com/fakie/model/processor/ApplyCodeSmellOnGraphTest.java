package com.fakie.model.processor;

import com.fakie.io.input.FakieInputException;
import com.fakie.io.input.codesmell.CodeSmells;
import com.fakie.io.input.codesmell.PaprikaDetectionParser;
import com.fakie.io.input.graphloader.Neo4j;
import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;
import com.fakie.utils.Zipper;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class ApplyCodeSmellOnGraphTest {
    private static final String NAME = "tor";
    private File db;
    private CodeSmells codeSmells;

    @Before
    public void setUp() throws Exception {
        URL dir = getClass().getClassLoader().getResource("codesmell/_BLOB.csv");
        assert dir != null : "Directory \'codesmell\' could not be located";
        URI uri = dir.toURI();
        File resource = new File(uri);
        PaprikaDetectionParser paprikaDetectionParser = new PaprikaDetectionParser();
        codeSmells = paprikaDetectionParser.parse(resource);
        db = Zipper.unzip(NAME);
    }

    @Test
    public void applyTorCodeSmells() throws FakieInputException, ProcessingException {
        try (Neo4j neo4j = new Neo4j(db.toPath())) {
            Graph graph = neo4j.load();
            int expected = countValues(graph) + 4;
            Graph process = new ApplyCodeSmellOnGraph(codeSmells).process(graph);
            assertEquals(expected, countValues(process));
        }
    }

    private int countValues(Graph graph) {
        int result = 0;
        for (Vertex vertex : graph.getVertices()) {
            result += vertex.values().size();
        }
        return result;
    }
}