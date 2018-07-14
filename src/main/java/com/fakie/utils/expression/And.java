package com.fakie.utils.expression;

public class And extends BinaryOperator {
    And(Expression left, Expression right) {
        super(Type.AND, left, right);
    }

    @Override
    public Boolean eval() {
        return getLeft().isTrue().eval() && getRight().isTrue().eval();
    }
}
