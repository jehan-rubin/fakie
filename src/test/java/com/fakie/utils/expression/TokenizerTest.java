package com.fakie.utils.expression;

import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class TokenizerTest {
    private Tokenizer tokenizer;

    @Before
    public void setUp() throws Exception {
        tokenizer = new Tokenizer();
    }

    @Test
    public void expr1() {
        int total = 100000;
        Set<Expression> expressions = new HashSet<>();
        for (int i = 0; i < total; i++) {
            Expression token = tokenizer.tokenize(BigInteger.valueOf(i));
            assertEquals(BigInteger.valueOf(i), token.id());
            expressions.add(token);
        }
        assertEquals(total, expressions.size());

        Implication imply = Expression.of(2).gt(3).imply(Expression.of(3).gt(4));
        assertEquals(imply, tokenizer.tokenize(imply.id()));
    }

    @Test
    public void expr2() {
        Implication imply = Expression.of(2).gt(3).imply(Expression.of(3).gt(4));
        assertEquals(imply, tokenizer.tokenize(imply.id()));
    }

    @Test
    public void expr3() {
        Implication imply = Expression.of(2).gt(3).imply(Expression.of(3).gt(4));
        Expression.of(5).and(6).and(7).and(8).and(9);
        assertEquals(imply, tokenizer.tokenize(imply.id()));
    }
}