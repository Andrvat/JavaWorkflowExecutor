package ru.nsu.andrvat.argsParsers;

import org.apache.commons.cli.*;
import ru.nsu.andrvat.loggersFeatures.LoggersHelper;

import java.io.InputStream;
import java.util.logging.Logger;

public class CommandLineArgsParser implements Parseable {
    private static final Logger logger = LoggersHelper.getLoggerInstance(CommandLineArgsParser.class.getName());

    private final Options parserOptions;
    private String workflowConfigFilename;

    public CommandLineArgsParser() {
        parserOptions = new Options();
        parserOptions.addOption(createNewOption(OptionParamsBuilder.builder()
                .opt("w")
                .longOpt("workflow")
                .hasArg(true)
                .description("Workflow configuration's filename")
                .build()));

        logger.info("All options were added by class constructor");
    }

    private Option createNewOption(OptionParamsBuilder paramsBuilder) {
        Option option = new Option(paramsBuilder.getOpt(),
                paramsBuilder.getLongOpt(),
                paramsBuilder.getHasArg(),
                paramsBuilder.getDescription());
        option.setRequired(true);

        logger.info("Option was created");
        return option;
    }

    @Override
    public void parseArgs(String[] args) throws ParseException {
        CommandLineParser parser = new BasicParser();
        CommandLine commandLine = parser.parse(parserOptions, args);

        workflowConfigFilename = commandLine.getOptionValue("w");
        logger.info("Config filename: " + workflowConfigFilename);

        logger.info("All command line arguments were read");
    }

    public Options getParserOptions() {
        return parserOptions;
    }

    public String getWorkflowConfigFilename() {
        return workflowConfigFilename;
    }
}
