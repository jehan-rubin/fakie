package com.fakie.logic;

import java.util.List;
import java.util.stream.Collectors;

public class And extends Operator {
    public And(List<Expression> expressions) {
        super(expressions);
    }

    @Override
    public Operator negation() {
        return new Or(getExpressionsNegations());
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return getExpressions().stream().map(Expression::toString).collect(Collectors.joining(" ∧ "));
    }
}
