package rocks.zipcode.squeaker.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import rocks.zipcode.squeaker.web.rest.TestUtil;

class TagsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TagsDTO.class);
        TagsDTO tagsDTO1 = new TagsDTO();
        tagsDTO1.setId(1L);
        TagsDTO tagsDTO2 = new TagsDTO();
        assertThat(tagsDTO1).isNotEqualTo(tagsDTO2);
        tagsDTO2.setId(tagsDTO1.getId());
        assertThat(tagsDTO1).isEqualTo(tagsDTO2);
        tagsDTO2.setId(2L);
        assertThat(tagsDTO1).isNotEqualTo(tagsDTO2);
        tagsDTO1.setId(null);
        assertThat(tagsDTO1).isNotEqualTo(tagsDTO2);
    }
}
