package com.fakie.io.output.queries;

import com.fakie.io.output.FakieOutputException;
import com.fakie.learning.Rule;

import java.util.List;

public interface QueryLanguage {
    void saveRulesAsQueries(List<Rule> rules) throws FakieOutputException;
}
