package com.myway.myway.domain;

import static com.myway.myway.domain.ItinaryTestSamples.*;
import static com.myway.myway.domain.PlaceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.myway.myway.web.rest.TestUtil;
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
    void placesTest() throws Exception {
        Itinary itinary = getItinaryRandomSampleGenerator();
        Place placeBack = getPlaceRandomSampleGenerator();

        itinary.addPlaces(placeBack);
        assertThat(itinary.getPlaces()).containsOnly(placeBack);
        assertThat(placeBack.getItinary()).isEqualTo(itinary);

        itinary.removePlaces(placeBack);
        assertThat(itinary.getPlaces()).doesNotContain(placeBack);
        assertThat(placeBack.getItinary()).isNull();

        itinary.places(new HashSet<>(Set.of(placeBack)));
        assertThat(itinary.getPlaces()).containsOnly(placeBack);
        assertThat(placeBack.getItinary()).isEqualTo(itinary);

        itinary.setPlaces(new HashSet<>());
        assertThat(itinary.getPlaces()).doesNotContain(placeBack);
        assertThat(placeBack.getItinary()).isNull();
    }
}
