package com.fakie.utils.expression;

public class LessThan extends BinaryOperator {
    private static final Law[] laws = {
            Law::none
    };

    LessThan(Expression left, Expression right) {
        super(Type.LT, left, right, laws);
    }

    @Override
    public Boolean eval() {
        try {
            return ((Comparable) getLeft().eval()).compareTo(getRight().eval()) < 0;
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    public LessThan newInstance(Expression left, Expression right) {
        return new LessThan(left, right);
    }
}
