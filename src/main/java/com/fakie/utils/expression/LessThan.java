package com.fakie.utils.expression;

public class LessThan extends BinaryOperator {
    LessThan(Expression left, Expression right) {
        super(Type.LT, left, right);
    }

    @Override
    public Boolean eval() {
        return ((Comparable) getLeft().eval()).compareTo(getRight().eval()) < 0;
    }
}
