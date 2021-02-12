package ru.nsu.andrvat.workflowConfigs;

import ru.nsu.andrvat.argsParsers.CommandLineArgsParser;
import ru.nsu.andrvat.loggersFeatures.LoggersHelper;

import javax.naming.InvalidNameException;
import java.io.*;
import java.security.SignatureException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkflowConfigsScanner implements Scannable, Analyzable {
    private final LinkedHashMap<Integer, ArrayList<String>> blockArguments = new LinkedHashMap<>();
    private final Map<Integer, String> blockNames = new LinkedHashMap<>();
    private final Queue<Integer> executorsQueue = new LinkedList<>();
    private final Queue<String> configuratorContents = new LinkedList<>();

    private static final Logger logger = LoggersHelper.getLoggerInstance(WorkflowConfigsScanner.class.getName());

    @Override
    public void scanConfig(InputStream sourceInputStream) throws RuntimeException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(sourceInputStream))) {
            readAllDataByBufferedReader(reader);
        } catch (IOException | NullPointerException exception) {
            logger.log(Level.SEVERE, "Couldn't read workflow config data", exception);
            throw new RuntimeException();
        }
        logger.info("Configuration was successfully scanned");
        logger.info("Configs source input stream" + sourceInputStream);
    }

    private void readAllDataByBufferedReader(BufferedReader reader) throws IOException {
        String currentConfigLine;
        while ((currentConfigLine = reader.readLine()) != null) {
            if (!currentConfigLine.equals("")) {
                configuratorContents.add(currentConfigLine);
            }
        }
    }

    @Override
    public void analyzeConfig() throws RuntimeException {
        checkConfigForCorrectTitle();
        logger.info("desc title was found successfully");

        parseOperationalMatches();
        logger.info("All matches were parsed successfully");

        parseExecutionBlocksSequence();
        logger.info("All block ids were added successfully to executors queue");

        logger.info("Workflow configs was successfully analyzed");
    }

    private void checkConfigForCorrectTitle() throws RuntimeException {
        try {
            String configTitle = configuratorContents.remove();
            if (!configTitle.equals("desc")) {
                throw new InvalidNameException();
            }
        } catch (InvalidNameException | NoSuchElementException exception) {
            logger.log(Level.SEVERE, "Config file doesn't content desc title", exception);
            throw new RuntimeException();
        }
    }

    private void parseOperationalMatches() throws RuntimeException {
        String nextConfigLine = getNextConfigLine();
        while (!isConfigHasCorrectConclusion(nextConfigLine)) {
            ArrayList<String> parsedNextConfigLine = parseNextConfigLine(nextConfigLine);

            Integer blockId = getBlockIdFromCurrentParsedConfigLine(parsedNextConfigLine);
            logger.info("Block id [" + blockId + "] was read successfully");

            checkSecondSymbolFromCurrentParsedConfigLineForEqualSign(parsedNextConfigLine);
            logger.info("Equal sign was read successfully");

            String blockOperationName = getBlockOperationNameFromCurrentParsedConfigLine(parsedNextConfigLine);
            logger.info("Block operation name [" + blockOperationName + "] was read successfully");

            blockNames.put(blockId, blockOperationName);
            logger.info("Pair [" + blockId + ", " + blockOperationName + "] were added to properties successfully");

            blockArguments.put(blockId, new ArrayList<>(parsedNextConfigLine.subList(3, parsedNextConfigLine.size())));
            logger.info("Block id [" + blockId + "] got his arguments successfully");

            nextConfigLine = getNextConfigLine();
        }
    }

    private String getNextConfigLine() throws RuntimeException {
        String nextConfigLine;
        try {
            nextConfigLine = configuratorContents.remove();
        } catch (NoSuchElementException exception) {
            logger.log(Level.SEVERE, "Config file doesn't content all necessary data", exception);
            throw new RuntimeException();
        }
        return nextConfigLine;
    }

    private Boolean isConfigHasCorrectConclusion(String conclusion) {
        return conclusion.equals("csed");
    }

    private ArrayList<String> parseNextConfigLine(String nextConfigLine) {
        return new ArrayList<>(Arrays.asList(nextConfigLine.split(" ")));
    }

    private Integer getBlockIdFromCurrentParsedConfigLine(ArrayList<String> parsedConfigLine)
            throws RuntimeException {
        int blockId;
        try {
            blockId = Integer.parseInt(parsedConfigLine.get(0));
        } catch (NumberFormatException | IndexOutOfBoundsException exception) {
            logger.log(Level.SEVERE, "In parsed line: " + parsedConfigLine + ". "
                    + "The block id was not found", exception);
            throw new RuntimeException();
        }
        return blockId;
    }

    private void checkSecondSymbolFromCurrentParsedConfigLineForEqualSign(ArrayList<String> parsedConfigLine)
            throws RuntimeException {
        try {
            if (!parsedConfigLine.get(1).equals("=")) {
                throw new SignatureException();
            }
        } catch (SignatureException | IndexOutOfBoundsException exception) {
            logger.log(Level.SEVERE, "In parsed line: " + parsedConfigLine + ". "
                    + "The equal sign was not found", exception);
            throw new RuntimeException();
        }
    }

    private String getBlockOperationNameFromCurrentParsedConfigLine(ArrayList<String> parsedConfigLine)
            throws RuntimeException {
        String blockOperationName;
        try {
            blockOperationName = parsedConfigLine.get(2);
        } catch (IndexOutOfBoundsException exception) {
            logger.log(Level.SEVERE, "In parsed line: " + parsedConfigLine + ". "
                    + "The block operation name was not found", exception);
            throw new RuntimeException();
        }
        return blockOperationName;
    }

    private void parseExecutionBlocksSequence() throws RuntimeException {
        try {
            String executionBlocksSequence = configuratorContents.remove();
            if (!configuratorContents.isEmpty()) {
                throw new ArrayStoreException();
            }
            ArrayList<String> parsedExecutionBlocksSequence =
                    new ArrayList<>(Arrays.asList(executionBlocksSequence.split(" -> ")));
            for (String allegedBlockId : parsedExecutionBlocksSequence) {
                executorsQueue.add(Integer.parseInt(allegedBlockId));
            }
        } catch (NoSuchElementException | NumberFormatException exception) {
            logger.log(Level.SEVERE, "Config file doesn't content correct execution blocks sequence", exception);
            throw new RuntimeException();
        }
    }

    public LinkedHashMap<Integer, ArrayList<String>> getBlockArguments() {
        return blockArguments;
    }

    public Map<Integer, String> getBlockNames() {
        return blockNames;
    }

    public Queue<Integer> getExecutorsQueue() {
        return executorsQueue;
    }
}
