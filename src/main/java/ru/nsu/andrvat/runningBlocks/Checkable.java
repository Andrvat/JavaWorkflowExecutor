package ru.nsu.andrvat.runningBlocks;

import ru.nsu.andrvat.exceptions.BlockArgumentsNumberException;

import java.util.ArrayList;

public interface Checkable {
    void checkArgs(ArrayList<String> arguments) throws BlockArgumentsNumberException;
}
