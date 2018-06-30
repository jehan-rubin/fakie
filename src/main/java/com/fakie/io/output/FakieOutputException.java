package com.fakie.io.output;

import com.fakie.io.FakieIOException;

public class FakieOutputException extends FakieIOException {
    public FakieOutputException() {
    }

    public FakieOutputException(String message) {
        super(message);
    }

    public FakieOutputException(String message, Throwable cause) {
        super(message, cause);
    }

    public FakieOutputException(Throwable cause) {
        super(cause);
    }

    public FakieOutputException(String message, Throwable cause, boolean enableSuppression, boolean
            writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
