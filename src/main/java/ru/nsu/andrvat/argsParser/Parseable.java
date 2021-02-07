package ru.nsu.andrvat.argsParser;

import org.apache.commons.cli.ParseException;

public interface Parseable {
    void parseArgs(String[] args) throws ParseException;
}
