package rocks.zipcode.squeaker.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rocks.zipcode.squeaker.domain.Utilizer;

/**
 * Spring Data JPA repository for the Utilizer entity.
 */
@Repository
public interface UtilizerRepository extends JpaRepository<Utilizer, Long> {
    default Optional<Utilizer> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Utilizer> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Utilizer> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct utilizer from Utilizer utilizer left join fetch utilizer.user",
        countQuery = "select count(distinct utilizer) from Utilizer utilizer"
    )
    Page<Utilizer> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct utilizer from Utilizer utilizer left join fetch utilizer.user")
    List<Utilizer> findAllWithToOneRelationships();

    @Query("select utilizer from Utilizer utilizer left join fetch utilizer.user where utilizer.id =:id")
    Optional<Utilizer> findOneWithToOneRelationships(@Param("id") Long id);
}
