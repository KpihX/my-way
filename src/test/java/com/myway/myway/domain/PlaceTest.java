package com.myway.myway.domain;

import static com.myway.myway.domain.ItinaryTestSamples.*;
import static com.myway.myway.domain.PlaceCategoryTestSamples.*;
import static com.myway.myway.domain.PlaceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.myway.myway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlaceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Place.class);
        Place place1 = getPlaceSample1();
        Place place2 = new Place();
        assertThat(place1).isNotEqualTo(place2);

        place2.setId(place1.getId());
        assertThat(place1).isEqualTo(place2);

        place2 = getPlaceSample2();
        assertThat(place1).isNotEqualTo(place2);
    }

    @Test
    void categoryTest() throws Exception {
        Place place = getPlaceRandomSampleGenerator();
        PlaceCategory placeCategoryBack = getPlaceCategoryRandomSampleGenerator();

        place.setCategory(placeCategoryBack);
        assertThat(place.getCategory()).isEqualTo(placeCategoryBack);

        place.category(null);
        assertThat(place.getCategory()).isNull();
    }

    @Test
    void itinaryTest() throws Exception {
        Place place = getPlaceRandomSampleGenerator();
        Itinary itinaryBack = getItinaryRandomSampleGenerator();

        place.setItinary(itinaryBack);
        assertThat(place.getItinary()).isEqualTo(itinaryBack);

        place.itinary(null);
        assertThat(place.getItinary()).isNull();
    }
}
