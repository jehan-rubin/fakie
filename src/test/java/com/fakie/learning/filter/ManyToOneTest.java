package com.fakie.learning.filter;

import com.fakie.learning.Rule;
import com.fakie.utils.expression.Expression;
import com.fakie.utils.expression.Implication;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ManyToOneTest {
    @Test
    public void shouldKeepTheRuleWithOnlyBlobInConsequences() {
        List<Rule> rules = new ArrayList<>();

        Implication implication = Expression.of("number_of_methods_greater_than_40").eq(true)
                .imply(Expression.of("CODE_SMELL_BLOB").eq(true));

        Rule rule = new Rule(implication, 1, 1);
        rules.add(rule);

        ManyToOne manyToOne = new ManyToOne();
        List<Rule> filter = manyToOne.filter(rules);
        assertEquals(1, filter.size());
    }

    @Test
    public void shouldNotKeepThisRuleCauseItHave2Consequences() {
        List<Rule> rules = new ArrayList<>();

        Implication implication = Expression.of("number_of_methods_greater_than_40").eq(true)
                .imply(Expression.of("CODE_SMELL_BLOB").eq(true).and(Expression.of("useless_consequences").eq(true)));

        Rule rule = new Rule(implication, 1, 1);
        rules.add(rule);

        ManyToOne manyToOne = new ManyToOne();
        List<Rule> filter = manyToOne.filter(rules);
        assertEquals(0, filter.size());
    }
}