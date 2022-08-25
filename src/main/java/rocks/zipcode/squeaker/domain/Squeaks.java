package rocks.zipcode.squeaker.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Squeaks.
 */
@Entity
@Table(name = "squeaks")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Squeaks implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "content", length = 255, nullable = false)
    private String content;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "likes")
    private Long likes;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Lob
    @Column(name = "video")
    private byte[] video;

    @Column(name = "video_content_type")
    private String videoContentType;

    @OneToMany(mappedBy = "squeaks")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "squeaks" }, allowSetters = true)
    private Set<Tags> tags = new HashSet<>();

    @OneToMany(mappedBy = "squeaks")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "squeaks" }, allowSetters = true)
    private Set<Mentions> mentions = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "squeaks" }, allowSetters = true)
    private Utilizer utilizer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Squeaks id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public Squeaks content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Squeaks createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Long getLikes() {
        return this.likes;
    }

    public Squeaks likes(Long likes) {
        this.setLikes(likes);
        return this;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Squeaks image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Squeaks imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public byte[] getVideo() {
        return this.video;
    }

    public Squeaks video(byte[] video) {
        this.setVideo(video);
        return this;
    }

    public void setVideo(byte[] video) {
        this.video = video;
    }

    public String getVideoContentType() {
        return this.videoContentType;
    }

    public Squeaks videoContentType(String videoContentType) {
        this.videoContentType = videoContentType;
        return this;
    }

    public void setVideoContentType(String videoContentType) {
        this.videoContentType = videoContentType;
    }

    public Set<Tags> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tags> tags) {
        if (this.tags != null) {
            this.tags.forEach(i -> i.setSqueaks(null));
        }
        if (tags != null) {
            tags.forEach(i -> i.setSqueaks(this));
        }
        this.tags = tags;
    }

    public Squeaks tags(Set<Tags> tags) {
        this.setTags(tags);
        return this;
    }

    public Squeaks addTags(Tags tags) {
        this.tags.add(tags);
        tags.setSqueaks(this);
        return this;
    }

    public Squeaks removeTags(Tags tags) {
        this.tags.remove(tags);
        tags.setSqueaks(null);
        return this;
    }

    public Set<Mentions> getMentions() {
        return this.mentions;
    }

    public void setMentions(Set<Mentions> mentions) {
        if (this.mentions != null) {
            this.mentions.forEach(i -> i.setSqueaks(null));
        }
        if (mentions != null) {
            mentions.forEach(i -> i.setSqueaks(this));
        }
        this.mentions = mentions;
    }

    public Squeaks mentions(Set<Mentions> mentions) {
        this.setMentions(mentions);
        return this;
    }

    public Squeaks addMentions(Mentions mentions) {
        this.mentions.add(mentions);
        mentions.setSqueaks(this);
        return this;
    }

    public Squeaks removeMentions(Mentions mentions) {
        this.mentions.remove(mentions);
        mentions.setSqueaks(null);
        return this;
    }

    public Utilizer getUtilizer() {
        return this.utilizer;
    }

    public void setUtilizer(Utilizer utilizer) {
        this.utilizer = utilizer;
    }

    public Squeaks utilizer(Utilizer utilizer) {
        this.setUtilizer(utilizer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Squeaks)) {
            return false;
        }
        return id != null && id.equals(((Squeaks) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Squeaks{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", likes=" + getLikes() +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", video='" + getVideo() + "'" +
            ", videoContentType='" + getVideoContentType() + "'" +
            "}";
    }
}
