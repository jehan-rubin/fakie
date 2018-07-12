package com.fakie.model.graph;

import java.util.Collection;

public enum Type {
    ARRAY(Collection.class),
    NUMBER(Number.class),
    STRING(String.class),
    BOOLEAN(Boolean.class),
    NONE(Void.class);

    private final Class<?> cls;

    Type(Class<?> cls) {
        this.cls = cls;
    }

    static Type valueOf(Object object) {
        for (Type type : values()) {
            if (type.cls.isAssignableFrom(object.getClass())) {
                return type;
            }
        }
        return NONE;
    }
}
