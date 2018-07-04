package com.fakie.learning.filter;

import com.fakie.learning.LearningException;
import com.fakie.learning.Rule;

import java.util.List;

public interface Filter {
    List<Rule> filter(List<Rule> rules) throws LearningException;
}
