package com.fakie.utils.expression;

public class Equals extends BinaryOperator {
    private static final Law[] laws = {
            Law::none,
            new Law.Collapse(Equals::collapse),
            Law::commutativity
    };

    Equals(Expression left, Expression right) {
        super(Type.EQ, left, right, laws);
    }

    @Override
    public Boolean eval() {
        return getLeft().eval().equals(getRight().eval());
    }

    @Override
    public Equals newInstance(Expression left, Expression right) {
        return new Equals(left, right);
    }

    private static Expression collapse(Expression a, Expression b, Expression master) {
        if (a.getType().isVariable()) {
            if (a.eq(true).eval()) {
                return b.isTrue();
            } else if (a.eq(false).eval()){
                return b.not();
            }
        }
        return master;
    }
}