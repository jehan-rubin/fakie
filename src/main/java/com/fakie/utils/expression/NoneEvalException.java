package com.fakie.utils.expression;

public class NoneEvalException extends RuntimeException {
    public NoneEvalException() {
    }

    public NoneEvalException(String message) {
        super(message);
    }

    public NoneEvalException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoneEvalException(Throwable cause) {
        super(cause);
    }

    public NoneEvalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}