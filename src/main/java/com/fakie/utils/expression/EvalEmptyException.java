package com.fakie.utils.expression;

import com.fakie.utils.exceptions.FakieRuntimeException;

public class EvalEmptyException extends FakieRuntimeException {
    public EvalEmptyException() {
    }

    public EvalEmptyException(String message) {
        super(message);
    }

    public EvalEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public EvalEmptyException(Throwable cause) {
        super(cause);
    }

    public EvalEmptyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}