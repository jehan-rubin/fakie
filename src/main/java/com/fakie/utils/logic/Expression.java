package com.fakie.utils.logic;

import java.util.Objects;

public class Expression {
    private final String attribute;
    private final boolean value;

    public Expression(String attribute, boolean value) {
        this.attribute = attribute;
        this.value = value;
    }

    public String getAttribute() {
        return attribute;
    }

    public boolean getValue() {
        return value;
    }

    public Expression negation() {
        return new Expression(attribute, !value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Expression that = (Expression) o;
        return value == that.value && Objects.equals(attribute, that.attribute);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attribute, value);
    }

    @Override
    public String toString() {
        return attribute + "=" + value;
    }
}
