package com.kpihx_labs.myway.domain;

import static com.kpihx_labs.myway.domain.ItinaryTestSamples.*;
import static com.kpihx_labs.myway.domain.LocationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kpihx_labs.myway.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ItinaryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Itinary.class);
        Itinary itinary1 = getItinarySample1();
        Itinary itinary2 = new Itinary();
        assertThat(itinary1).isNotEqualTo(itinary2);

        itinary2.setId(itinary1.getId());
        assertThat(itinary1).isEqualTo(itinary2);

        itinary2 = getItinarySample2();
        assertThat(itinary1).isNotEqualTo(itinary2);
    }

    @Test
    void locationTest() throws Exception {
        Itinary itinary = getItinaryRandomSampleGenerator();
        Location locationBack = getLocationRandomSampleGenerator();

        itinary.addLocation(locationBack);
        assertThat(itinary.getLocations()).containsOnly(locationBack);

        itinary.removeLocation(locationBack);
        assertThat(itinary.getLocations()).doesNotContain(locationBack);

        itinary.locations(new HashSet<>(Set.of(locationBack)));
        assertThat(itinary.getLocations()).containsOnly(locationBack);

        itinary.setLocations(new HashSet<>());
        assertThat(itinary.getLocations()).doesNotContain(locationBack);
    }
}
