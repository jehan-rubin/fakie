package com.fakie.utils.expression;

public class And extends BinaryOperator {
    private static final Law[] laws = {
            Law::none,
            new Law.Annihilation(Expression.of(false), Expression.of(false)),
            new Law.Idempotent(Expression.of(true)),
            Law::associativity,
            Law::commutativity
    };

    And(Expression left, Expression right) {
        super(Type.AND, left, right, laws);
    }

    @Override
    public Boolean eval() {
        return getLeft().isTrue().eval() && getRight().isTrue().eval();
    }

    @Override
    public And newInstance(Expression left, Expression right) {
        return new And(left, right);
    }
}
