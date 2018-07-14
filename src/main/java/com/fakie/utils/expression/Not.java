package com.fakie.utils.expression;

public class Not extends UnaryOperator {
    Not(Expression expression) {
        super(Type.NOT, expression);
    }

    @Override
    public Boolean eval() {
        return getExpression().eq(false).eval();
    }
}
