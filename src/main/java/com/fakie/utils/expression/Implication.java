package com.fakie.utils.expression;

public class Implication extends BinaryOperator {
    private static final Law[] laws = {
            Implication::equivalent,
            Law::none
    };

    Implication(Expression left, Expression right) {
        super(Type.IMPLY, left, right, laws);
    }

    public Implication contrapositive() {
        return newInstance(getRight().not(), getLeft().not());
    }

    @Override
    public Object eval() {
        return getLeft().and(getRight().not()).not().eval();
    }

    @Override
    public Implication newInstance(Expression left, Expression right) {
        return new Implication(left, right);
    }

    private static Expression equivalent(Expression expression) {
        if (expression.getType() == Type.IMPLY) {
            Implication op = expression.cast(Implication.class);
            return op.getLeft().and(op.getRight().not()).not().simplify();
        }
        return expression;
    }
}
