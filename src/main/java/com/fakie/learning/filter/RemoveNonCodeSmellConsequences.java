package com.fakie.learning.filter;

import com.fakie.learning.Rule;
import com.fakie.utils.logic.Expression;
import com.fakie.utils.logic.Implication;
import com.fakie.utils.logic.Operator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RemoveNonCodeSmellConsequences implements Filter {
    @Override
    public List<Rule> filter(List<Rule> rules) {
        List<Rule> filtered = new ArrayList<>();
        for (Rule rule : rules) {
            addRuleWithOnlyCodeSmellInConsequences(filtered, rule);
        }
        return filtered;
    }

    private void addRuleWithOnlyCodeSmellInConsequences(List<Rule> filtered, Rule rule) {
        Rule clean = removeAllNonCodeSmellConsequences(rule);
        filtered.add(clean);
    }

    private Rule removeAllNonCodeSmellConsequences(Rule rule) {
        Operator consequences = rule.consequences();
        Rule result = rule;
        if (consequences.getType() == Operator.Type.AND) {
            for (Expression expression : consequences.getExpressions()) {
                if (isACodeSmell(expression)) {
                    Operator operator = consequences.newInstance(Collections.singletonList(expression));
                    Implication implication = new Implication(rule.premises(), operator);
                    result = new Rule(implication, rule.getSupport(), rule.getConfidence());
                }
            }
        }
        return result;
    }
}
