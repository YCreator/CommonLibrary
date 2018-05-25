package com.frame.aop.tools;

/**
 * Created by yzd on 2018/5/25 0025.
 */

public class ReflectException extends RuntimeException {

    private static final long serialVersionUID = -6654702552823551870L;

    public ReflectException(String message) {
        super(message);
    }

    public ReflectException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectException() {
        super();
    }

    public ReflectException(Throwable cause) {
        super(cause);
    }
}
