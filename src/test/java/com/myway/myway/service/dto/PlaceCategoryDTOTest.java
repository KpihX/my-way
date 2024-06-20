package com.myway.myway.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.myway.myway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlaceCategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlaceCategoryDTO.class);
        PlaceCategoryDTO placeCategoryDTO1 = new PlaceCategoryDTO();
        placeCategoryDTO1.setId(1L);
        PlaceCategoryDTO placeCategoryDTO2 = new PlaceCategoryDTO();
        assertThat(placeCategoryDTO1).isNotEqualTo(placeCategoryDTO2);
        placeCategoryDTO2.setId(placeCategoryDTO1.getId());
        assertThat(placeCategoryDTO1).isEqualTo(placeCategoryDTO2);
        placeCategoryDTO2.setId(2L);
        assertThat(placeCategoryDTO1).isNotEqualTo(placeCategoryDTO2);
        placeCategoryDTO1.setId(null);
        assertThat(placeCategoryDTO1).isNotEqualTo(placeCategoryDTO2);
    }
}
