package com.fakie.logic;

import java.util.List;
import java.util.stream.Collectors;

public class Or extends Operator {
    public Or(List<Expression> expressions) {
        super(expressions);
    }

    @Override
    public Operator negation() {
        return new And(getExpressionsNegations());
    }

    @Override
    public String toString() {
        return getExpressions().stream().map(Expression::toString).collect(Collectors.joining(" v "));
    }
}
