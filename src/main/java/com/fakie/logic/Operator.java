package com.fakie.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class Operator {
    private final List<Expression> expressions;

    public Operator(List<Expression> expressions) {
        this.expressions = new ArrayList<>(expressions);
    }

    public List<Expression> getExpressions() {
        return new ArrayList<>(expressions);
    }

    public abstract Operator negation();

    protected List<Expression> getExpressionsNegations() {
        return getExpressions().stream().map(Expression::negation).collect(Collectors.toList());
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
        if (getClass() != o.getClass()) {
            return false;
        }
        return sameExpressions;
    }

    @Override
    public int hashCode() {
        return Objects.hash(expressions);
    }
}
