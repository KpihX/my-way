package com.myway.myway.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ItinaryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Itinary getItinarySample1() {
        return new Itinary().id(1L).name("name1").prefName("prefName1").description("description1").image("image1");
    }

    public static Itinary getItinarySample2() {
        return new Itinary().id(2L).name("name2").prefName("prefName2").description("description2").image("image2");
    }

    public static Itinary getItinaryRandomSampleGenerator() {
        return new Itinary()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .prefName(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .image(UUID.randomUUID().toString());
    }
}
