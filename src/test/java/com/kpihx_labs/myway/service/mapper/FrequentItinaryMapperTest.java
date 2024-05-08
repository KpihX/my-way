package com.kpihx_labs.myway.service.mapper;

import static com.kpihx_labs.myway.domain.FrequentItinaryAsserts.*;
import static com.kpihx_labs.myway.domain.FrequentItinaryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FrequentItinaryMapperTest {

    private FrequentItinaryMapper frequentItinaryMapper;

    @BeforeEach
    void setUp() {
        frequentItinaryMapper = new FrequentItinaryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFrequentItinarySample1();
        var actual = frequentItinaryMapper.toEntity(frequentItinaryMapper.toDto(expected));
        assertFrequentItinaryAllPropertiesEquals(expected, actual);
    }
}
