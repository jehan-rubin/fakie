package com.fakie.utils.expression;

import java.util.*;

public abstract class UnaryOperator extends Operator {
    private Expression expression;

    public UnaryOperator(Type type, Expression expression) {
        super(type);
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public int depth() {
        return expression.depth() + 1;
    }

    @Override
    public int size() {
        return expression.size() + 1;
    }

    @Override
    public int variables() {
        return expression.variables();
    }

    @Override
    public Collection<Expression> children() {
        return Collections.singleton(expression);
    }

    @Override
    public Collection<Expression> depthFirstChildren() {
        List<Expression> children = new ArrayList<>(children());
        children.addAll(expression.depthFirstChildren());
        return children;
    }

    @Override
    public Collection<Expression> breadthFirstChildren() {
        List<Expression> children = new ArrayList<>(children());
        children.addAll(expression.breadthFirstChildren());
        return children;
    }

    @Override
    public Expression simplify() {
        setExpression(expression.simplify());
        if (expression.getType() == Type.EMPTY) {
            return Expression.empty();
        }
        return super.simplify();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        UnaryOperator that = (UnaryOperator) o;
        return Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), expression);
    }

    @Override
    public String toString() {
        return "(" + getType() + expression + ")";
    }
}
