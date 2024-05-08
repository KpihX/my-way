package com.kpihx_labs.myway.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FrequentItinaryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FrequentItinary getFrequentItinarySample1() {
        return new FrequentItinary().id(1L).name("name1");
    }

    public static FrequentItinary getFrequentItinarySample2() {
        return new FrequentItinary().id(2L).name("name2");
    }

    public static FrequentItinary getFrequentItinaryRandomSampleGenerator() {
        return new FrequentItinary().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
