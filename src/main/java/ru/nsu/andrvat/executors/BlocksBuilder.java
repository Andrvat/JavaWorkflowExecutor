package ru.nsu.andrvat.executors;

import ru.nsu.andrvat.argsParsers.CommandLineArgsParser;
import ru.nsu.andrvat.loggersFeatures.LoggersHelper;
import ru.nsu.andrvat.runningBlocks.Executable;
import ru.nsu.andrvat.runningBlocks.ExecutableBlock;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class BlocksBuilder {
    private static final Logger logger = LoggersHelper.getLoggerInstance(BlocksBuilder.class.getName());

    private static final Properties executingCommandsConfigs = new Properties();

    private static volatile BlocksBuilder instance;

    public static BlocksBuilder getInstance() throws IOException {
        if (instance == null) {
            synchronized (BlocksBuilder.class) {
                if (instance == null) {
                    instance = new BlocksBuilder();
                }
            }
        }
        return instance;
    }

    private BlocksBuilder() throws IOException {
        InputStream configsStream = BlocksBuilder.class.getResourceAsStream("executingCommandsConfigs.properties");
        if (configsStream == null) {
            throw new IOException("Unable to read config");
        }
        executingCommandsConfigs.load(configsStream);
    }

    public ExecutableBlock buildBlock(String blockName) throws RuntimeException {
        try {
            String executableClassName = (String) executingCommandsConfigs.get(blockName);
            Class<?> operandClass = Class.forName(executableClassName);
            return (ExecutableBlock) operandClass.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException exception) {
            logger.log(Level.SEVERE, "Class by name [" + blockName + "] was not found. " +
                    "Program stopped. Exit from run app", exception);
            throw new RuntimeException();
        } catch (NoSuchMethodException exception) {
            logger.log(Level.SEVERE, "Class by name [" + blockName + "] couldn't found empty constructor. " +
                    "Program stopped. Exit from run app", exception);
            throw new RuntimeException();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            logger.log(Level.SEVERE, "Class by name [" + blockName + "] couldn't instance an object. " +
                    "Program stopped. Exit from run app", exception);
            throw new RuntimeException();
        } catch (Exception exception) {
            logger.log(Level.SEVERE, "Class by name [" + blockName + "] couldn't create an object. " +
                    "The reason is not defined. " +
                    "Program stopped. Exit from run app", exception);
            throw new RuntimeException();
        }
    }
}
