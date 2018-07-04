package com.fakie.model.processor;

public enum Keyword {
    SEPARATOR("_"),
    LABEL,
    CODE_SMELL,
    IS("%s_IS_%s");

    private final String name;

    Keyword() {
        this.name = name();
    }

    Keyword(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
