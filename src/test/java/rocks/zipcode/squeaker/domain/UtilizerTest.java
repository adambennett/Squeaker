package rocks.zipcode.squeaker.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import rocks.zipcode.squeaker.web.rest.TestUtil;

class UtilizerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Utilizer.class);
        Utilizer utilizer1 = new Utilizer();
        utilizer1.setId(1L);
        Utilizer utilizer2 = new Utilizer();
        utilizer2.setId(utilizer1.getId());
        assertThat(utilizer1).isEqualTo(utilizer2);
        utilizer2.setId(2L);
        assertThat(utilizer1).isNotEqualTo(utilizer2);
        utilizer1.setId(null);
        assertThat(utilizer1).isNotEqualTo(utilizer2);
    }
}
