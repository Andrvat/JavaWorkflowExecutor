package ru.nsu.andrvat.runningBlocks;

import ru.nsu.andrvat.argsParsers.CommandLineArgsParser;
import ru.nsu.andrvat.exceptions.BlockArgumentsNumberException;
import ru.nsu.andrvat.executors.ExecutionContext;
import ru.nsu.andrvat.loggersFeatures.LoggersHelper;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Replace extends ExecutableBlock {
    private static final Logger logger = LoggersHelper.getLoggerInstance(Replace.class.getName());
    private static final Integer requiredArgumentsNumber = 2;

    public Replace() {
        super(BlocksInOutTypes.InOutAvailable);
    }

    @Override
    public void execute(ArrayList<String> blockArguments, ExecutionContext context) throws BlockArgumentsNumberException {
        ArrayList<String> textForReplacing = new ArrayList<>();
        ArgumentsChecker checker = ArgumentsChecker.builder()
                .requiredArgumentsNumber(requiredArgumentsNumber)
                .logger(logger)
                .build();
        checker.checkArgs(blockArguments);
        String wordToReplaceIt = getWordToReplaceIt(blockArguments);
        String substitutingWord = getSubstitutingWord(blockArguments);
        for (String currentTextLine : context.getOperatingText()) {
            textForReplacing.add(currentTextLine.replace(wordToReplaceIt, substitutingWord));
        }
        context.setOperatingText(textForReplacing);
        logger.info("The word [" + wordToReplaceIt + "] " +
                "was successfully replaced with the word [" + substitutingWord + "] in the entire source text");
    }

    private String getWordToReplaceIt(ArrayList<String> arguments) throws BlockArgumentsNumberException {
        try {
            return arguments.get(0);
        } catch (IndexOutOfBoundsException exception) {
            logger.log(Level.SEVERE, "There is no word to replace it in arguments", exception);
            throw new BlockArgumentsNumberException();
        }
    }

    private String getSubstitutingWord(ArrayList<String> arguments) throws BlockArgumentsNumberException {
        try {
            return arguments.get(1);
        } catch (IndexOutOfBoundsException exception) {
            logger.log(Level.SEVERE, "There is no substituting word int arguments", exception);
            throw new BlockArgumentsNumberException();
        }
    }
}
