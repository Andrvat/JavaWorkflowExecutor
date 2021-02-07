package ru.nsu.andrvat.runningBlocks;

import ru.nsu.andrvat.executor.ExecutionContext;

public interface Executesable {
    void execute(Integer id, ExecutionContext context) throws RuntimeException;
}
