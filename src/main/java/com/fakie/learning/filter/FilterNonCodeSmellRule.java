package com.fakie.learning.filter;

import com.fakie.learning.Rule;
import com.fakie.utils.FakieUtils;
import com.fakie.utils.expression.BinaryOperator;
import com.fakie.utils.expression.Expression;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class FilterNonCodeSmellRule implements Filter {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public List<Rule> filter(List<Rule> rules) {
        logger.info("Filtering rules which does not contain a code smell (%d)", rules.size());
        List<Rule> filtered = new ArrayList<>();
        for (Rule rule : rules) {
            addRuleIfItContainsACodeSmell(filtered, rule);
            addRuleIfItContainsACodeSmell(filtered, rule.contrapositive());
        }
        return filtered;
    }

    private void addRuleIfItContainsACodeSmell(List<Rule> filtered, Rule rule) {
        Rule cleaned = filterFalseCodeSmell(rule);
        Expression premises = cleaned.premises();
        boolean codeSmellInPremises = containsACodeSmell(premises);

        Expression consequences = cleaned.consequences();
        boolean codeSmellInConsequences = containsACodeSmell(consequences);

        if (codeSmellInConsequences && !codeSmellInPremises) {
            filtered.add(rule);
        }
    }

    private Rule filterFalseCodeSmell(Rule rule) {
        Expression premises = filterFalseCodeSmell(rule.premises());
        Expression consequences = filterFalseCodeSmell(rule.consequences());
        return new Rule(premises.imply(consequences), rule.getSupport(), rule.getConfidence());
    }

    private Expression filterFalseCodeSmell(Expression expression) {
        if (expression.getType().isBinaryOperator()) {
            BinaryOperator op = expression.cast(BinaryOperator.class);
            Expression left = op.getLeft();
            Expression right = op.getRight();
            if (left.getType().isVariable() && right.getType().isVariable()) {
                boolean isACodeSmell = FakieUtils.isACodeSmell(left);
                boolean rightIsTrue = right.eq(true).eval();
                return !isACodeSmell || rightIsTrue ? expression : Expression.empty();
            }
            return op.newInstance(filterFalseCodeSmell(left), filterFalseCodeSmell(right));
        }
        return expression;
    }

    private boolean containsACodeSmell(Expression expression) {
        if (expression.getType().isBinaryOperator()) {
            BinaryOperator op = expression.cast(BinaryOperator.class);
            Expression left = op.getLeft();
            Expression right = op.getRight();
            if (left.getType().isVariable() && right.getType().isVariable()) {
                boolean isCodeSmell = FakieUtils.isACodeSmell(left);
                boolean rightIsTrue = right.eq(true).eval();
                return isCodeSmell && rightIsTrue;
            }
            return containsACodeSmell(left) || containsACodeSmell(right);
        }
        return false;
    }
}
