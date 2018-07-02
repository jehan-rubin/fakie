package com.fakie.io.output.queryexporter;

import com.fakie.io.output.FakieOutputException;
import com.fakie.learning.Rule;

import java.util.List;

public interface QueryExporter {
    void exportRulesAsQueries(List<Rule> rules) throws FakieOutputException;
}
