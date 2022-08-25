package rocks.zipcode.squeaker.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocks.zipcode.squeaker.domain.Squeaks;
import rocks.zipcode.squeaker.repository.SqueaksRepository;
import rocks.zipcode.squeaker.service.dto.SqueaksDTO;
import rocks.zipcode.squeaker.service.mapper.SqueaksMapper;

/**
 * Service Implementation for managing {@link Squeaks}.
 */
@Service
@Transactional
public class SqueaksService {

    private final Logger log = LoggerFactory.getLogger(SqueaksService.class);

    private final SqueaksRepository squeaksRepository;

    private final SqueaksMapper squeaksMapper;

    public SqueaksService(SqueaksRepository squeaksRepository, SqueaksMapper squeaksMapper) {
        this.squeaksRepository = squeaksRepository;
        this.squeaksMapper = squeaksMapper;
    }

    /**
     * Save a squeaks.
     *
     * @param squeaksDTO the entity to save.
     * @return the persisted entity.
     */
    public SqueaksDTO save(SqueaksDTO squeaksDTO) {
        log.debug("Request to save Squeaks : {}", squeaksDTO);
        Squeaks squeaks = squeaksMapper.toEntity(squeaksDTO);
        squeaks = squeaksRepository.save(squeaks);
        return squeaksMapper.toDto(squeaks);
    }

    /**
     * Update a squeaks.
     *
     * @param squeaksDTO the entity to save.
     * @return the persisted entity.
     */
    public SqueaksDTO update(SqueaksDTO squeaksDTO) {
        log.debug("Request to save Squeaks : {}", squeaksDTO);
        Squeaks squeaks = squeaksMapper.toEntity(squeaksDTO);
        squeaks = squeaksRepository.save(squeaks);
        return squeaksMapper.toDto(squeaks);
    }

    /**
     * Partially update a squeaks.
     *
     * @param squeaksDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SqueaksDTO> partialUpdate(SqueaksDTO squeaksDTO) {
        log.debug("Request to partially update Squeaks : {}", squeaksDTO);

        return squeaksRepository
            .findById(squeaksDTO.getId())
            .map(existingSqueaks -> {
                squeaksMapper.partialUpdate(existingSqueaks, squeaksDTO);

                return existingSqueaks;
            })
            .map(squeaksRepository::save)
            .map(squeaksMapper::toDto);
    }

    /**
     * Get all the squeaks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SqueaksDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Squeaks");
        return squeaksRepository.findAll(pageable).map(squeaksMapper::toDto);
    }

    /**
     * Get all the squeaks with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SqueaksDTO> findAllWithEagerRelationships(Pageable pageable) {
        return squeaksRepository.findAllWithEagerRelationships(pageable).map(squeaksMapper::toDto);
    }

    /**
     * Get one squeaks by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SqueaksDTO> findOne(Long id) {
        log.debug("Request to get Squeaks : {}", id);
        return squeaksRepository.findOneWithEagerRelationships(id).map(squeaksMapper::toDto);
    }

    /**
     * Delete the squeaks by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Squeaks : {}", id);
        squeaksRepository.deleteById(id);
    }
}
