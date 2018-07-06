package com.fakie.learning.filter;

import com.fakie.learning.Rule;
import com.fakie.utils.logic.And;
import com.fakie.utils.logic.Expression;
import com.fakie.utils.logic.Implication;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class FilterNonCodeSmellRuleTest {
    @Test
    public void shouldKeepBlob() {
        List<Rule> rules = new ArrayList<>();

        And left = new And(Collections.singletonList(new Expression("number_of_methods_greater_than_40", true)));
        And right = new And(Collections.singletonList(new Expression("CODE_SMELL_BLOB", true)));
        Implication implication = new Implication(left, right);

        Rule rule = new Rule(implication, 1, 1);
        rules.add(rule);

        FilterNonCodeSmellRule filterNonCodeSmellRule = new FilterNonCodeSmellRule();
        List<Rule> filter = filterNonCodeSmellRule.filter(rules);
        assertEquals(1, filter.size());
    }

    @Test
    public void shouldNotKeepCauseBlobIsFalse() {
        List<Rule> rules = new ArrayList<>();

        And left = new And(Collections.singletonList(new Expression("number_of_methods_greater_than_40", true)));
        And right = new And(Collections.singletonList(new Expression("CODE_SMELL_BLOB", false)));
        Implication implication = new Implication(left, right);

        Rule rule = new Rule(implication, 1, 1);
        rules.add(rule);

        FilterNonCodeSmellRule filterNonCodeSmellRule = new FilterNonCodeSmellRule();
        List<Rule> filter = filterNonCodeSmellRule.filter(rules);
        assertEquals(0, filter.size());
    }

    @Test
    public void shouldNotKeepCauseBlobIsInPremises() {
        List<Rule> rules = new ArrayList<>();

        And right = new And(Collections.singletonList(new Expression("number_of_methods_greater_than_40", true)));
        And left = new And(Collections.singletonList(new Expression("CODE_SMELL_BLOB", true)));
        Implication implication = new Implication(left, right);

        Rule rule = new Rule(implication, 1, 1);
        rules.add(rule);

        FilterNonCodeSmellRule filterNonCodeSmellRule = new FilterNonCodeSmellRule();
        List<Rule> filter = filterNonCodeSmellRule.filter(rules);
        assertEquals(0, filter.size());
    }

    @Test
    public void shouldKeepTheContrapositive() {
        List<Rule> rules = new ArrayList<>();

        And right = new And(Collections.singletonList(new Expression("number_of_methods_greater_than_40", true)));
        And left = new And(Collections.singletonList(new Expression("CODE_SMELL_BLOB", false)));
        Implication implication = new Implication(left, right);

        Rule rule = new Rule(implication, 1, 1);
        rules.add(rule);

        FilterNonCodeSmellRule filterNonCodeSmellRule = new FilterNonCodeSmellRule();
        List<Rule> filter = filterNonCodeSmellRule.filter(rules);
        assertEquals(1, filter.size());
        assertEquals(new Expression("CODE_SMELL_BLOB", true), filter.get(0).consequences().getExpressions().get(0));
    }
}