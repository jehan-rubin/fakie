package com.fakie.io.output.queryexporter;

import com.fakie.io.output.FakieOutputException;
import com.fakie.learning.Rule;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public interface QueryExporter {
    void exportRulesAsQueries(Path path, List<Rule> rules) throws FakieOutputException;

    default String createName() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy MM dd - HH mm ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}
