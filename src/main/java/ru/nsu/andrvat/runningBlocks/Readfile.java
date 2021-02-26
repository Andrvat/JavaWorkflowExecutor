package ru.nsu.andrvat.runningBlocks;

import ru.nsu.andrvat.argsParsers.CommandLineArgsParser;
import ru.nsu.andrvat.exceptions.BlockArgumentsNumberException;
import ru.nsu.andrvat.executors.ExecutionContext;
import ru.nsu.andrvat.loggersFeatures.LoggersHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Readfile extends ExecutableBlock {
    private static final Logger logger = LoggersHelper.getLoggerInstance(Readfile.class.getName());
    private static final Integer requiredArgumentsNumber = 1;

    public Readfile() {
        super(BlocksInOutTypes.OutOnly);
    }

    @Override
    public void execute(ArrayList<String> blockArguments, ExecutionContext context) throws BlockArgumentsNumberException, IOException {
        ArgumentsChecker checker = ArgumentsChecker.builder()
                .requiredArgumentsNumber(requiredArgumentsNumber)
                .logger(logger)
                .build();
        checker.checkArgs(blockArguments);
        String sourceFilename = getSourceFilenameForReadingText(blockArguments);
        InputStream inputStream = Readfile.class.getResourceAsStream(sourceFilename);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            ArrayList<String> fromFileReadText = readAllDataByBufferedReader(reader);
            context.setOperatingText(fromFileReadText);
        }
        logger.info("All data from source file were successfully scanned");
        logger.info("Source data filename: " + sourceFilename);
    }

    private String getSourceFilenameForReadingText(ArrayList<String> arguments) throws BlockArgumentsNumberException {
        try {
            return arguments.get(0);
        } catch (IndexOutOfBoundsException exception) {
            logger.log(Level.SEVERE, "Not enough arguments to perform a read operation from a file", exception);
            throw new BlockArgumentsNumberException();
        }
    }

    private ArrayList<String> readAllDataByBufferedReader(BufferedReader reader) throws IOException {
        ArrayList<String> data = new ArrayList<>();
        String currentConfigLine;
        while ((currentConfigLine = reader.readLine()) != null) {
            if (!currentConfigLine.equals("")) {
                data.add(currentConfigLine);
            }
        }
        return data;
    }
}
