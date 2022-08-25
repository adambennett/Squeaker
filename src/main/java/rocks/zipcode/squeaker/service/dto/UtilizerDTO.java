package rocks.zipcode.squeaker.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link rocks.zipcode.squeaker.domain.Utilizer} entity.
 */
public class UtilizerDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2)
    private String handle;

    private Long followers;

    private Long following;

    private UserDTO user;

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

    public Long getFollowers() {
        return followers;
    }

    public void setFollowers(Long followers) {
        this.followers = followers;
    }

    public Long getFollowing() {
        return following;
    }

    public void setFollowing(Long following) {
        this.following = following;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UtilizerDTO)) {
            return false;
        }

        UtilizerDTO utilizerDTO = (UtilizerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, utilizerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UtilizerDTO{" +
            "id=" + getId() +
            ", handle='" + getHandle() + "'" +
            ", followers=" + getFollowers() +
            ", following=" + getFollowing() +
            ", user=" + getUser() +
            "}";
    }
}
