package com.fakie.utils.expression;

import java.math.BigInteger;
import java.util.*;

public class Variable extends AbstractExpression {
    private static final Map<Object, Variable> instances = new HashMap<>();
    private static final Map<BigInteger, Variable> ids = new HashMap<>();
    public static final Variable False = Variable.of(false);
    public static final Variable True = Variable.of(true);
    private final Object value;
    private final BigInteger valueId;

    private Variable(Object value, BigInteger id) {
        super(Type.VAR, id);
        this.value = value;
        this.valueId = id;
    }

    static Variable of(Object value) {
        if (!instances.containsKey(value)) {
            Variable variable = new Variable(value, createId());
            instances.put(value, variable);
            ids.put(variable.valueId, variable);
        }
        return instances.get(value);
    }

    static Variable byId(BigInteger id) {
        return ids.get(id);
    }

    static Variable newVariable() {
        int i = 2;
        Set<Object> keys = instances.keySet();
        while (keys.contains(i)) {
            i += 1;
        }
        return Variable.of(i);
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
        return "'" + value.toString() + "'";
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

    private static BigInteger createId() {
        BigInteger id = BigInteger.ZERO;
        Set<BigInteger> idSet = ids.keySet();
        while (idSet.contains(id)) {
            id = id.add(BigInteger.ONE);
        }
        return id;
    }
}
