package com.fakie.utils.expression;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;

public class None extends AbstractExpression {
    private static final None INSTANCE = new None();

    static None instance() {
        return INSTANCE;
    }

    private None() {
        super(Type.NONE, BigInteger.ZERO);
    }

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public int depth() {
        return 0;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public int variables() {
        return 0;
    }

    @Override
    public Object eval() {
        throw new NoneEvalException();
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
        return getType().toString();
    }
}
