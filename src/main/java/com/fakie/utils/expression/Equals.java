package com.fakie.utils.expression;

public class Equals extends BinaryOperator {
    Equals(Expression left, Expression right) {
        super(Type.EQ, left, right);
    }

    @Override
    public Boolean eval() {
        return getLeft().eval().equals(getRight().eval());
    }
}
