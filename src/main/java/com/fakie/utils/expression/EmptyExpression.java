package com.fakie.utils.expression;

import java.util.ArrayList;
import java.util.Collection;

public class EmptyExpression extends AbstractExpression {
    private static final EmptyExpression INSTANCE = new EmptyExpression();

    static EmptyExpression instance() {
        return INSTANCE;
    }

    private EmptyExpression() {
        super(Type.EMPTY);
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
        throw new EvalEmptyException();
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
