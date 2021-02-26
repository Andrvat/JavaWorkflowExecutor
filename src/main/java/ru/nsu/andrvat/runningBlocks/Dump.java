package ru.nsu.andrvat.runningBlocks;

import ru.nsu.andrvat.argsParsers.CommandLineArgsParser;
import ru.nsu.andrvat.exceptions.BlockArgumentsNumberException;
import ru.nsu.andrvat.executors.ExecutionContext;
import ru.nsu.andrvat.loggersFeatures.LoggersHelper;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Dump extends ExecutableBlock {
    private static final Logger logger = LoggersHelper.getLoggerInstance(Dump.class.getName());
    private static final Integer requiredArgumentsNumber = 1;

    public Dump() {
        super(BlocksInOutTypes.InOutAvailable);
    }

    @Override
    public void execute(ArrayList<String> blockArguments, ExecutionContext context) throws BlockArgumentsNumberException, IOException {
                ArgumentsChecker checker = ArgumentsChecker.builder()
                .requiredArgumentsNumber(requiredArgumentsNumber)
                .logger(logger)
                .build();
        checker.checkArgs(blockArguments);
        String destinationFilename = getDestinationFilenameForReadingText(blockArguments);
        try (PrintWriter writer = new PrintWriter(destinationFilename, StandardCharsets.UTF_8)) {
            for (String currentTextLine : context.getOperatingText()) {
                writer.println(currentTextLine);
            }
        }
        logger.info("All data from operating text were successfully dumped to " + destinationFilename);
    }

    private String getDestinationFilenameForReadingText(ArrayList<String> arguments) throws BlockArgumentsNumberException {
        try {
            return arguments.get(0);
        } catch (IndexOutOfBoundsException exception) {
            logger.log(Level.SEVERE, "Not enough arguments to perform a dump operation to a file", exception);
            throw new BlockArgumentsNumberException();
        }
    }

}
