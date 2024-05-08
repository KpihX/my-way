package com.kpihx_labs.myway.service.mapper;

import static com.kpihx_labs.myway.domain.ItinaryAsserts.*;
import static com.kpihx_labs.myway.domain.ItinaryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ItinaryMapperTest {

    private ItinaryMapper itinaryMapper;

    @BeforeEach
    void setUp() {
        itinaryMapper = new ItinaryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getItinarySample1();
        var actual = itinaryMapper.toEntity(itinaryMapper.toDto(expected));
        assertItinaryAllPropertiesEquals(expected, actual);
    }
}
