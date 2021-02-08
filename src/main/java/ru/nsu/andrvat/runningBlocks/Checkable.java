package ru.nsu.andrvat.runningBlocks;

import java.util.ArrayList;

public interface Checkable {
    void checkArgs(ArrayList<String> arguments) throws RuntimeException;

    void checkTextForNull(ArrayList<String> text) throws RuntimeException;
}
