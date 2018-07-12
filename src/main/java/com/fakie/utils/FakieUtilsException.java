package com.fakie.utils;

import com.fakie.utils.exceptions.FakieException;

public class FakieUtilsException extends FakieException {
    public FakieUtilsException() {
    }

    public FakieUtilsException(String message) {
        super(message);
    }

    public FakieUtilsException(String message, Throwable cause) {
        super(message, cause);
    }

    public FakieUtilsException(Throwable cause) {
        super(cause);
    }

    public FakieUtilsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
