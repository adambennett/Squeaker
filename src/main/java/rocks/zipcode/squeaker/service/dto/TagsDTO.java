package rocks.zipcode.squeaker.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link rocks.zipcode.squeaker.domain.Tags} entity.
 */
public class TagsDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2)
    private String hashtag;

    private SqueaksDTO squeaks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public SqueaksDTO getSqueaks() {
        return squeaks;
    }

    public void setSqueaks(SqueaksDTO squeaks) {
        this.squeaks = squeaks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TagsDTO)) {
            return false;
        }

        TagsDTO tagsDTO = (TagsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tagsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TagsDTO{" +
            "id=" + getId() +
            ", hashtag='" + getHashtag() + "'" +
            ", squeaks=" + getSqueaks() +
            "}";
    }
}
