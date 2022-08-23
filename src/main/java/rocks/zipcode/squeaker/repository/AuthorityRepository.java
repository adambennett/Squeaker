package rocks.zipcode.squeaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rocks.zipcode.squeaker.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
