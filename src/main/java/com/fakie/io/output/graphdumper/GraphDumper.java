package com.fakie.io.output.graphdumper;

import com.fakie.io.IOPath;
import com.fakie.model.graph.Graph;
import com.fakie.utils.FakieUtils;
import com.fakie.utils.exceptions.FakieException;

import java.nio.file.Path;

public interface GraphDumper {
    default Path dump(Graph graph) throws FakieException {
        Path directory = IOPath.GRAPH_DIRECTORY.asPath();
        Path filename = IOPath.GRAPH_FILENAME.asPath();
        Path path = directory.resolve(FakieUtils.uniqueName()).resolve(filename);
        dump(path, graph);
        return path;
    }

    void dump(Path path, Graph graph) throws FakieException;
}
