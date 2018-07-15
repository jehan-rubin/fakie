package com.fakie.utils.expression;

import java.math.BigInteger;
import java.util.Collection;

public interface Expression extends Comparable<Expression> {
    BigInteger id();

    Object eval();

    Type getType();

    int arity();

    int depth();

    int size();

    int variables();

    Collection<Expression> children();

    Collection<Expression> depthFirstChildren();

    Collection<Expression> breadthFirstChildren();

    @Override
    default int compareTo(Expression o) {
        if (equals(o)) {
            return 0;
        }
        return size() > o.size() || (size() == o.size()) && id().compareTo(o.id()) > 0 ? 1 : -1;
    }

    default <T extends Expression> T cast(Class<T> cls) {
        return cls.cast(this);
    }

    static None empty() {
        return None.instance();
    }

    static Variable of(Object o) {
        return Variable.of(o);
    }

    default Not not() {
        return new Not(this);
    }

    default IsTrue isTrue() {
        return new IsTrue(this);
    }

    default Or or(Object o) {
        return or(Expression.of(o));
    }

    default Or or(Expression other) {
        return new Or(this, other);
    }

    default And and(Object o) {
        return and(Expression.of(o));
    }

    default And and(Expression other) {
        return new And(this, other);
    }

    default GreaterThan gt(Object o) {
        return gt(Expression.of(o));
    }

    default GreaterThan gt(Expression other) {
        return new GreaterThan(this, other);
    }

    default LessThan lt(Object o) {
        return lt(Expression.of(o));
    }

    default LessThan lt(Expression other) {
        return new LessThan(this, other);
    }

    default Equals eq(Object o) {
        return eq(Expression.of(o));
    }

    default Equals eq(Expression other) {
        return new Equals(this, other);
    }

    default NotEquals neq(Object o) {
        return neq(Expression.of(o));
    }

    default NotEquals neq(Expression other) {
        return new NotEquals(this, other);
    }

    default Implication imply(Object o) {
        return imply(Expression.of(o));
    }

    default Implication imply(Expression other) {
        return new Implication(this, other);
    }

    Expression simplify();

    enum Type {
        NONE("None", false, false),
        VAR("", false, false),
        IS_TRUE("⊨", true, false),
        NOT("¬", true, false),
        OR(" ∨ ", false, true),
        AND(" ∧ ", false, true),
        GT(" > ", false, true),
        LT(" < ", false, true),
        EQ(" == ", false, true),
        NEQ(" != ", false, true),
        IMPLY(" => ", false, true);

        private final String representation;
        private final boolean unaryOperator;
        private final boolean binaryOperator;

        Type(String representation, boolean unaryOperator, boolean binaryOperator) {
            this.representation = representation;
            this.unaryOperator = unaryOperator;
            this.binaryOperator = binaryOperator;
        }

        public boolean isNone() {
            return this == NONE;
        }

        public boolean isVariable() {
            return this == VAR;
        }

        public boolean isUnaryOperator() {
            return unaryOperator;
        }

        public boolean isBinaryOperator() {
            return binaryOperator;
        }

        public static long size() {
            return values().length;
        }

        @Override
        public String toString() {
            return representation;
        }
    }
}
