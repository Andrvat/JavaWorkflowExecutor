package ru.nsu.andrvat.executor;

import lombok.Builder;
import ru.nsu.andrvat.argsParser.CommandLineArgsParser;
import ru.nsu.andrvat.loggersFeatures.LoggersHelper;

import java.util.*;
import java.util.logging.Logger;

@Builder
public class ExecutionContext {
    private final LinkedHashMap<Integer, ArrayList<String>> blockArguments;
    private final Map<Integer, String> blockNames;

    private ArrayList<String> operatingText;

    private static final Logger logger = LoggersHelper.getLoggerInstance(CommandLineArgsParser.class.getName());

    public ArrayList<String> getBlockArgumentsById(Integer id) {
        return blockArguments.get(id);
    }

    public String getBlockNameById(Integer id) {
        return blockNames.get(id);
    }

    public ArrayList<String> getOperatingText() {
        return operatingText;
    }

    public void setOperatingText(ArrayList<String> operatingText) {
        this.operatingText = operatingText;
    }

}
