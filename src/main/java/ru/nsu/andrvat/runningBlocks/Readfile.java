package ru.nsu.andrvat.runningBlocks;

import ru.nsu.andrvat.argsParsers.CommandLineArgsParser;
import ru.nsu.andrvat.executors.ExecutionContext;
import ru.nsu.andrvat.loggersFeatures.LoggersHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Readfile implements Executable {
    private static final Logger logger = LoggersHelper.getLoggerInstance(CommandLineArgsParser.class.getName());
    private static final Integer requiredArgumentsNumber = 1;

    @Override
    public void execute(Integer id, ExecutionContext context) throws RuntimeException {
        ArrayList<String> blockArguments = context.getBlockArgumentsById(id);
        ArgumentsChecker checker = ArgumentsChecker.builder()
                .requiredArgumentsNumber(requiredArgumentsNumber)
                .logger(logger)
                .build();
        checker.checkArgs(blockArguments);
        checker.checkTextForNull(context.getOperatingText());
        String sourceFilename = getSourceFilenameForReadingText(blockArguments);
        InputStream inputStream = Readfile.class.getResourceAsStream(sourceFilename);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            ArrayList<String> fromFileReadText = readAllDataByBufferedReader(reader);
            context.setOperatingText(fromFileReadText);
        } catch (IOException | NullPointerException exception) {
            logger.log(Level.SEVERE, "Couldn't read data from " + sourceFilename, exception);
            throw new RuntimeException();
        }
        logger.info("All data from source file were successfully scanned");
        logger.info("Source data filename: " + sourceFilename);
    }

    private String getSourceFilenameForReadingText(ArrayList<String> arguments) throws RuntimeException {
        try {
            return arguments.get(0);
        } catch (IndexOutOfBoundsException exception) {
            logger.log(Level.SEVERE, "Not enough arguments to perform a read operation from a file", exception);
            throw new RuntimeException();
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
