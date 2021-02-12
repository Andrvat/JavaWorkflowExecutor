package ru.nsu.andrvat.runningBlocks;

public abstract class ExecutableBlock implements Executable {
    private final BlocksInOutTypes blockType;

    public ExecutableBlock(BlocksInOutTypes blockType) {
        this.blockType = blockType;
    }

    public BlocksInOutTypes getBlockType() {
        return blockType;
    }
}
