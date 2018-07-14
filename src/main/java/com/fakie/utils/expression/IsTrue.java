package com.fakie.utils.expression;

public class IsTrue extends UnaryOperator {
    IsTrue(Expression expression) {
        super(Type.IS_TRUE, expression);
    }

    @Override
    public Boolean eval() {
        return getExpression().eq(true).eval() || getExpression().neq(0).eval();
    }
}
