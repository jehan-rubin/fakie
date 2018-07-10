package com.fakie.model.processor;

import com.fakie.model.MockedGraph;
import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class CodeSmellTest {
    @Test
    public void applyOn() throws ProcessingException {
        Graph graph = MockedGraph.wikipedia();
        CodeSmell godClass = new CodeSmell(Collections.singletonList("class"),
                Collections.singletonMap("name", "Main"), "God Class");
        List<Vertex> processed = godClass.process(graph.getVertices());
        Graph newGraph = new Graph();
        newGraph.addVertices(processed);
        assertNotEquals(graph.getVertices(), newGraph.getVertices());
        Vertex vertex = newGraph.getVertices().get(1);
        assertEquals(true, vertex.getProperties().get("CODE_SMELL_God_Class"));
    }

    @Test
    public void noMatch() throws ProcessingException {
        Graph graph = MockedGraph.wikipedia();
        CodeSmell godClass = new CodeSmell(Collections.singletonList("method"), new HashMap<>(), "God Class");
        assertEquals(graph.getVertices(), godClass.process(graph.getVertices()));
    }

    @Test
    public void tooManyMatches() throws ProcessingException {
        Graph graph = MockedGraph.wikipedia();
        CodeSmell godClass = new CodeSmell(Collections.singletonList("class"), new HashMap<>(), "God Class");
        assertEquals(graph.getVertices(), godClass.process(graph.getVertices()));
    }
}