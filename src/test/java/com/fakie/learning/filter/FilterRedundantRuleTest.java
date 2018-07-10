package com.fakie.learning.filter;

import com.fakie.learning.LearningException;
import com.fakie.learning.Rule;
import com.fakie.utils.logic.And;
import com.fakie.utils.logic.Expression;
import com.fakie.utils.logic.Implication;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FilterRedundantRuleTest {
    @Test
    public void lowerSupport() throws LearningException {
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule(new Implication(
                new And(Collections.singletonList(new Expression("LABEL_0_Class", true))),
                new And(Collections.singletonList(new Expression("CODE_SMELL_BLOB", true)))),
                1, 1));
        rules.add(new Rule(new Implication(
                new And(Collections.singletonList(new Expression("LABEL_0_Class", true))),
                new And(Collections.singletonList(new Expression("CODE_SMELL_BLOB", true)))),
                0.9523809523809523, 0.9523809523809523));
        FilterRedundantRule filterRedundantRule = new FilterRedundantRule();
        List<Rule> filter = filterRedundantRule.filter(rules);
        assertEquals(1, filter.size());
        Rule expected = new Rule(new Implication(
                new And(Collections.singletonList(new Expression("LABEL_0_Class", true))),
                new And(Collections.singletonList(new Expression("CODE_SMELL_BLOB", true)))),
                1, 1);
        assertEquals(expected, filter.get(0));
    }
}