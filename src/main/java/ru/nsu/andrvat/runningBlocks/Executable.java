package ru.nsu.andrvat.runningBlocks;

import ru.nsu.andrvat.exceptions.BlockArgumentsNumberException;
import ru.nsu.andrvat.executors.ExecutionContext;

import java.io.IOException;

public interface Executable {
    void execute(Integer id, ExecutionContext context) throws BlockArgumentsNumberException, IOException;
}
