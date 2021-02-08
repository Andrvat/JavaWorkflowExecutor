package ru.nsu.andrvat.runningBlocks;

import ru.nsu.andrvat.argsParsers.CommandLineArgsParser;
import ru.nsu.andrvat.executors.ExecutionContext;
import ru.nsu.andrvat.loggersFeatures.LoggersHelper;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Replace implements Executable {
    private static final Logger logger = LoggersHelper.getLoggerInstance(CommandLineArgsParser.class.getName());
    private static final Integer requiredArgumentsNumber = 2;

    @Override
    public void execute(Integer id, ExecutionContext context) throws RuntimeException {
        ArrayList<String> blockArguments = context.getBlockArgumentsById(id);
        ArrayList<String> textForReplacing = new ArrayList<>();
        ArgumentsChecker checker = ArgumentsChecker.builder()
                .requiredArgumentsNumber(requiredArgumentsNumber)
                .logger(logger)
                .build();
        checker.checkArgs(blockArguments);
        checker.checkTextForNull(context.getOperatingText());
        String wordToReplaceIt = getWordToReplaceIt(blockArguments);
        String substitutingWord = getSubstitutingWord(blockArguments);
        for (String currentTextLine : context.getOperatingText()) {
            textForReplacing.add(currentTextLine.replace(wordToReplaceIt, substitutingWord));
        }
        context.setOperatingText(textForReplacing);
        logger.info("The word [" + wordToReplaceIt + "] " +
                "was successfully replaced with the word [" + substitutingWord + "] in the entire source text");
    }

    private String getWordToReplaceIt(ArrayList<String> arguments) throws RuntimeException {
        try {
            return arguments.get(0);
        } catch (IndexOutOfBoundsException exception) {
            logger.log(Level.SEVERE, "There is no word to replace it in arguments", exception);
            throw new RuntimeException();
        }
    }

    private String getSubstitutingWord(ArrayList<String> arguments) throws RuntimeException {
        try {
            return arguments.get(1);
        } catch (IndexOutOfBoundsException exception) {
            logger.log(Level.SEVERE, "There is no substituting word int arguments", exception);
            throw new RuntimeException();
        }
    }
}
