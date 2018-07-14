package com.fakie.utils.expression;

public class Implication extends BinaryOperator {
    Implication(Expression left, Expression right) {
        super(Type.IMPLY, left, right);
    }

    public Implication contrapositive() {
        return new Implication(getRight().not(), getLeft().not());
    }

    @Override
    public Object eval() {
        return getLeft().not().or(getRight()).eval();
    }
}
