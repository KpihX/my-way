package com.kpihx_labs.myway.domain;

import static com.kpihx_labs.myway.domain.ItinaryTestSamples.*;
import static com.kpihx_labs.myway.domain.LocationCategoryTestSamples.*;
import static com.kpihx_labs.myway.domain.LocationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kpihx_labs.myway.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class LocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Location.class);
        Location location1 = getLocationSample1();
        Location location2 = new Location();
        assertThat(location1).isNotEqualTo(location2);

        location2.setId(location1.getId());
        assertThat(location1).isEqualTo(location2);

        location2 = getLocationSample2();
        assertThat(location1).isNotEqualTo(location2);
    }

    @Test
    void categoryTest() throws Exception {
        Location location = getLocationRandomSampleGenerator();
        LocationCategory locationCategoryBack = getLocationCategoryRandomSampleGenerator();

        location.setCategory(locationCategoryBack);
        assertThat(location.getCategory()).isEqualTo(locationCategoryBack);

        location.category(null);
        assertThat(location.getCategory()).isNull();
    }

    @Test
    void itinaryTest() throws Exception {
        Location location = getLocationRandomSampleGenerator();
        Itinary itinaryBack = getItinaryRandomSampleGenerator();

        location.addItinary(itinaryBack);
        assertThat(location.getItinaries()).containsOnly(itinaryBack);
        assertThat(itinaryBack.getLocations()).containsOnly(location);

        location.removeItinary(itinaryBack);
        assertThat(location.getItinaries()).doesNotContain(itinaryBack);
        assertThat(itinaryBack.getLocations()).doesNotContain(location);

        location.itinaries(new HashSet<>(Set.of(itinaryBack)));
        assertThat(location.getItinaries()).containsOnly(itinaryBack);
        assertThat(itinaryBack.getLocations()).containsOnly(location);

        location.setItinaries(new HashSet<>());
        assertThat(location.getItinaries()).doesNotContain(itinaryBack);
        assertThat(itinaryBack.getLocations()).doesNotContain(location);
    }
}
