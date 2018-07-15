package com.fakie.utils.expression;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class Operator extends AbstractExpression {
    private final List<Law> laws;

    Operator(Type type, BigInteger id, Law... laws) {
        super(type, id);
        this.laws = new ArrayList<>(Arrays.asList(laws));
    }

    @Override
    public Expression simplify() {
        Expression expression = super.simplify();
        for (Law law : laws) {
            if (expression.getType() != getType()) {
                return expression;
            }
            expression = law.apply(expression);
        }
        return expression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        Operator operator = (Operator) o;
        return Objects.equals(laws, operator.laws);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), laws);
    }
}
