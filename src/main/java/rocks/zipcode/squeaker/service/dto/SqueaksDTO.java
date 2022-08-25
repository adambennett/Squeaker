package rocks.zipcode.squeaker.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link rocks.zipcode.squeaker.domain.Squeaks} entity.
 */
public class SqueaksDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String content;

    private Instant createdAt;

    private Long likes;

    @Lob
    private byte[] image;

    private String imageContentType;

    @Lob
    private byte[] video;

    private String videoContentType;
    private UtilizerDTO utilizer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public byte[] getVideo() {
        return video;
    }

    public void setVideo(byte[] video) {
        this.video = video;
    }

    public String getVideoContentType() {
        return videoContentType;
    }

    public void setVideoContentType(String videoContentType) {
        this.videoContentType = videoContentType;
    }

    public UtilizerDTO getUtilizer() {
        return utilizer;
    }

    public void setUtilizer(UtilizerDTO utilizer) {
        this.utilizer = utilizer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SqueaksDTO)) {
            return false;
        }

        SqueaksDTO squeaksDTO = (SqueaksDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, squeaksDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SqueaksDTO{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", likes=" + getLikes() +
            ", image='" + getImage() + "'" +
            ", video='" + getVideo() + "'" +
            ", utilizer=" + getUtilizer() +
            "}";
    }
}
