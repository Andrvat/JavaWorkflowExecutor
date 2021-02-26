package ru.nsu.andrvat.exceptions;

public class BlocksFactoryException extends Exception {
    public BlocksFactoryException(String message) {
        super(message);
    }

    public BlocksFactoryException() {
        super();
    }

    public BlocksFactoryException(Throwable t) {
        super(t);
    }
}
