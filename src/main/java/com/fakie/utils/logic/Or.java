package com.fakie.utils.logic;

import java.util.List;

public class Or extends Operator {
    public Or(List<Expression> expressions) {
        super(Type.OR, expressions);
    }

    @Override
    public Or newInstance(List<Expression> expressions) {
        return new Or(expressions);
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
