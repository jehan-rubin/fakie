package com.fakie.utils.expression;

public class GreaterThan extends BinaryOperator {
    private static final Law[] laws = {
            Law::none
    };

    GreaterThan(Expression left, Expression right) {
        super(Type.GT, left, right, laws);
    }

    @Override
    public Boolean eval() {
        try {
            return ((Comparable) getLeft().eval()).compareTo(getRight().eval()) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public GreaterThan newInstance(Expression left, Expression right) {
        return new GreaterThan(left, right);
    }
}
