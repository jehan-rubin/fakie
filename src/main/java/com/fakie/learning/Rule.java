package com.fakie.learning;

import com.fakie.logic.Implication;

import java.util.Objects;

public class Rule {
    private final double confidence;
    private final Implication implication;

    public Rule(Implication implication, double confidence) {
        this.implication = implication;
        this.confidence = confidence;
    }

    public Implication getImplication() {
        return implication;
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
        return Double.compare(rule.confidence, confidence) == 0 && Objects.equals(implication, rule.implication);
    }

    @Override
    public int hashCode() {
        return Objects.hash(confidence, implication);
    }

    @Override
    public String toString() {
        return "Rule{rule=" + implication + ", confidence=" + confidence + '}';
    }
}
