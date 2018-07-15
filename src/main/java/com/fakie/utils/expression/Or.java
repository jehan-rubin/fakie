package com.fakie.utils.expression;

public class Or extends BinaryOperator {
    private static final Law[] laws = {
            Law::none,
            new Law.Annihilation(Expression.of(true), Expression.of(true)),
            new Law.Idempotent(Expression.of(false)),
            Law::associativity,
            Law::commutativity
    };

    Or(Expression left, Expression right) {
        super(Type.OR, left, right, laws);
    }

    @Override
    public Boolean eval() {
        return getLeft().isTrue().eval() || getRight().isTrue().eval();
    }

    @Override
    public Or newInstance(Expression left, Expression right) {
        return new Or(left, right);
    }
}
