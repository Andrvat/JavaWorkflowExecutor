package ru.nsu.andrvat.exceptions;

public class MismatchTypesException extends Exception {
    public MismatchTypesException(String message) {
        super(message);
    }

    public MismatchTypesException() {
        super();
    }

    public MismatchTypesException(Throwable t) {
        super(t);
    }
}
