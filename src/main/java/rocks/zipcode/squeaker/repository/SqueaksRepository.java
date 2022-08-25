package rocks.zipcode.squeaker.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rocks.zipcode.squeaker.domain.Squeaks;

/**
 * Spring Data JPA repository for the Squeaks entity.
 */
@Repository
public interface SqueaksRepository extends JpaRepository<Squeaks, Long> {
    default Optional<Squeaks> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Squeaks> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Squeaks> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct squeaks from Squeaks squeaks left join fetch squeaks.utilizer",
        countQuery = "select count(distinct squeaks) from Squeaks squeaks"
    )
    Page<Squeaks> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct squeaks from Squeaks squeaks left join fetch squeaks.utilizer")
    List<Squeaks> findAllWithToOneRelationships();

    @Query("select squeaks from Squeaks squeaks left join fetch squeaks.utilizer where squeaks.id =:id")
    Optional<Squeaks> findOneWithToOneRelationships(@Param("id") Long id);
}
