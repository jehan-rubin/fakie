package com.fakie.learning.filter;

import com.fakie.learning.LearningException;
import com.fakie.learning.Rule;
import com.fakie.utils.expression.Expression;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FilterRedundantRuleTest {
    @Test
    public void lowerSupport() throws LearningException {
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule(Expression.of("LABEL_0_Class").eq(true)
                .imply(Expression.of("CODE_SMELL_BLOB").eq(true)), 1, 1));
        rules.add(new Rule(Expression.of("LABEL_0_Class").eq(true)
                .imply(Expression.of("CODE_SMELL_BLOB").eq(true)),
                0.9523809523809523, 0.9523809523809523));
        FilterRedundantRule filterRedundantRule = new FilterRedundantRule();
        List<Rule> filter = filterRedundantRule.filter(rules);
        assertEquals(1, filter.size());
        Expression.of("LABEL_0_Class").eq(true).imply(Expression.of("CODE_SMELL_BLOB").eq(true));
        Rule expected = new Rule(Expression.of("LABEL_0_Class").eq(true)
                .imply(Expression.of("CODE_SMELL_BLOB").eq(true)), 1, 1);
        assertEquals(expected, filter.get(0));
    }
}