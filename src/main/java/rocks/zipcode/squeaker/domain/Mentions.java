package rocks.zipcode.squeaker.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Mentions.
 */
@Entity
@Table(name = "mentions")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Mentions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2)
    @Column(name = "handle", nullable = false)
    private String handle;

    @ManyToOne
    @JsonIgnoreProperties(value = { "tags", "mentions", "utilizer" }, allowSetters = true)
    private Squeaks squeaks;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Mentions id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHandle() {
        return this.handle;
    }

    public Mentions handle(String handle) {
        this.setHandle(handle);
        return this;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public Squeaks getSqueaks() {
        return this.squeaks;
    }

    public void setSqueaks(Squeaks squeaks) {
        this.squeaks = squeaks;
    }

    public Mentions squeaks(Squeaks squeaks) {
        this.setSqueaks(squeaks);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mentions)) {
            return false;
        }
        return id != null && id.equals(((Mentions) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Mentions{" +
            "id=" + getId() +
            ", handle='" + getHandle() + "'" +
            "}";
    }
}
