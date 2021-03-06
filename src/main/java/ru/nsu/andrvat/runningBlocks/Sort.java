package ru.nsu.andrvat.runningBlocks;

import ru.nsu.andrvat.exceptions.BlockArgumentsNumberException;
import ru.nsu.andrvat.executors.ExecutionContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

public class Sort extends ExecutableBlock {
    private static final Logger logger = Logger.getLogger(Sort.class.getName());
    private static final Integer requiredArgumentsNumber = 0;

    public Sort() {
        super(BlocksInOutTypes.InOutAvailable);
    }

    @Override
    public void execute(ArrayList<String> blockArguments, ExecutionContext context) throws BlockArgumentsNumberException {
        ArgumentsChecker checker = ArgumentsChecker.builder()
                .requiredArgumentsNumber(requiredArgumentsNumber)
                .logger(logger)
                .build();
        checker.checkArgs(blockArguments);
        ArrayList<String> textForSort = context.getOperatingText();
        Collections.sort(textForSort);
        context.setOperatingText(textForSort);
        logger.info("All lines from operating text were successfully sorted");
    }
}
