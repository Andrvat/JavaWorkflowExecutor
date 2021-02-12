package ru.nsu.andrvat.executors;

import ru.nsu.andrvat.argsParsers.CommandLineArgsParser;
import ru.nsu.andrvat.loggersFeatures.LoggersHelper;
import ru.nsu.andrvat.runningBlocks.BlocksInOutTypes;
import ru.nsu.andrvat.runningBlocks.Executable;
import ru.nsu.andrvat.runningBlocks.ExecutableBlock;
import ru.nsu.andrvat.workflowConfigs.WorkflowConfigsScanner;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkflowExecutor implements ParameterizedRunnable {
    private static final Logger logger = LoggersHelper.getLoggerInstance(WorkflowExecutor.class.getName());

    private ExecutionContext context;

    @Override
    public void parametrizedRun(InputStream sourceInputStream) throws RuntimeException {
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
        }
    }

    private void executeBlocksCallsSequence(Queue<Integer> executorsQueue) throws IOException {
        BlocksInOutTypes previousBlockType = BlocksInOutTypes.OutOnly;
        while (!executorsQueue.isEmpty()) {
            Integer blockId = executorsQueue.remove();
            ExecutableBlock executableBlock = BlocksBuilder.getInstance().buildBlock(context.getBlockNameById(blockId));
            BlocksInOutTypes currentExecutableBlockType = executableBlock.getBlockType();
            if (isCurrentBlockMatchThatPreviousOne(previousBlockType, currentExecutableBlockType)) {
                executableBlock.execute(blockId, context);
                previousBlockType = currentExecutableBlockType;
            } else {
                logger.log(Level.SEVERE, "Check the executing sequence for writefile and readfile blocks places");
                return;
            }

        }
    }

    private Boolean isCurrentBlockMatchThatPreviousOne(BlocksInOutTypes previous, BlocksInOutTypes current) {
        return !previous.equals(BlocksInOutTypes.InOnly) ||
                (!current.equals(BlocksInOutTypes.InOutAvailable) && !current.equals(BlocksInOutTypes.OutOnly));
    }

    public ArrayList<String> getContextOperatingText() {
        return context.getOperatingText();
    }

    public static InputStream getSourceInputStreamForReadingConfigsFromFile(String filename) {
        return WorkflowExecutor.class.getResourceAsStream(filename);
    }
}
