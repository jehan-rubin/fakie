package com.fakie.learning.filter;

import com.fakie.learning.Rule;
import com.fakie.utils.logic.And;
import com.fakie.utils.logic.Expression;
import com.fakie.utils.logic.Implication;
import com.fakie.utils.logic.Or;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class ManyToOneTest {
    @Test
    public void shouldKeepTheRuleWithOnlyBlobInConsequences() {
        List<Rule> rules = new ArrayList<>();

        And left = new And(Collections.singletonList(new Expression("number_of_methods_greater_than_40", true)));
        And right = new And(Collections.singletonList(new Expression("CODE_SMELL_BLOB", true)));
        Implication implication = new Implication(left, right);

        Rule rule = new Rule(implication, 1, 1);
        rules.add(rule);

        ManyToOne manyToOne = new ManyToOne();
        List<Rule> filter = manyToOne.filter(rules);
        assertEquals(1, filter.size());
    }

    @Test
    public void shouldNotKeepThisRuleCauseItHave2Consequences() {
        List<Rule> rules = new ArrayList<>();

        And left = new And(Collections.singletonList(new Expression("number_of_methods_greater_than_40", true)));
        And right = new And(Arrays.asList(new Expression("CODE_SMELL_BLOB", true), new Expression("useless_consequences", true)));
        Implication implication = new Implication(left, right);

        Rule rule = new Rule(implication, 1, 1);
        rules.add(rule);

        ManyToOne manyToOne = new ManyToOne();
        List<Rule> filter = manyToOne.filter(rules);
        assertEquals(0, filter.size());
    }
}