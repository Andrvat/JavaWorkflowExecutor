package ru.nsu.andrvat.executors;

import ru.nsu.andrvat.argsParsers.CommandLineArgsParser;
import ru.nsu.andrvat.loggersFeatures.LoggersHelper;
import ru.nsu.andrvat.runningBlocks.Executable;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class BlocksBuilder {
    private static final Logger logger = LoggersHelper.getLoggerInstance(CommandLineArgsParser.class.getName());

    private static final String packageNameForExecutableClasses = Executable.class.getPackageName() + ".";
    private final String fullClassForExecuteName;

    public BlocksBuilder(String executableClassName) {
        fullClassForExecuteName = packageNameForExecutableClasses
                + getCorrectRepresentationOfClassNameWithFirstCharInUpperCase(executableClassName);
    }

    public Executable buildBlock() throws RuntimeException {
        try {
            Class<?> operandClass = Class.forName(fullClassForExecuteName);
            return (Executable) operandClass.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException exception) {
            logger.log(Level.SEVERE, "Class by name [" + fullClassForExecuteName + "] was not found. " +
                    "Program stopped. Exit from run app", exception);
            throw new RuntimeException();
        } catch (NoSuchMethodException exception) {
            logger.log(Level.SEVERE, "Class by name [" + fullClassForExecuteName + "] couldn't found empty constructor. " +
                    "Program stopped. Exit from run app", exception);
            throw new RuntimeException();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            logger.log(Level.SEVERE, "Class by name [" + fullClassForExecuteName + "] couldn't instance an object. " +
                    "Program stopped. Exit from run app", exception);
            throw new RuntimeException();
        }
    }

    private String getCorrectRepresentationOfClassNameWithFirstCharInUpperCase(String word) {
        if (word == null || word.isEmpty()) {
            return "";
        }
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }
}
