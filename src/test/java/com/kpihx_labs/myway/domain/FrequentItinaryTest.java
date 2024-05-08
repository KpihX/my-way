package com.kpihx_labs.myway.domain;

import static com.kpihx_labs.myway.domain.FrequentItinaryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kpihx_labs.myway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FrequentItinaryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FrequentItinary.class);
        FrequentItinary frequentItinary1 = getFrequentItinarySample1();
        FrequentItinary frequentItinary2 = new FrequentItinary();
        assertThat(frequentItinary1).isNotEqualTo(frequentItinary2);

        frequentItinary2.setId(frequentItinary1.getId());
        assertThat(frequentItinary1).isEqualTo(frequentItinary2);

        frequentItinary2 = getFrequentItinarySample2();
        assertThat(frequentItinary1).isNotEqualTo(frequentItinary2);
    }
}
