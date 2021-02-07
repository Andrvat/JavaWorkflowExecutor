package ru.nsu.andrvat.runningBlocks;

import lombok.Builder;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@Builder
public class ArgumentsChecker implements Checkable {
    private final Integer requiredArgumentsNumber;
    private final Logger logger;

    @Override
    public void checkArgs(ArrayList<String> arguments) throws RuntimeException {
        if (arguments.size() > requiredArgumentsNumber) {
            logger.log(Level.SEVERE, "Too much arguments in arguments " + arguments);
            throw new RuntimeException();
        }
    }

    @Override
    public void checkTextForNull(ArrayList<String> text) throws RuntimeException {
        if (text == null) {
            logger.log(Level.SEVERE, "The text is null. Check the executing sequence for writefile and readfile blocks places");
            throw new RuntimeException();
        }
    }
}
