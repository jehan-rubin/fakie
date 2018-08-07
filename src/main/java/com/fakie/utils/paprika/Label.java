package com.fakie.utils.paprika;

public enum Label {
    NAME("name"),
    FULL_NAME("full_name"),
    CLASS("Class"),
    METHOD("Method"),
    EXTENDS("EXTENDS"),
    IMPLEMENTS("IMPLEMENTS"),
    CLASS_OWNS_METHOD("CLASS_OWNS_METHOD");

    private final String name;

    Label(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
