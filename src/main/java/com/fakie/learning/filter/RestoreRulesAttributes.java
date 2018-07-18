package com.fakie.learning.filter;

import com.fakie.learning.LearningException;
import com.fakie.learning.Rule;
import com.fakie.utils.Keyword;
import com.fakie.utils.expression.BinaryOperator;
import com.fakie.utils.expression.Expression;
import com.fakie.utils.expression.UnaryOperator;

import java.util.ArrayList;
import java.util.List;

public class RestoreRulesAttributes implements Filter {
    @Override
    public List<Rule> filter(List<Rule> rules) throws LearningException {
        List<Rule> restored = new ArrayList<>();
        for (Rule rule : rules) {
            restored.add(restoreRule(rule));
        }
        return restored;
    }

    private Rule restoreRule(Rule rule) {
        Expression premises = restoreExpression(rule.premises());
        return new Rule(premises.simplify().imply(rule.consequences()), rule.getSupport(), rule.getConfidence());
    }

    private Expression restoreExpression(Expression expression) {
        if (expression.getType().isBinaryOperator()) {
            BinaryOperator op = expression.cast(BinaryOperator.class);
            return op.newInstance(restoreExpression(op.getLeft()), restoreExpression(op.getRight()));
        }
        if (expression.getType().isUnaryOperator()) {
            UnaryOperator op = expression.cast(UnaryOperator.class);
            return op.newInstance(restoreExpression(op.getExpression()));
        }
        if (expression.getType().isVariable()) {
            Object o = expression.eval();
            if (o instanceof String) {
                return restoreValue(o.toString());
            }
        }
        return expression;
    }

    private Expression restoreValue(String value) {
        if (value.startsWith(Keyword.LABEL.toString())) {
            return Expression.empty();
        }
        if (value.contains(Expression.Type.GT.toString())) {
            String[] split = value.split(Expression.Type.GT.toString());
            return restoreValue(split[0]).gt(Expression.of(Double.valueOf(split[1])));
        }
        if (value.contains(Expression.Type.LT.toString())) {
            String[] split = value.split(Expression.Type.LT.toString());
            return restoreValue(split[0]).lt(Expression.of(Double.valueOf(split[1])));
        }
        if (value.contains(Expression.Type.EQ.toString())) {
            String[] split = value.split(Expression.Type.EQ.toString());
            return restoreValue(split[0]).eq(restoreValue(split[1]));
        }
        return Expression.of(value);
    }
}
