package ru.nsu.andrvat.executors;

import ru.nsu.andrvat.argsParsers.CommandLineArgsParser;
import ru.nsu.andrvat.loggersFeatures.LoggersHelper;
import ru.nsu.andrvat.runningBlocks.Executable;
import ru.nsu.andrvat.workflowConfigs.WorkflowConfigsScanner;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkflowExecutor implements ParameterizedRunnable {
    private static final Logger logger = LoggersHelper.getLoggerInstance(CommandLineArgsParser.class.getName());

    private ExecutionContext context;
    private Queue<Integer> executorsQueue;

    @Override
    public void parametrizedRun(InputStream sourceInputStream) {
        WorkflowConfigsScanner configsScanner = new WorkflowConfigsScanner();
        try {
            configsScanner.scanConfig(sourceInputStream);
            configsScanner.analyzeConfig();
        } catch (RuntimeException exception) {
            logger.log(Level.SEVERE, "Program stopped. Exit from run app", exception);
            return;
        }

        context = ExecutionContext.builder()
                .blockNames(configsScanner.getBlockNames())
                .blockArguments(configsScanner.getBlockArguments())
                .operatingText(new ArrayList<>())
                .build();

        executorsQueue = configsScanner.getExecutorsQueue();
        executeBlocksCallsSequence();
    }

    private void executeBlocksCallsSequence() {
        while (!executorsQueue.isEmpty()) {
            try {
                Integer blockId = executorsQueue.remove();
                Executable executableBlock = new BlocksBuilder(context.getBlockNameById(blockId)).buildBlock();
                executableBlock.execute(blockId, context);
            } catch (RuntimeException exception) {
                logger.log(Level.SEVERE, "ParametrizedRun function ended abruptly. Program stopped", exception);
                return;
            }
        }
    }

    public ArrayList<String> getContextOperatingText() {
        return context.getOperatingText();
    }
}
