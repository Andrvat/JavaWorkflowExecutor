package ru.nsu.andrvat.runningBlocks;

import lombok.Builder;
import ru.nsu.andrvat.exceptions.BlockArgumentsNumberException;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@Builder
public class ArgumentsChecker implements Checkable {
    private final Integer requiredArgumentsNumber;
    private final Logger logger;

    @Override
    public void checkArgs(ArrayList<String> arguments) throws BlockArgumentsNumberException {
        if (arguments.size() > requiredArgumentsNumber) {
            logger.log(Level.SEVERE, "Too much arguments in arguments " + arguments);
            throw new BlockArgumentsNumberException();
        }
    }
}
