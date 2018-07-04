package com.fakie.model;

import com.fakie.utils.exceptions.FakieException;

public class FakieModelException extends FakieException {
    public FakieModelException() {
    }

    public FakieModelException(String message) {
        super(message);
    }

    public FakieModelException(String message, Throwable cause) {
        super(message, cause);
    }

    public FakieModelException(Throwable cause) {
        super(cause);
    }

    public FakieModelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
