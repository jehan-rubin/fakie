package com.fakie.model.processor;

import com.fakie.model.graph.Edge;
import com.fakie.model.graph.Graph;
import com.fakie.model.graph.Vertex;
import com.fakie.utils.exceptions.FakieException;
import com.fakie.utils.paprika.Label;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public class MethodWhiteList implements Processor {
    private static final Logger logger = LogManager.getFormatterLogger();
    private final Collection<String> whiteList = Arrays.asList(
            "clipRest",
            "quickReject",
            "onDraw",
            "onCreate",
            "onLowMemory",
            "onTrimMemory",
            "onPreExecute",
            "onProgressUpdate",
            "onPostExecute",
            "onStartCommand",
            "onReceive",
            "release",
            "lock",
            "setInexactRepeating",
            "close",
            "drawPosText",
            "drawPicture",
            "drawTextOnPath",
            "setLinearText",
            "setMaskFilter",
            "setPathEffect",
            "setSubPixelText",
            "drawPath"
    );

    @Override
    public Graph process(Graph graph) throws FakieException {
        logger.info("Keep only white listed methods in %s", graph);
        Set<Vertex> vertices = graph.findVerticesByLabel(Label.CLASS.toString());
        for (Vertex vertex : vertices) {
            for (Edge edge : vertex.outputEdges()) {
                if (!isInWhiteList(edge)) {
                    graph.remove(edge);
                }
            }
        }
        return graph;
    }

    private boolean isInWhiteList(Edge edge) {
        return !edge.getType().equals(Label.CLASS_OWNS_METHOD.toString()) ||
                whiteList.contains(edge.getDestination().getProperty(Label.NAME.toString()).toString());
    }
}
