package ru.nsu.andrvat.executors;

import ru.nsu.andrvat.appRunner.ApplicationRunner;
import ru.nsu.andrvat.exceptions.BlockArgumentsNumberException;
import ru.nsu.andrvat.exceptions.BlocksFactoryException;
import ru.nsu.andrvat.exceptions.MismatchTypesException;
import ru.nsu.andrvat.runningBlocks.BlocksInOutTypes;
import ru.nsu.andrvat.runningBlocks.ExecutableBlock;
import ru.nsu.andrvat.workflowConfigs.WorkflowConfigsScanner;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkflowExecutor implements ParameterizedRunnable {
    private static final Logger logger = Logger.getLogger(WorkflowExecutor.class.getName());

    private ExecutionContext context;

    @Override
    public void parametrizedRun(InputStream sourceInputStream) {
        WorkflowConfigsScanner configsScanner = new WorkflowConfigsScanner();
        configsScanner.scanConfig(sourceInputStream);
        configsScanner.analyzeConfig();

        context = ExecutionContext.builder()
                .blockNames(configsScanner.getBlockNames())
                .blockArguments(configsScanner.getBlockArguments())
                .operatingText(new ArrayList<>())
                .build();

        try {
            Queue<Integer> executorsQueue = configsScanner.getExecutorsQueue();
            executeBlocksCallsSequence(executorsQueue);
        } catch (IOException exception) {
            logger.log(Level.SEVERE, "Couldn't read file executingCommandsConfigs.properties. " +
                    "Parametrized run stopped", exception);
            throw new RuntimeException();
        } catch (BlocksFactoryException exception) {
            logger.log(Level.SEVERE, "Couldn't create a block by blocks factory. " +
                    "Parametrized run stopped", exception);
            throw new RuntimeException();
        }
    }

    private void executeBlocksCallsSequence(Queue<Integer> executorsQueue) throws IOException, BlocksFactoryException {
        BlocksInOutTypes previousBlockType = BlocksInOutTypes.OutOnly;
        while (!executorsQueue.isEmpty()) {
            Integer blockId = executorsQueue.remove();
            ExecutableBlock executableBlock = BlocksBuilder.getInstance().buildBlock(context.getBlockNameById(blockId));
            BlocksInOutTypes currentExecutableBlockType = executableBlock.getBlockType();
            try {
                checkCurrentBlockMatchThatPreviousOne(previousBlockType, currentExecutableBlockType);
                ArrayList<String> blockArguments = context.getBlockArgumentsById(blockId);
                executableBlock.execute(blockArguments, context);
                previousBlockType = currentExecutableBlockType;
            } catch (MismatchTypesException exception) {
                logger.log(Level.SEVERE, "Mismatch types. Check InOnly- and OutOnly's operation places.",
                        exception);
                return;
            } catch (BlockArgumentsNumberException exception) {
                logger.log(Level.SEVERE,
                        "Invalid arguments number to perform an operation.",
                        exception);
                return;
            } catch (IOException exception) {
                logger.log(Level.SEVERE, "Couldn't read or write data. Check source and destination files.", exception);
                return;
            }
        }
    }

    private void checkCurrentBlockMatchThatPreviousOne(BlocksInOutTypes previous, BlocksInOutTypes current) throws MismatchTypesException {
        if (previous.equals(BlocksInOutTypes.InOnly) &&
                (current.equals(BlocksInOutTypes.InOutAvailable) || current.equals(BlocksInOutTypes.OutOnly))) {
            throw new MismatchTypesException();
        }
    }

    public ArrayList<String> getContextOperatingText() {
        return context.getOperatingText();
    }

    public static InputStream getSourceInputStreamForReadingConfigsFromFile(String filename) {
        return WorkflowExecutor.class.getResourceAsStream(filename);
    }
}
