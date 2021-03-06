package ru.nsu.andrvat.appRunner;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import ru.nsu.andrvat.argsParsers.CommandLineArgsParser;
import ru.nsu.andrvat.executors.WorkflowExecutor;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ApplicationRunner {
    private static final Logger logger = Logger.getLogger(ApplicationRunner.class.getName());

    // TODO: getResourceAsStream ВО ВСЕМ ПРОЕКТЕ читает файлы только рядом с .class. Как их заставить читать оттуда, где мне нужно?
    public static void main(String[] args) {
        try {
            InputStream loggerConfigFile = ApplicationRunner.class.getResourceAsStream("loggerConfigs.properties");
            LogManager.getLogManager().readConfiguration(loggerConfigFile);
        } catch (IOException exception) {
            Logger.getAnonymousLogger().severe("Could not load loggerConfigs.properties file");
            Logger.getAnonymousLogger().severe(exception.getMessage());
        }

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

        try {
            WorkflowExecutor executor = new WorkflowExecutor();
            executor.parametrizedRun(WorkflowExecutor.getSourceInputStreamForReadingConfigsFromFile(
                    commandLineArgsParser.getWorkflowConfigFilename()));
        } catch (RuntimeException exception) {
            logger.log(Level.SEVERE, "ParametrizedRun function ended abruptly. Program stopped!", exception);
        }
    }

}
