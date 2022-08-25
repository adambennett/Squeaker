package rocks.zipcode.squeaker.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Tags.
 */
@Entity
@Table(name = "tags")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tags implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2)
    @Column(name = "hashtag", nullable = false)
    private String hashtag;

    @ManyToOne
    @JsonIgnoreProperties(value = { "tags", "mentions", "utilizer" }, allowSetters = true)
    private Squeaks squeaks;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tags id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHashtag() {
        return this.hashtag;
    }

    public Tags hashtag(String hashtag) {
        this.setHashtag(hashtag);
        return this;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public Squeaks getSqueaks() {
        return this.squeaks;
    }

    public void setSqueaks(Squeaks squeaks) {
        this.squeaks = squeaks;
    }

    public Tags squeaks(Squeaks squeaks) {
        this.setSqueaks(squeaks);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tags)) {
            return false;
        }
        return id != null && id.equals(((Tags) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tags{" +
            "id=" + getId() +
            ", hashtag='" + getHashtag() + "'" +
            "}";
    }
}
