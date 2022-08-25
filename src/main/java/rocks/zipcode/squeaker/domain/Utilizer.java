package rocks.zipcode.squeaker.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Utilizer.
 */
@Entity
@Table(name = "utilizer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Utilizer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2)
    @Column(name = "handle", nullable = false)
    private String handle;

    @Column(name = "followers")
    private Long followers;

    @Column(name = "following")
    private Long following;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "utilizer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tags", "mentions", "utilizer" }, allowSetters = true)
    private Set<Squeaks> squeaks = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Utilizer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHandle() {
        return this.handle;
    }

    public Utilizer handle(String handle) {
        this.setHandle(handle);
        return this;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public Long getFollowers() {
        return this.followers;
    }

    public Utilizer followers(Long followers) {
        this.setFollowers(followers);
        return this;
    }

    public void setFollowers(Long followers) {
        this.followers = followers;
    }

    public Long getFollowing() {
        return this.following;
    }

    public Utilizer following(Long following) {
        this.setFollowing(following);
        return this;
    }

    public void setFollowing(Long following) {
        this.following = following;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Utilizer user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Squeaks> getSqueaks() {
        return this.squeaks;
    }

    public void setSqueaks(Set<Squeaks> squeaks) {
        if (this.squeaks != null) {
            this.squeaks.forEach(i -> i.setUtilizer(null));
        }
        if (squeaks != null) {
            squeaks.forEach(i -> i.setUtilizer(this));
        }
        this.squeaks = squeaks;
    }

    public Utilizer squeaks(Set<Squeaks> squeaks) {
        this.setSqueaks(squeaks);
        return this;
    }

    public Utilizer addSqueaks(Squeaks squeaks) {
        this.squeaks.add(squeaks);
        squeaks.setUtilizer(this);
        return this;
    }

    public Utilizer removeSqueaks(Squeaks squeaks) {
        this.squeaks.remove(squeaks);
        squeaks.setUtilizer(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Utilizer)) {
            return false;
        }
        return id != null && id.equals(((Utilizer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Utilizer{" +
            "id=" + getId() +
            ", handle='" + getHandle() + "'" +
            ", followers=" + getFollowers() +
            ", following=" + getFollowing() +
            "}";
    }
}
