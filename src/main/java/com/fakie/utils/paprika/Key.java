package com.fakie.utils.paprika;

public enum Key {
    NAME("name"),
    FULL_NAME("full_name");

    private final String name;

    Key(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
