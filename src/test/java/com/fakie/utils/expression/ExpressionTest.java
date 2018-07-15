package com.fakie.utils.expression;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExpressionTest {
    @Test
    public void expr1() {
        // (((50 > 43) == true) && (name == name))
        Expression exp = Expression.of(50).gt(43).eq(true).and(Expression.of("name").eq("name"))
                .simplify();
        assertEquals(true, exp.eval());
    }

    @Test
    public void expr2() {
        // (((10 > 43) == true) && (name == name))
        Expression exp = Expression.of(10).gt(43).eq(true).and(Expression.of("name").eq("name"))
                .simplify();
        assertEquals(false, exp.eval());
    }

    @Test
    public void expr3() {
        // (((50 > 43) == true) && (name == hello))
        Expression exp = Expression.of(50).gt(43).eq(true).and(Expression.of("name").eq("hello"))
                .simplify();
        assertEquals(false, exp.eval());
    }

    @Test
    public void expr4() {
        // (((50 > 43) == true) || (name == hello))
        Expression exp = Expression.of(50).gt(43).eq(true).or(Expression.of("name").eq("hello"))
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

    @Test
    public void equality() {
        Expression exp1 = Expression.of(true).or("Hello");
        Expression exp2 = Expression.of(true).or("Hello");
        assertEquals(exp1, exp2);
    }

    @Test
    public void annihilation1() {
        Expression exp = Expression.of(true).or("Hello").simplify();
        assertEquals(Expression.of(true), exp);
    }

    @Test
    public void annihilation2() {
        Expression exp = Expression.of(false).and("Hello").simplify();
        assertEquals(Expression.of(false), exp);
    }

    @Test
    public void contrapositive1() {
        Implication exp = Expression.of("number_of_methods > 50").eq(true)
                .and(Expression.of("lack_of_cohesion_in_methods > 40").eq(true))
                .and(Expression.of("label 0 == Class").eq(true))
                .imply(Expression.of("Code smell = BLOB").eq(true));
        Implication contrapositive = exp.contrapositive();
        Expression expected = exp.simplify();
        Expression actual = contrapositive.simplify();
        assertEquals(expected, actual);
    }

    @Test
    public void contrapositive2() {
        Implication exp = Expression.of("number_of_methods > 50").eq(true)
                .and(Expression.of("lack_of_cohesion_in_methods > 40").eq(true))
                .and(Expression.of("label 0 == Class").eq(true))
                .and(Expression.of("name").eq("Activity").or(Expression.of("name").eq("Main")))
                .imply(Expression.of("Code smell = BLOB").eq(true));
        Implication contrapositive = exp.contrapositive();
        Expression expected = exp.simplify();
        Expression actual = contrapositive.simplify();

        assertEquals(expected, actual);
    }

    @Test
    public void commutativity1() {
        Expression exp = Expression.of("x").eq(1).and(Expression.of("y").eq(2)).simplify();
        Expression act = Expression.of("y").eq(2).and(Expression.of("x").eq(1)).simplify();
        assertEquals(exp, act);
    }

    @Test
    public void commutativity2() {
        Expression exp = Expression.of("x").eq(1).or(Expression.of("y").eq(2)).simplify();
        Expression act = Expression.of("y").eq(2).or(Expression.of("x").eq(1)).simplify();
        assertEquals(exp, act);
    }

    @Test
    public void associativity1() {
        // ((x == 1) ∨ ((y == 2) ∨ (z == 3)))
        Expression exp = Expression.of("x").eq(1).or(Expression.of("y").eq(2).or(Expression.of("z").eq(3))).simplify();
        // (((x == 1) ∨ (y == 2)) ∨ (z == 3))
        Expression act = Expression.of("x").eq(1).or(Expression.of("y").eq(2)).or(Expression.of("z").eq(3)).simplify();
        assertEquals(exp, act);
    }

    @Test
    public void associativity2() {
        Expression exp = Expression.of("x").eq(1).and(Expression.of("y").eq(2).and(Expression.of("z").eq(3)))
                .simplify();
        Expression act = Expression.of("x").eq(1).and(Expression.of("y").eq(2)).and(Expression.of("z").eq(3))
                .simplify();
        assertEquals(exp, act);
    }

    @Test
    public void associativity3() {
        Expression exp = Expression.of("x").eq(1).and(
                Expression.of("y").eq(2).and(
                        Expression.of("z").eq(3)
                                .and(Expression.of("w").eq(4))))
                .simplify();
        Expression act = Expression.of("x").eq(1).and(
                Expression.of("y").eq(2))
                .and(Expression.of("z").eq(3))
                .and(Expression.of("w").eq(4))
                .simplify();
        assertEquals(exp, act);
    }

    @Test
    public void associativity4() {
        Expression exp = Expression.of("x").eq(1).and(Expression.of("y").eq(2).and(Expression.of("z").eq(3)))
                .and(Expression.of("a").eq(4).and(Expression.of("b").eq(5)).and(Expression.of("c").eq(6))).simplify();
        Expression act = Expression.of("x").eq(1).and(Expression.of("y").eq(2)).and(Expression.of("z").eq(3))
                .and(Expression.of("a").eq(4)).and(Expression.of("b").eq(5)).and(Expression.of("c").eq(6)).simplify();
        assertEquals(exp, act);
    }

    @Test
    public void associativity5() {
        Expression exp = Expression.of("x").eq(1).and(
                Expression.of("y").eq(2).and(
                        Expression.of("z").eq(3)
                                .and(Expression.of("w").eq(4)
                                        .and(Expression.of("u").eq(5)
                                                .and(Expression.of("v").eq(6))))))
                .simplify();
        Expression act = Expression.of("x").eq(1).and(
                Expression.of("y").eq(2))
                .and(Expression.of("z").eq(3))
                .and(Expression.of("w").eq(4))
                .and(Expression.of("u").eq(5))
                .and(Expression.of("v").eq(6))
                .simplify();
        assertEquals(exp, act);
    }

    @Test
    public void associativity6() {
        Expression exp = Expression.of("x").eq(1).and(
                Expression.of("y").eq(2).and(
                        Expression.of("z").eq(3)))
                .and(Expression.of("w").eq(4)
                        .and(Expression.of("u").eq(5)
                                .and(Expression.of("v").eq(6))))
                .simplify();
        Expression act = Expression.of("v").eq(6).and(
                Expression.of("y").eq(2)
                        .and(Expression.of("u").eq(5)))
                .and(Expression.of("z").eq(3)
                        .and(Expression.of("w").eq(4)))
                .and(Expression.of("x").eq(1))
                .simplify();
        assertEquals(exp, act);
    }

    @Test
    public void contrapositive3() {
        Expression exp = Expression.of("x").eq(1).and(
                Expression.of("y").eq(2).and(
                        Expression.of("z").eq(3)))
                .and(Expression.of("w").eq(4)
                        .and(Expression.of("u").eq(5)
                                .and(Expression.of("v").eq(6))));
        Expression act = Expression.of("v").eq(6).and(
                Expression.of("y").eq(2)
                        .and(Expression.of("u").eq(5)))
                .and(Expression.of("z").eq(3)
                        .and(Expression.of("w").eq(4)))
                .and(Expression.of("x").eq(1));
        Implication imply = exp.imply(act);
        assertEquals(imply.simplify(), imply.contrapositive().simplify());
    }

    @Test
    public void idempotent1() {
        Expression exp = Expression.of(true).and(Expression.of("y").eq(2)).simplify();
        Expression act = Expression.of("y").eq(2).simplify();
        assertEquals(exp, act);
    }
}