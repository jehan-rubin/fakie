package com.fakie.utils.logic;

import java.util.List;

public class And extends Operator {
    public And(List<Expression> expressions) {
        super(Type.AND, expressions);
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
        return "(" + formatExpressions(" ∧ ") + ")";
    }
}
