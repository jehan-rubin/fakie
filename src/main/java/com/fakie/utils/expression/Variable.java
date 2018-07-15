package com.fakie.utils.expression;

import java.util.*;

public class Variable extends AbstractExpression {
    private static final Map<Object, Variable> instances = new HashMap<>();
    private final Object value;

    private Variable(Object value) {
        super(Type.VAR, instances.size());
        this.value = value;
    }

    static Variable of(Object value) {
        if (!instances.containsKey(value)) {
            instances.put(value, new Variable(value));
        }
        return instances.get(value);
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
