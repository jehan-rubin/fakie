package com.fakie.learning;

import com.fakie.utils.logic.Implication;
import com.fakie.utils.logic.Operator;

import java.util.Objects;

public class Rule {
    private final int support;
    private final Implication implication;

    public Rule(Implication implication, int support) {
        this.implication = implication;
        this.support = support;
    }

    public Rule contrapositive() {
        return new Rule(implication.contrapositive(), support);
    }

    public Operator premises() {
        return implication.getLeft();
    }

    public Operator consequences() {
        return implication.getRight();
    }

    public int getSupport() {
        return support;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Rule rule = (Rule) o;
        return rule.support == support && Objects.equals(implication, rule.implication);
    }

    @Override
    public int hashCode() {
        return Objects.hash(support, implication);
    }

    @Override
    public String toString() {
        return "Rule{rule=" + implication + ", support=" + support + '}';
    }
}
