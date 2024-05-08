package com.kpihx_labs.myway.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.kpihx_labs.myway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FavoriteLocationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FavoriteLocationDTO.class);
        FavoriteLocationDTO favoriteLocationDTO1 = new FavoriteLocationDTO();
        favoriteLocationDTO1.setId(1L);
        FavoriteLocationDTO favoriteLocationDTO2 = new FavoriteLocationDTO();
        assertThat(favoriteLocationDTO1).isNotEqualTo(favoriteLocationDTO2);
        favoriteLocationDTO2.setId(favoriteLocationDTO1.getId());
        assertThat(favoriteLocationDTO1).isEqualTo(favoriteLocationDTO2);
        favoriteLocationDTO2.setId(2L);
        assertThat(favoriteLocationDTO1).isNotEqualTo(favoriteLocationDTO2);
        favoriteLocationDTO1.setId(null);
        assertThat(favoriteLocationDTO1).isNotEqualTo(favoriteLocationDTO2);
    }
}
