package org.eaglescript.vm;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

class GlobalSequenceTest {
    @Test
    public void testNextId() {
        for (int i = 0; i < 100; i++) {
            System.out.println("Sequence generated: " + Long.toHexString(GlobalSequence.nextId()));
        }
    }

    @Test
    public void testTimeCost() {
        Set<Long> ids = new HashSet<>();
        int total = 10000;
        long start = System.nanoTime();
        for (int i = 0; i < total; i++) {
            if (!ids.add(GlobalSequence.nextId())) {
                Assertions.fail("Duplicated ID");
            }
        }

        long elapsed = System.nanoTime() - start;
        System.out.println("Generated " + total + " sequences in " + TimeUnit.NANOSECONDS.toMillis(elapsed) + "ms");
    }
}