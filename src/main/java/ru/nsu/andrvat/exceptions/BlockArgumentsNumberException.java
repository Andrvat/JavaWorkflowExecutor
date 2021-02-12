package ru.nsu.andrvat.exceptions;

public class BlockArgumentsNumberException extends Exception {
    public BlockArgumentsNumberException(String message) {
        super(message);
    }

    public BlockArgumentsNumberException() {
        super();
    }

    public BlockArgumentsNumberException(Throwable t) {
        super(t);
    }
}
