package ru.nsu.andrvat.runningBlocks;

import ru.nsu.andrvat.argsParser.CommandLineArgsParser;
import ru.nsu.andrvat.executor.ExecutionContext;
import ru.nsu.andrvat.loggersFeatures.LoggersHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

public class Sort implements Executesable {
    private static final Logger logger = LoggersHelper.getLoggerInstance(CommandLineArgsParser.class.getName());
    private static final Integer requiredArgumentsNumber = 0;

    @Override
    public void execute(Integer id, ExecutionContext context) throws RuntimeException {
        ArgumentsChecker checker = ArgumentsChecker.builder()
                .requiredArgumentsNumber(requiredArgumentsNumber)
                .logger(logger)
                .build();
        checker.checkArgs(context.getBlockArgumentsById(id));
        checker.checkTextForNull(context.getOperatingText());
        ArrayList<String> textForSort = context.getOperatingText();
        Collections.sort(textForSort);
        context.setOperatingText(textForSort);
        logger.info("All lines from operating text were successfully sorted");
    }
}
