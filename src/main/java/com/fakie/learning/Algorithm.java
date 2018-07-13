package com.fakie.learning;

import com.fakie.utils.exceptions.FakieException;

import java.util.List;

public interface Algorithm {
    List<Rule> generateRules() throws FakieException;
}
