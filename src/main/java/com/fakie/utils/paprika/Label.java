package com.fakie.utils.paprika;

public enum Label {
    CLASS("Class"),
    METHOD("Method");

    private final String name;

    Label(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
