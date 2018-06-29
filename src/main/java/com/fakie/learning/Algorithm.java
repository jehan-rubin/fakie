package com.fakie.learning;

import java.util.List;

public interface Algorithm {
    List<Rule> generateRules() throws LearningException;
}
