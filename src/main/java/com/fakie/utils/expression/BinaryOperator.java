package com.fakie.utils.expression;

import com.fakie.utils.FakieUtils;

import java.util.*;

public abstract class BinaryOperator extends Operator {
    private final Expression left;
    private final Expression right;

    BinaryOperator(Expression.Type type, Expression left, Expression right, Law... laws) {
        super(type, FakieUtils.pair(left.id(), right.id()), laws);
        this.left = left;
        this.right = right;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public int depth() {
        return Math.max(left.depth(), right.depth()) + 1;
    }

    @Override
    public int size() {
        return left.size() + right.size() + 1;
    }

    @Override
    public int variables() {
        return left.variables() + right.variables();
    }

    @Override
    public Collection<Expression> children() {
        return Arrays.asList(left, right);
    }

    @Override
    public Collection<Expression> depthFirstChildren() {
        List<Expression> children = new ArrayList<>();
        children.add(left);
        children.addAll(left.depthFirstChildren());
        children.add(right);
        children.addAll(right.depthFirstChildren());
        return children;
    }

    @Override
    public Collection<Expression> breadthFirstChildren() {
        Deque<Expression> temp = new ArrayDeque<>(children());
        List<Expression> children = new ArrayList<>();
        while (!temp.isEmpty()) {
            Expression pop = temp.pop();
            children.add(pop);
            temp.addAll(pop.children());
        }
        return children;
    }

    public abstract BinaryOperator newInstance(Expression left, Expression right);

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        BinaryOperator that = (BinaryOperator) o;
        return Objects.equals(left, that.left) &&
                Objects.equals(right, that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), left, right);
    }

    @Override
    public String toString() {
        return "(" + left + getType() + right + ")";
    }
}
