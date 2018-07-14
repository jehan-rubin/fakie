package com.fakie.utils.expression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class Variable extends AbstractExpression {
    private final Object value;

    Variable(Object value) {
        super(Type.VAR);
        this.value = value;
    }

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public int depth() {
        return 0;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public int variables() {
        return 1;
    }

    @Override
    public Object eval() {
        return value;
    }

    @Override
    public Collection<Expression> children() {
        return new ArrayList<>();
    }

    @Override
    public Collection<Expression> depthFirstChildren() {
        return new ArrayList<>();
    }

    @Override
    public Collection<Expression> breadthFirstChildren() {
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Variable variable = (Variable) o;
        return Objects.equals(value, variable.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
