package com.fakie.utils.expression;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExpressionTest {
    @Test
    public void expr1() {
        // (((50 > 43) == true) && (name == name))
        Expression exp = Expression.empty()
                .and(Expression.of(50).gt(43)).eq(true).and(Expression.of("name").eq("name"))
                .simplify();
        assertEquals(true, exp.eval());
    }

    @Test
    public void expr2() {
        // (((10 > 43) == true) && (name == name))
        Expression exp = Expression.empty()
                .and(Expression.of(10).gt(43)).eq(true).and(Expression.of("name").eq("name"))
                .simplify();
        assertEquals(false, exp.eval());
    }

    @Test
    public void expr3() {
        // (((50 > 43) == true) && (name == hello))
        Expression exp = Expression.empty()
                .and(Expression.of(10).gt(43)).eq(true).and(Expression.of("name").eq("hello"))
                .simplify();
        assertEquals(false, exp.eval());
    }

    @Test
    public void expr4() {
        // (((50 > 43) == true) || (name == hello))
        Expression exp = Expression.empty()
                .and(Expression.of(50).gt(43)).eq(true).or(Expression.of("name").eq("hello"))
                .simplify();
        assertEquals(true, exp.eval());
    }

    @Test
    public void expr5() {
        // (((!(50 > 43)) == false) || (name == 50))
        Expression exp = Expression.of(50).gt(43).not().eq(false).or(Expression.of("name").eq(50));
        assertEquals(true, exp.eval());
    }

    @Test
    public void expr6() {
        // (((50 > 43) != false) || (name == 50))
        Expression exp = Expression.of(50).gt(43).neq(false).or(Expression.of("name").eq(50));
        assertEquals(true, exp.eval());
    }

    @Test
    public void expr7() {
        // ((1 > 2) || 1)
        Expression exp = Expression.of(1).gt(2).or(1);
        assertEquals(true, exp.eval());
    }

    @Test
    public void expr8() {
        // (false == ((1 > 2) || 1))
        Expression exp = Expression.of(false).eq(Expression.of(1).gt(2).or(1));
        assertEquals(false, exp.eval());
        // (((1 > 2) || 1) == false)
        exp = Expression.of(1).gt(2).or(1).eq(false);
        assertEquals(false, exp.eval());
    }
}