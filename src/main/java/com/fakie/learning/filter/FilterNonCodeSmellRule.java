package com.fakie.learning.filter;

import com.fakie.learning.Rule;
import com.fakie.utils.logic.Expression;
import com.fakie.utils.logic.Implication;
import com.fakie.utils.logic.Operator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class FilterNonCodeSmellRule implements Filter {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public List<Rule> filter(List<Rule> rules) {
        logger.info("Filtering rules which does not contain a code smell");
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
        return new Rule(new Implication(premises, consequences), rule.getSupport(), rule.getConfidence());
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
}
