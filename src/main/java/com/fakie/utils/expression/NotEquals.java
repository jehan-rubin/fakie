package com.fakie.utils.expression;

public class NotEquals extends BinaryOperator {
    private static final Law[] laws = {
            Law::none,
            new Law.Collapse(NotEquals::collapse),
            Law::commutativity
    };

    NotEquals(Expression left, Expression right) {
        super(Type.NEQ, left, right, laws);
    }

    @Override
    public Boolean eval() {
        return !getLeft().eq(getRight()).eval();
    }

    @Override
    public NotEquals newInstance(Expression left, Expression right) {
        return new NotEquals(left, right);
    }

    private static Expression collapse(Expression a, Expression b, Expression master) {
        if (a.getType().isVariable()) {
            if (a.eq(true).eval()) {
                return b.not();
            } else if (a.eq(false).eval()){
                return b.isTrue();
            }
        }
        return master;
    }
}