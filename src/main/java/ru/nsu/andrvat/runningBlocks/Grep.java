package ru.nsu.andrvat.runningBlocks;

import ru.nsu.andrvat.argsParsers.CommandLineArgsParser;
import ru.nsu.andrvat.executors.ExecutionContext;
import ru.nsu.andrvat.loggersFeatures.LoggersHelper;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Grep implements Executable {
    private static final Logger logger = LoggersHelper.getLoggerInstance(CommandLineArgsParser.class.getName());
    private static final Integer requiredArgumentsNumber = 1;

    @Override
    public void execute(Integer id, ExecutionContext context) throws RuntimeException {
        ArrayList<String> blockArguments = context.getBlockArgumentsById(id);
        ArrayList<String> textForGrep = context.getOperatingText();
        ArgumentsChecker checker = ArgumentsChecker.builder()
                .requiredArgumentsNumber(requiredArgumentsNumber)
                .logger(logger)
                .build();
        checker.checkArgs(blockArguments);
        checker.checkTextForNull(context.getOperatingText());
        String searchWord = getWordForGrep(blockArguments);
        textForGrep.removeIf(currentTextLine -> !currentTextLine.contains(searchWord));
        context.setOperatingText(textForGrep);
        logger.info("Selection of context text lines containing the word [" + searchWord + "] was successful done");
    }

    private String getWordForGrep(ArrayList<String> arguments) throws RuntimeException {
        try {
            return arguments.get(0);
        } catch (IndexOutOfBoundsException exception) {
            logger.log(Level.SEVERE, "Not enough arguments to select lines with the word", exception);
            throw new RuntimeException();
        }
    }
}
