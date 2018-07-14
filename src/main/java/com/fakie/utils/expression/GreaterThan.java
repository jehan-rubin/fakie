package com.fakie.utils.expression;

public class GreaterThan extends BinaryOperator {
    GreaterThan(Expression left, Expression right) {
        super(Type.GT, left, right);
    }

    @Override
    public Boolean eval() {
        return ((Comparable) getLeft().eval()).compareTo(getRight().eval()) > 0;
    }
}
