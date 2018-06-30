package com.fakie.model.processor;

import com.fakie.model.MockedGraph;
import com.fakie.model.graph.Graph;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertTrue;

public class ConvertPropertiesToBooleanTest {
    private Graph graph;

    @Before
    public void setUp() {
        graph = MockedGraph.wikipedia();
    }

    @Test
    public void transformWikipediaPropertiesToBoolean() throws ProcessingException {
        Processor processor = new ConvertPropertiesToBoolean();
        Graph processed = processor.process(graph);
        for (Set<Object> properties : processed.getProperties().values()) {
            for (Object property : properties) {
                assertTrue(property instanceof Boolean);
            }
        }
    }
}