package com.reactionmachine.util;

import com.reactionmachine.core.IRandom;
import java.util.Random;

public class RandomGenerator implements IRandom {
    private final Random rnd;

    public RandomGenerator() {
        this.rnd = new Random();
    }

    public RandomGenerator(long seed) {
        this.rnd = new Random(seed);
    }

    @Override
    public int getRandom(int from, int to) {
        return rnd.nextInt(to - from) + from;
    }
}