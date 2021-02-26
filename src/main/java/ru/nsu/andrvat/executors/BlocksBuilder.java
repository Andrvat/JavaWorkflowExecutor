package ru.nsu.andrvat.executors;

import ru.nsu.andrvat.exceptions.BlocksFactoryException;
import ru.nsu.andrvat.runningBlocks.ExecutableBlock;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class BlocksBuilder {
    private static final Logger logger = Logger.getLogger(BlocksBuilder.class.getName());

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

    public ExecutableBlock buildBlock(String blockName) throws BlocksFactoryException {
        try {
            String executableClassName = (String) executingCommandsConfigs.get(blockName);
//            if (executableClassName == null) {
//                throw new BlocksFactoryException();
//            }
            Class<?> operandClass = Class.forName(executableClassName);
            return (ExecutableBlock) operandClass.getDeclaredConstructor().newInstance();
        } catch (Exception exception) {
            logger.log(Level.SEVERE, "Class by name [" + blockName + "] couldn't create an object. " +
                    "Program stopped. Exit from run app", exception);
            throw new BlocksFactoryException();
        }
    }
}
