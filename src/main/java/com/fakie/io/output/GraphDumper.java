package com.fakie.io.output;

import com.fakie.model.graph.Graph;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface GraphDumper {
    default Path dump(Graph graph) throws FakieOutputException {
        URL url = getClass().getClassLoader().getResource(".");
        if (url == null) {
            throw new FakieOutputException("Could not locate resource folder");
        }
        try {
            Path path = new File(url.toURI()).toPath().resolve(createName());
            dump(path, graph);
            return path;
        }
        catch (URISyntaxException e) {
            throw new FakieOutputException(e);
        }
    }

    void dump(Path path, Graph graph) throws FakieOutputException;

    default String createName() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}
