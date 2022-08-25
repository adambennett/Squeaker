package rocks.zipcode.squeaker.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import rocks.zipcode.squeaker.web.rest.TestUtil;

class SqueaksTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Squeaks.class);
        Squeaks squeaks1 = new Squeaks();
        squeaks1.setId(1L);
        Squeaks squeaks2 = new Squeaks();
        squeaks2.setId(squeaks1.getId());
        assertThat(squeaks1).isEqualTo(squeaks2);
        squeaks2.setId(2L);
        assertThat(squeaks1).isNotEqualTo(squeaks2);
        squeaks1.setId(null);
        assertThat(squeaks1).isNotEqualTo(squeaks2);
    }
}
