package com.fakie.utils;

public enum Keyword {
    SEPARATOR(" "),
    LABEL,
    CODE_SMELL,
    EQUAL("%s == %s"),
    LOW,
    HIGH,
    SPLIT("%s %s"),
    ARRAY("%s[%s]"),
    BELOW("%s < %s"),
    ABOVE("%s > %s"),
    OUTPUT_EDGE(" --%s--> %s"),
    INPUT_EDGE(" <--%s-- %s");

    private final String name;

    Keyword() {
        this.name = name();
    }

    Keyword(String name) {
        this.name = name;
    }

    public String format(Object... objects) {
        return String.format(toString(), objects);
    }

    @Override
    public String toString() {
        return name;
    }
}
