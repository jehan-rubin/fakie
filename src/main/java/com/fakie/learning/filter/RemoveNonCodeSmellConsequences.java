package com.fakie.learning.filter;

import com.fakie.learning.Rule;
import com.fakie.utils.FakieUtils;
import com.fakie.utils.expression.And;
import com.fakie.utils.expression.Equals;
import com.fakie.utils.expression.Expression;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class RemoveNonCodeSmellConsequences implements Filter {
    private static final Logger logger = LogManager.getFormatterLogger();

    @Override
    public List<Rule> filter(List<Rule> rules) {
        logger.info("Remove consequences which are not code smell from the rules (%d)", rules.size());
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
        Expression consequences = rule.consequences();
        Expression result = removeAllNonCodeSmell(consequences);
        return new Rule(rule.premises().imply(result), rule.getSupport(), rule.getConfidence());
    }

    private Expression removeAllNonCodeSmell(Expression expression) {
        if (expression.getType() == Expression.Type.EQ) {
            Equals op = expression.cast(Equals.class);
            Expression left = op.getLeft();
            Expression right = op.getRight();
            if (left.getType().isVariable() && right.getType().isVariable()) {
                boolean isACodeSmell = FakieUtils.isACodeSmell(left);
                return isACodeSmell ? expression : Expression.empty();
            }
            return op.newInstance(removeAllNonCodeSmell(left), removeAllNonCodeSmell(right));
        } else if (expression.getType() == Expression.Type.AND) {
            And and = expression.cast(And.class);
            return and.newInstance(removeAllNonCodeSmell(and.getLeft()), removeAllNonCodeSmell(and.getRight()));
        }
        return expression;
    }
}
