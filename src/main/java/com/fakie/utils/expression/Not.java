package com.fakie.utils.expression;

import java.util.EnumMap;
import java.util.Map;

public class Not extends UnaryOperator {
    private static final Law[] laws = {
            Law::none,
            Not::simplify
    };

    private static final Map<Type, Mapper> mapping = new EnumMap<>(Type.class);

    static {
        mapping.put(Type.NONE, UnaryOperator::getExpression);
        mapping.put(Type.IS_TRUE, Not::mapIsTrue);
        mapping.put(Type.NOT, Not::mapNot);
        mapping.put(Type.EQ, Not::mapEquals);
        mapping.put(Type.NEQ, Not::mapNotEquals);
        mapping.put(Type.VAR, Not::mapVar);
        mapping.put(Type.AND, Not::mapAnd);
        mapping.put(Type.OR, Not::mapOr);
    }

    Not(Expression expression) {
        super(Type.NOT, expression, laws);
    }

    @Override
    public Boolean eval() {
        return !getExpression().isTrue().eval();
    }

    @Override
    public Not newInstance(Expression expression) {
        return new Not(expression);
    }

    private static Expression simplify(Expression expression) {
        if (expression.getType() == Type.NOT) {
            Not not = expression.cast(Not.class);
            return mapping.getOrDefault(not.getExpression().getType(), Law::identity).map(not);
        }
        return expression;
    }

    private interface Mapper {
        Expression map(Not not);
    }

    private static Expression mapVar(Not not) {
        Expression expression = not.getExpression();
        if (expression.eq(true).eval()) {
            return Expression.of(false);
        } else if (expression.eq(false).eval()) {
            return Expression.of(true);
        } else if (expression.eq(0).eval()) {
            return Expression.of(1);
        } else if (expression.gt(0).or(expression.lt(0)).eval()) {
            return Expression.of(0);
        }
        return not;
    }

    private static Expression mapIsTrue(Not not) {
        IsTrue isTrue = not.getExpression().cast(IsTrue.class);
        return isTrue.getExpression().not().simplify();
    }

    private static Expression mapNot(Not not) {
        Not n = not.getExpression().cast(Not.class);
        return n.getExpression().isTrue().simplify();
    }

    private static Expression mapEquals(Not not) {
        Equals equals = not.getExpression().cast(Equals.class);
        return equals.getLeft().neq(equals.getRight()).simplify();
    }

    private static Expression mapNotEquals(Not not) {
        NotEquals notEquals = not.getExpression().cast(NotEquals.class);
        return notEquals.getLeft().eq(notEquals.getRight()).simplify();
    }

    private static Expression mapAnd(Not not) {
        And and = not.getExpression().cast(And.class);
        return and.getLeft().not().or(and.getRight().not()).simplify();
    }

    private static Expression mapOr(Not not) {
        Or or = not.getExpression().cast(Or.class);
        return or.getLeft().not().and(or.getRight().not()).simplify();
    }
}
