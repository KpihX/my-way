package com.kpihx_labs.myway.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FavoriteLocationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FavoriteLocation getFavoriteLocationSample1() {
        return new FavoriteLocation().id(1L).name("name1");
    }

    public static FavoriteLocation getFavoriteLocationSample2() {
        return new FavoriteLocation().id(2L).name("name2");
    }

    public static FavoriteLocation getFavoriteLocationRandomSampleGenerator() {
        return new FavoriteLocation().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
