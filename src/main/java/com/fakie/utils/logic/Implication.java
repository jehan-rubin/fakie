package com.fakie.utils.logic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

public class Implication {
    private final Operator left;
    private final Operator right;

    public Implication(Operator left, Operator right) {
        this.left = left;
        this.right = right;
    }

    public Implication contrapositive() {
        return new Implication(right.negation(), left.negation());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Implication that = (Implication) o;
        Implication contra = that.contrapositive();
        boolean thatEquality = Objects.equals(left, that.left) && Objects.equals(right, that.right);
        boolean contraEquality = Objects.equals(left, contra.left) && Objects.equals(right, contra.right);
        return thatEquality || contraEquality;
    }

    @Override
    public int hashCode() {
        Implication contra = contrapositive();
        return new HashSet<>(Arrays.asList(left, right, contra.left, contra.right)).hashCode();
    }

    @Override
    public String toString() {
        return "(" + left + " => " + right + ")";
    }
}
