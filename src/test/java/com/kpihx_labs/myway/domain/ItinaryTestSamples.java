package com.kpihx_labs.myway.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ItinaryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Itinary getItinarySample1() {
        return new Itinary().id(1L).name("name1").description("description1").duration(1).polyline("polyline1");
    }

    public static Itinary getItinarySample2() {
        return new Itinary().id(2L).name("name2").description("description2").duration(2).polyline("polyline2");
    }

    public static Itinary getItinaryRandomSampleGenerator() {
        return new Itinary()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .duration(intCount.incrementAndGet())
            .polyline(UUID.randomUUID().toString());
    }
}
