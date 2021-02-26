package ru.nsu.andrvat.runningBlocks;

import ru.nsu.andrvat.exceptions.BlockArgumentsNumberException;
import ru.nsu.andrvat.executors.ExecutionContext;

import java.io.IOException;
import java.util.ArrayList;

public interface Executable {
    void execute(ArrayList<String> blockArguments, ExecutionContext context) throws BlockArgumentsNumberException, IOException;
}
