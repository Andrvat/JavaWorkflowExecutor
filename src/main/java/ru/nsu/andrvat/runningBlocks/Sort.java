package ru.nsu.andrvat.runningBlocks;

import ru.nsu.andrvat.argsParsers.CommandLineArgsParser;
import ru.nsu.andrvat.exceptions.BlockArgumentsNumberException;
import ru.nsu.andrvat.executors.ExecutionContext;
import ru.nsu.andrvat.loggersFeatures.LoggersHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

public class Sort extends ExecutableBlock {
    private static final Logger logger = LoggersHelper.getLoggerInstance(Sort.class.getName());
    private static final Integer requiredArgumentsNumber = 0;

    public Sort() {
        super(BlocksInOutTypes.InOutAvailable);
    }

    @Override
    public void execute(Integer id, ExecutionContext context) throws BlockArgumentsNumberException {
        ArgumentsChecker checker = ArgumentsChecker.builder()
                .requiredArgumentsNumber(requiredArgumentsNumber)
                .logger(logger)
                .build();
        checker.checkArgs(context.getBlockArgumentsById(id));
        ArrayList<String> textForSort = context.getOperatingText();
        Collections.sort(textForSort);
        context.setOperatingText(textForSort);
        logger.info("All lines from operating text were successfully sorted");
    }
}
