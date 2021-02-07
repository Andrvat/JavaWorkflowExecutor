package ru.nsu.andrvat.runningBlocks;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface Checkable {
    void checkArgs(ArrayList<String> arguments) throws RuntimeException;

    void checkTextForNull(ArrayList<String> text) throws RuntimeException;
}
