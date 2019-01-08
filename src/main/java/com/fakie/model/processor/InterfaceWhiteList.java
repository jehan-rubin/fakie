package com.fakie.model.processor;

import com.fakie.model.graph.Edge;
import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;
import com.fakie.utils.FakieUtils;
import com.fakie.utils.exceptions.FakieException;
import com.fakie.utils.paprika.Key;
import com.fakie.utils.paprika.Relationship;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class InterfaceWhiteList implements Processor{
    private static final Logger logger = LogManager.getFormatterLogger();
    private final Collection<String> whiteList = Arrays.asList(
            "java.io.Closeable",
            "org.wordpress.android.ui.themes.ThemePreviewFragment$ThemePreviewFragmentCallback",
            "org.wordpress.android.ui.themes.ThemePreviewFragment"
    );

    @Override
    public Graph process(Graph graph) throws FakieException{
        logger.info("Keep only white listed interfaces in %s", graph);
        Set<String> labels = findLabelOfCodesmell(graph);
        for(String label : labels){
            Set<Vertex> vertices = graph.findVerticesByLabel(label);
            for(Vertex vertex : vertices){
                for(Edge edge : vertex.outputEdges(Relationship.IMPLEMENTS.toString())){
                    if(!isInWhiteList(edge)){
                        graph.remove(edge);
                    }
                }
            }
        }
        return graph;
    }

    private boolean isInWhiteList(Edge edge) {
        return whiteList.contains(edge.getDestination().getProperty(Key.NAME.toString()).toString());
    }

    private Set<String> findLabelOfCodesmell(Graph graph) {
        Set<String> labels = new HashSet<>();
        for (Vertex vertex : graph.getVertices()) {
            if (FakieUtils.containsACodeSmell(vertex)) {
                labels.addAll(vertex.getLabels());
            }
        }
        return labels;
    }
}
