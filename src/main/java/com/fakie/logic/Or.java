package com.fakie.logic;

import java.util.List;

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
        return "(" + formatExpressions(" v ") + ")";
    }
}
