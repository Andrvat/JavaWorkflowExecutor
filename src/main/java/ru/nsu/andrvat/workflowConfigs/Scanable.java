package ru.nsu.andrvat.workflowConfigs;

public interface Scanable {
    void scanConfig(String sourceFilename) throws RuntimeException;
}
