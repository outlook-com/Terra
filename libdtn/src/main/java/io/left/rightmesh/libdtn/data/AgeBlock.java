package io.left.rightmesh.libdtn.data;

/**
 * AgeBlock is a block that can be used to track lifetime for DTN node that doesn't have access to
 * UTC time but have a mean to track the elapsed time between reception and delivery of the block.
 *
 * @author Lucien Loiseau on 03/09/18.
 */
public class AgeBlock extends ExtensionBlock {

    public static final int type = 10;

    public long age = 0;
    long time_start;
    long time_end;

    AgeBlock() {
        super(type);
        setV6Flag(BlockV6Flags.REPLICATE_IN_EVERY_FRAGMENT, true);
        start();
    }

    AgeBlock(long age) {
        super(type);
        setV6Flag(BlockV6Flags.REPLICATE_IN_EVERY_FRAGMENT, true);
        this.age = age;
        start();
    }

    /**
     * Start aging this AgeBlock.
     */
    public void start() {
        time_start = System.nanoTime();
    }

    /**
     * Stop aging this AgeBlock.
     */
    public void stop() {
        time_end = System.nanoTime();
    }
}
