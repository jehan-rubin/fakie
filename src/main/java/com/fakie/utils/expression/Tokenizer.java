package com.fakie.utils.expression;

import com.fakie.utils.FakieUtils;

import java.math.BigInteger;
import java.util.EnumMap;
import java.util.Map;

public class Tokenizer {
    private final Map<Expression.Type, Generator> generators = new EnumMap<>(Expression.Type.class);

    public Tokenizer() {
        setup();
    }

    private void setup() {
        generators.put(Expression.Type.VAR, this::var);
        generators.put(Expression.Type.IS_TRUE, this::isTrue);
        generators.put(Expression.Type.NOT, this::not);
        generators.put(Expression.Type.AND, this::and);
        generators.put(Expression.Type.OR, this::or);
        generators.put(Expression.Type.EQ, this::eq);
        generators.put(Expression.Type.NEQ, this::neq);
        generators.put(Expression.Type.GT, this::gt);
        generators.put(Expression.Type.LT, this::lt);
        generators.put(Expression.Type.IMPLY, this::imply);
    }

    public Expression tokenize(BigInteger id) {
        if (id.equals(BigInteger.ZERO)) {
            return Expression.empty();
        }
        BigInteger fid = id.subtract(BigInteger.ONE);
        BigInteger[] mod = fid.divideAndRemainder(AbstractExpression.TYPE_SIZE);
        Expression.Type type = Expression.Type.values()[mod[1].intValue() + 1];
        return generators.get(type).generate(mod[0]);
    }

    private Expression var(BigInteger id) {
        if (id.equals(BigInteger.ZERO)) {
            return Expression.of(false);
        } else if (id.equals(BigInteger.ONE)) {
            return Expression.of(true);
        }
        Variable variable = Variable.byId(id);
        if (variable != null) {
            return variable;
        }
        return Variable.newVariable();
    }

    private Expression isTrue(BigInteger id) {
        return tokenize(id).isTrue();
    }

    private Expression not(BigInteger id) {
        return tokenize(id).not();
    }

    private Expression and(BigInteger id) {
        FakieUtils.Pair pair = FakieUtils.unPair(id);
        return tokenize(pair.getKey()).and(tokenize(pair.getValue()));
    }

    private Expression or(BigInteger id) {
        FakieUtils.Pair pair = FakieUtils.unPair(id);
        return tokenize(pair.getKey()).or(tokenize(pair.getValue()));
    }

    private Expression eq(BigInteger id) {
        FakieUtils.Pair pair = FakieUtils.unPair(id);
        return tokenize(pair.getKey()).eq(tokenize(pair.getValue()));
    }

    private Expression neq(BigInteger id) {
        FakieUtils.Pair pair = FakieUtils.unPair(id);
        return tokenize(pair.getKey()).neq(tokenize(pair.getValue()));
    }

    private Expression gt(BigInteger id) {
        FakieUtils.Pair pair = FakieUtils.unPair(id);
        return tokenize(pair.getKey()).gt(tokenize(pair.getValue()));
    }

    private Expression lt(BigInteger id) {
        FakieUtils.Pair pair = FakieUtils.unPair(id);
        return tokenize(pair.getKey()).lt(tokenize(pair.getValue()));
    }

    private Expression imply(BigInteger id) {
        FakieUtils.Pair pair = FakieUtils.unPair(id);
        return tokenize(pair.getKey()).imply(tokenize(pair.getValue()));
    }

    private interface Generator {
        Expression generate(BigInteger id);
    }
}
