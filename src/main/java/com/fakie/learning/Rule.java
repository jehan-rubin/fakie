package com.fakie.learning;

import com.fakie.utils.expression.Expression;
import com.fakie.utils.expression.Implication;

import java.util.Objects;

public class Rule {
    private final double support;
    private final double confidence;
    private final Implication implication;

    public Rule(Implication implication, double support, double confidence) {
        this.implication = implication;
        this.support = support;
        this.confidence = confidence;
    }

    public Rule contrapositive() {
        return new Rule(implication.contrapositive(), support, confidence);
    }

    public Expression premises() {
        return implication.getLeft();
    }

    public Expression consequences() {
        return implication.getRight();
    }

    public double getSupport() {
        return support;
    }

    public double getConfidence() {
        return confidence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Rule rule = (Rule) o;
        return Double.compare(rule.support, support) == 0 &&
                Double.compare(rule.confidence, confidence) == 0 &&
                Objects.equals(implication, rule.implication);
    }

    @Override
    public int hashCode() {
        return Objects.hash(support, confidence, implication);
    }

    @Override
    public String toString() {
        return "Rule{rule=" + implication + ", support=" + support + ", confidence=" + confidence + '}';
    }
}
