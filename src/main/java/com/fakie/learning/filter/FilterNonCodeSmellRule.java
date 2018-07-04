package com.fakie.learning.filter;

import com.fakie.learning.LearningException;
import com.fakie.learning.Rule;
import com.fakie.model.processor.Keyword;
import com.fakie.utils.logic.*;

import java.util.ArrayList;
import java.util.List;

public class FilterNonCodeSmellRule implements Filter {
    @Override
    public List<Rule> filter(List<Rule> rules) {
        List<Rule> filtered = new ArrayList<>();
        for (Rule rule : rules) {
            addRuleIfItContainsACodeSmell(filtered, rule);
            addRuleIfItContainsACodeSmell(filtered, rule.contrapositive());
        }
        return filtered;
    }

    private void addRuleIfItContainsACodeSmell(List<Rule> filtered, Rule rule) {
        Rule cleaned = filterFalseCodeSmell(rule);
        Operator premises = cleaned.premises();
        boolean codeSmellInPremises = containsACodeSmell(premises);

        Operator consequences = cleaned.consequences();
        boolean codeSmellInConsequences = containsACodeSmell(consequences);

        if (codeSmellInConsequences && !codeSmellInPremises) {
            filtered.add(rule);
        }
    }

    private Rule filterFalseCodeSmell(Rule rule) {
        Operator premises = filterFalseCodeSmell(rule.premises());
        Operator consequences = filterFalseCodeSmell(rule.consequences());
        return new Rule(new Implication(premises, consequences), rule.getSupport());
    }

    private Operator filterFalseCodeSmell(Operator operator) {
        return operator.filter(expression -> (!isACodeSmell(expression) || expression.getValue()));
    }

    private boolean containsACodeSmell(Operator operator) {
        for (Expression expression : operator.getExpressions()) {
            if (isACodeSmell(expression) && expression.getValue()) {
                return true;
            }
        }
        return false;
    }

    private boolean isACodeSmell(Expression expression) {
        return expression.getAttribute().startsWith(Keyword.CODE_SMELL.toString());
    }
}
