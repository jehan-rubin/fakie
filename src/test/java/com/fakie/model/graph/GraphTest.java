package com.fakie.model.graph;

import com.fakie.model.MockedGraph;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class GraphTest {
    @Test
    public void matchMain() {
        Graph graph = MockedGraph.wikipedia();
        List<Vertex> bestMatches = graph.matches(Collections.singletonList("class"),
                Collections.singletonMap("name", "Main"));
        assertEquals(1, bestMatches.size());
        Vertex vertex = graph.getVertices().get(1);
        assertEquals(vertex, bestMatches.get(0));
    }

    @Test
    public void matchClass() {
        Graph graph = MockedGraph.wikipedia();
        List<Vertex> bestMatches = graph.matches(Collections.singletonList("class"), new HashMap<>());
        assertEquals(2, bestMatches.size());
        List<Vertex> expected = Arrays.asList(graph.getVertices().get(1), graph.getVertices().get(2));
        assertEquals(expected, bestMatches);
    }

    @Test
    public void matchNone() {
        Graph graph = MockedGraph.wikipedia();
        List<Vertex> bestMatches = graph.matches(Arrays.asList("class", "app"), new HashMap<>());
        assertEquals(0, bestMatches.size());
    }
}