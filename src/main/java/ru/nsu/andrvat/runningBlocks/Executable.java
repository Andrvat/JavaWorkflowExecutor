package ru.nsu.andrvat.runningBlocks;

import ru.nsu.andrvat.executors.ExecutionContext;

public interface Executable {
    void execute(Integer id, ExecutionContext context) throws RuntimeException;
}
