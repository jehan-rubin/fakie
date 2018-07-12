package com.fakie.io.output.graphdumper;

import com.fakie.io.FakieIOException;
import com.fakie.io.IOPath;
import com.fakie.io.output.FakieOutputException;
import com.fakie.model.graph.Graph;
import com.fakie.utils.FakieUtils;

import java.nio.file.Path;

public interface GraphDumper {
    default Path dump(Graph graph) throws FakieIOException {
        Path directory = IOPath.GRAPH_DIRECTORY.asPath();
        Path filename = IOPath.GRAPH_FILENAME.asPath();
        Path path = FakieUtils.findResource(".").toPath()
                .resolve(directory)
                .resolve(FakieUtils.uniqueName())
                .resolve(filename);
        dump(path, graph);
        return path;
    }

    void dump(Path path, Graph graph) throws FakieOutputException;
}
