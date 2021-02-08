package ru.nsu.andrvat.argsParsers;

import org.apache.commons.cli.ParseException;

public interface Parseable {
    void parseArgs(String[] args) throws ParseException;
}
