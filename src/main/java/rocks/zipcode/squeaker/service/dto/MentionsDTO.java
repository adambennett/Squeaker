package rocks.zipcode.squeaker.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link rocks.zipcode.squeaker.domain.Mentions} entity.
 */
public class MentionsDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2)
    private String handle;

    private SqueaksDTO squeaks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
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
        if (!(o instanceof MentionsDTO)) {
            return false;
        }

        MentionsDTO mentionsDTO = (MentionsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, mentionsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MentionsDTO{" +
            "id=" + getId() +
            ", handle='" + getHandle() + "'" +
            ", squeaks=" + getSqueaks() +
            "}";
    }
}
