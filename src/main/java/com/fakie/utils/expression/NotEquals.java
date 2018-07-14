package com.fakie.utils.expression;

public class NotEquals extends BinaryOperator {
    NotEquals(Expression left, Expression right) {
        super(Type.NEQ, left, right);
    }

    @Override
    public Boolean eval() {
        return !getLeft().eq(getRight()).eval();
    }
}