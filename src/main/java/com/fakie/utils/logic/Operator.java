package com.fakie.utils.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class Operator {
    private final Type type;
    private final List<Expression> expressions;

    protected Operator(Type type, List<Expression> expressions) {
        this.type = type;
        this.expressions = new ArrayList<>(expressions);
    }

    public Type getType() {
        return type;
    }

    public List<Expression> getExpressions() {
        return new ArrayList<>(expressions);
    }

    public abstract Operator negation();

    protected List<Expression> getExpressionsNegations() {
        return getExpressions().stream().map(Expression::negation).collect(Collectors.toList());
    }

    protected String formatExpressions(String separator) {
        return expressions.stream().map(Expression::toString).collect(Collectors.joining(separator));
    }

    public abstract Operator newInstance(List<Expression> expressions);

    public Operator filter(Predicate<Expression> predicate) {
        List<Expression> filtered = new ArrayList<>();
        for (Expression expression : expressions) {
            if (predicate.test(expression)) {
                filtered.add(expression);
            }
        }
        return newInstance(filtered);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Operator))
            return false;
        Operator operator = (Operator) o;
        boolean sameExpressions = Objects.equals(expressions, operator.expressions);
        if (operator.expressions.size() <= 1 && sameExpressions) {
            return true;
        }
        return getClass() == o.getClass() && sameExpressions;
    }

    @Override
    public int hashCode() {
        return Objects.hash(expressions);
    }

    public enum Type {
        AND,
        OR
    }
}
