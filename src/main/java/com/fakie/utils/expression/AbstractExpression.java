package com.fakie.utils.expression;

import java.util.Objects;

abstract class AbstractExpression implements Expression {
    private final Type type;

    AbstractExpression(Type type) {
        this.type = type;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public Expression simplify() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AbstractExpression that = (AbstractExpression) o;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
