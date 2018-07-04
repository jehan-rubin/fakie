package com.fakie.learning.filter;

import com.fakie.learning.LearningException;
import com.fakie.learning.Rule;
import com.fakie.model.processor.Keyword;
import com.fakie.utils.logic.Expression;

import java.util.List;

public interface Filter {
    List<Rule> filter(List<Rule> rules) throws LearningException;

    default boolean isACodeSmell(Expression expression) {
        return expression.getAttribute().startsWith(Keyword.CODE_SMELL.toString());
    }
}
