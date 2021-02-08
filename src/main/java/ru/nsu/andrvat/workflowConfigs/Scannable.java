package ru.nsu.andrvat.workflowConfigs;

import java.io.InputStream;

public interface Scannable {
    void scanConfig(InputStream sourceInputStream) throws RuntimeException;
}
