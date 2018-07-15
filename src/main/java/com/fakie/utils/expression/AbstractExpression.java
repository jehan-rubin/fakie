package com.fakie.utils.expression;

import java.math.BigInteger;
import java.util.Objects;

abstract class AbstractExpression implements Expression {
    static final BigInteger TYPE_SIZE = BigInteger.valueOf(Type.size() - 1);
    private final Type type;
    private final BigInteger id;

    AbstractExpression(Type type, BigInteger id) {
        this.type = type;
        this.id = id.multiply(TYPE_SIZE).add(BigInteger.valueOf(type.ordinal()));
    }

    @Override
    public BigInteger id() {
        return id;
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
