package com.fakie.utils.expression;

import java.util.Objects;

public interface Law {
    Expression apply(Expression expression);

    static Expression identity(Expression expression) {
        return expression;
    }

    static Expression none(Expression expression) {
        if (expression.getType().isUnaryOperator()) {
            UnaryOperator op = expression.cast(UnaryOperator.class);
            Expression exp = op.getExpression().simplify();
            return exp.getType().isNone() ? exp : op.newInstance(exp);
        } else if (expression.getType().isBinaryOperator()) {
            BinaryOperator op = expression.cast(BinaryOperator.class);
            Expression left = op.getLeft().simplify();
            Expression right = op.getRight().simplify();
            if (left.getType().isNone()) {
                return right;
            } else if (right.getType().isNone()) {
                return left;
            }
            return op.newInstance(left, right);
        }
        return expression;
    }

    static Expression commutativity(Expression expression) {
        if (expression.getType().isBinaryOperator()) {
            BinaryOperator op = expression.cast(BinaryOperator.class);
            Expression left = op.getLeft();
            Expression right = op.getRight();
            if (left.compareTo(right) > 0) {
                return op.newInstance(right, left).simplify();
            }
        }
        return expression;
    }

    static Expression associativity(Expression expression) {
        if (expression.getType().isBinaryOperator()) {
            BinaryOperator op = expression.cast(BinaryOperator.class);
            Expression left = op.getLeft();
            Expression right = op.getRight();
            if (left.getType() == expression.getType()) {
                BinaryOperator leftOp = left.cast(BinaryOperator.class);
                if (leftOp.getLeft().compareTo(leftOp.getRight()) < 0 && leftOp.getLeft().compareTo(right) < 0)
                    return op.newInstance(leftOp.getLeft(), leftOp.newInstance(leftOp.getRight(), right)).simplify();
            }
            if (right.getType() == expression.getType()) {
                BinaryOperator rightOp = right.cast(BinaryOperator.class);
                if (left.compareTo(rightOp.getLeft()) > 0 || left.compareTo(rightOp.getRight()) > 0)
                    return op.newInstance(rightOp.newInstance(left, rightOp.getLeft()), rightOp.getRight()).simplify();
            }
        }
        return expression;
    }

    class Annihilation implements Law {
        private final Expression annihilator;
        private final Expression result;

        Annihilation(Expression annihilator, Expression result) {
            this.annihilator = annihilator;
            this.result = result;
        }

        @Override
        public Expression apply(Expression expression) {
            if (expression.getType().isBinaryOperator()) {
                BinaryOperator op = expression.cast(BinaryOperator.class);
                Expression left = op.getLeft();
                Expression right = op.getRight();
                if (left.equals(annihilator) || right.equals(annihilator)) {
                    return result;
                }
            }
            return expression;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Annihilation that = (Annihilation) o;
            return Objects.equals(annihilator, that.annihilator) &&
                    Objects.equals(result, that.result);
        }

        @Override
        public int hashCode() {
            return Objects.hash(annihilator, result);
        }
    }

    class Idempotent implements Law {
        private final Expression neutral;

        Idempotent(Expression neutral) {
            this.neutral = neutral;
        }

        @Override
        public Expression apply(Expression expression) {
            if (expression.getType().isBinaryOperator()) {
                BinaryOperator op = expression.cast(BinaryOperator.class);
                Expression left = op.getLeft();
                Expression right = op.getRight();
                if (left.equals(neutral)) {
                    return right;
                } else if (right.equals(neutral)) {
                    return left;
                }
            }
            return expression;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Idempotent that = (Idempotent) o;
            return Objects.equals(neutral, that.neutral);
        }

        @Override
        public int hashCode() {
            return Objects.hash(neutral);
        }
    }

    class Collapse implements Law {
        private final Compactor compactor;

        Collapse(Compactor compactor) {
            this.compactor = compactor;
        }

        @Override
        public Expression apply(Expression expression) {
            if (expression.getType().isBinaryOperator()) {
                BinaryOperator op = expression.cast(BinaryOperator.class);
                Expression left = op.getLeft();
                Expression right = op.getRight();
                expression = compactor.compact(left, right, expression);
                expression = compactor.compact(right, left, expression);
            }
            return expression;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Collapse collapse = (Collapse) o;
            return Objects.equals(compactor, collapse.compactor);
        }

        @Override
        public int hashCode() {

            return Objects.hash(compactor);
        }

        public interface Compactor {
            Expression compact(Expression left, Expression right, Expression byDefault);
        }
    }
}

