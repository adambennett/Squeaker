package rocks.zipcode.squeaker.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MentionsMapperTest {

    private MentionsMapper mentionsMapper;

    @BeforeEach
    public void setUp() {
        mentionsMapper = new MentionsMapperImpl();
    }
}
