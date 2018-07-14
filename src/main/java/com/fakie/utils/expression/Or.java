package com.fakie.utils.expression;

public class Or extends BinaryOperator {
    Or(Expression left, Expression right) {
        super(Type.OR, left, right);
    }

    @Override
    public Boolean eval() {
        return getLeft().isTrue().eval() || getRight().isTrue().eval();
    }
}
