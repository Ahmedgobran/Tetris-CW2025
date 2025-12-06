package com.comp2042.logic.bricks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A concrete implementation of {@link BrickGenerator} that produces bricks randomly.
 * <p>
 * Maintains a queue of upcoming bricks to allow for a "Next Piece" preview.
 * Uses a bag-style randomization or simple random selection from the available brick types.
 * </p>
 */
public class RandomBrickGenerator implements BrickGenerator {

    private final List<Brick> brickList;
    private final Deque<Brick> nextBricks = new ArrayDeque<>();

    /**
     * Initializes the generator with all standard Tetris brick types.
     * Pre-populates the queue with the first set of bricks.
     */
    public RandomBrickGenerator() {
        brickList = new ArrayList<>();
        brickList.add(new IBrick());
        brickList.add(new JBrick());
        brickList.add(new LBrick());
        brickList.add(new OBrick());
        brickList.add(new SBrick());
        brickList.add(new TBrick());
        brickList.add(new ZBrick());

        // Initialize queue
        nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
        nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
    }

    /**
     * Retrieves the next brick from the queue and replenishes the queue.
     *
     * @return The next {@link Brick} to play.
     */
    @Override
    public Brick getBrick() {
        if (nextBricks.size() <= 1) {
            nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
        }
        return nextBricks.poll();
    }

    /**
     * Returns the upcoming brick for preview purposes.
     *
     * @return The {@link Brick} that will be returned by the next call to {@link #getBrick()}.
     */
    @Override
    public Brick getNextBrick() {
        return nextBricks.peek();
    }
}