package ru.nsu.andrvat.executors;

import ru.nsu.andrvat.exceptions.BlocksFactoryException;

import java.io.InputStream;

public interface ParameterizedRunnable {
    void parametrizedRun(InputStream sourceInputStream);
}
