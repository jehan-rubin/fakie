package com.fakie.utils.expression;

public class IsTrue extends UnaryOperator {
    private static final Law[] laws = {
            Law::none,
    };

    IsTrue(Expression expression) {
        super(Type.IS_TRUE, expression, laws);
    }

    @Override
    public Boolean eval() {
        return getExpression().neq(false).eval() && getExpression().neq(0).eval();
    }

    @Override
    public IsTrue newInstance(Expression expression) {
        return new IsTrue(expression);
    }
}
