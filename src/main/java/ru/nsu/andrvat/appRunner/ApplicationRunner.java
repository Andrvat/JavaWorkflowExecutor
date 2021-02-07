package ru.nsu.andrvat.appRunner;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import ru.nsu.andrvat.argsParser.CommandLineArgsParser;
import ru.nsu.andrvat.executor.ExecutionContext;
import ru.nsu.andrvat.loggersFeatures.LoggersHelper;
import ru.nsu.andrvat.runningBlocks.Executesable;
import ru.nsu.andrvat.workflowConfigs.WorkflowConfigsScanner;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationRunner {
    private static final Logger logger = LoggersHelper.getLoggerInstance(CommandLineArgsParser.class.getName());

    // TODO: getResourceAsStream ВО ВСЕМ ПРОЕКТЕ читает файлы только рядом с .class. Как их заставить читать оттуда, где мне нужно?
    public static void main(String[] args) {
        CommandLineArgsParser commandLineArgsParser = new CommandLineArgsParser();
        try {
            commandLineArgsParser.parseArgs(args);
        } catch (ParseException exception) {
            logger.log(Level.SEVERE, "Couldn't parse command line arguments", exception);
            HelpFormatter formatter = new HelpFormatter();
            System.out.println(exception.getMessage());
            formatter.printHelp("ApplicationRunner", commandLineArgsParser.getParserOptions());
            logger.log(Level.SEVERE, "Program stopped");
            return;
        }
        runApplication(commandLineArgsParser.getWorkflowConfigFilename());
    }

    public static void runApplication(String workflowConfigName) {
        WorkflowConfigsScanner configsScanner = new WorkflowConfigsScanner();
        try {
            configsScanner.scanConfig(workflowConfigName);
            configsScanner.analyzeConfig();
        } catch (RuntimeException exception) {
            logger.log(Level.SEVERE, "Program stopped. Exit from run app", exception);
            return;
        }

        ExecutionContext context = ExecutionContext.builder()
                .blockNames(configsScanner.getBlockNames())
                .blockArguments(configsScanner.getBlockArguments())
                .operatingText(new ArrayList<>())
                .build();

        Queue<Integer> executorsQueue = configsScanner.getExecutorsQueue();
        String packageNameForExecutesableClasses = Executesable.class.getPackageName() + ".";

        while (!executorsQueue.isEmpty()) {
            Integer blockId = executorsQueue.remove();
            String fullClassForExecuteName = packageNameForExecutesableClasses
                    + getCorrectRepresentationOfClassNameWithFirstCharInUpperCase(context.getBlockNameById(blockId));
            try {
                Class<?> operandClass = Class.forName(fullClassForExecuteName);
                Executesable operand = (Executesable) operandClass.getDeclaredConstructor().newInstance();
                operand.execute(blockId, context);
            } catch (ClassNotFoundException exception) {
                logger.log(Level.SEVERE, "Class by name [" + fullClassForExecuteName + "] was not found. " +
                        "Program stopped. Exit from run app", exception);
                return;
            } catch (NoSuchMethodException exception) {
                logger.log(Level.SEVERE, "Class by name [" + fullClassForExecuteName + "] couldn't found empty constructor. " +
                        "Program stopped. Exit from run app", exception);
                return;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException exception) {
                logger.log(Level.SEVERE, "Class by name [" + fullClassForExecuteName + "] couldn't instance an object. " +
                        "Program stopped. Exit from run app", exception);
                return;
            }
        }
    }

    public static String getCorrectRepresentationOfClassNameWithFirstCharInUpperCase(String word) {
        if (word == null || word.isEmpty()) {
            return "";
        }
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

}
