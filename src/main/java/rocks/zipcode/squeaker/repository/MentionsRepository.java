package rocks.zipcode.squeaker.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import rocks.zipcode.squeaker.domain.Mentions;

/**
 * Spring Data JPA repository for the Mentions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MentionsRepository extends JpaRepository<Mentions, Long> {}
