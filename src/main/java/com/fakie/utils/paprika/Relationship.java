package com.fakie.utils.paprika;

public enum Relationship {
    CALLS("CALLS"),
    EXTENDS("EXTENDS"),
    IMPLEMENTS("IMPLEMENTS"),
    CLASS_OWNS_METHOD("CLASS_OWNS_METHOD");

    private final String name;

    Relationship(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
