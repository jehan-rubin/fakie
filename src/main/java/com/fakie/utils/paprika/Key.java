package com.fakie.utils.paprika;

public enum Key {
    NAME("name"),
    FULL_NAME("full_name"),
    NUMBER_OF_INSTRUCTIONS("number_of_instructions"),
    IS_ASYNC_TASK("is_async_task");


    private final String name;

    Key(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
