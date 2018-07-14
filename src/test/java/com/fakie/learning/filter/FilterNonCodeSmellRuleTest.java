package com.fakie.learning.filter;

import com.fakie.learning.Rule;
import com.fakie.utils.expression.Expression;
import com.fakie.utils.expression.Implication;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FilterNonCodeSmellRuleTest {
    @Test
    public void shouldKeepBlob() {
        List<Rule> rules = new ArrayList<>();

        Implication implication = Expression.of("number_of_methods_greater_than_40").eq(true)
                .imply(Expression.of("CODE_SMELL_BLOB").eq(true));

        Rule rule = new Rule(implication, 1, 1);
        rules.add(rule);

        FilterNonCodeSmellRule filterNonCodeSmellRule = new FilterNonCodeSmellRule();
        List<Rule> filter = filterNonCodeSmellRule.filter(rules);
        assertEquals(1, filter.size());
    }

    @Test
    public void shouldNotKeepCauseBlobIsFalse() {
        List<Rule> rules = new ArrayList<>();

        Implication implication = Expression.of("number_of_methods_greater_than_40").eq(true)
                .imply(Expression.of("CODE_SMELL_BLOB").eq(false));

        Rule rule = new Rule(implication, 1, 1);
        rules.add(rule);

        FilterNonCodeSmellRule filterNonCodeSmellRule = new FilterNonCodeSmellRule();
        List<Rule> filter = filterNonCodeSmellRule.filter(rules);
        assertEquals(0, filter.size());
    }

    @Test
    public void shouldNotKeepCauseBlobIsInPremises() {
        List<Rule> rules = new ArrayList<>();

        Implication implication = Expression.of("CODE_SMELL_BLOB").eq(true)
                .imply(Expression.of("number_of_methods_greater_than_40").eq(true));

        Rule rule = new Rule(implication, 1, 1);
        rules.add(rule);

        FilterNonCodeSmellRule filterNonCodeSmellRule = new FilterNonCodeSmellRule();
        List<Rule> filter = filterNonCodeSmellRule.filter(rules);
        assertEquals(0, filter.size());
    }
}