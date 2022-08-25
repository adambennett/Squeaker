package rocks.zipcode.squeaker.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocks.zipcode.squeaker.domain.Tags;
import rocks.zipcode.squeaker.repository.TagsRepository;
import rocks.zipcode.squeaker.service.dto.TagsDTO;
import rocks.zipcode.squeaker.service.mapper.TagsMapper;

/**
 * Service Implementation for managing {@link Tags}.
 */
@Service
@Transactional
public class TagsService {

    private final Logger log = LoggerFactory.getLogger(TagsService.class);

    private final TagsRepository tagsRepository;

    private final TagsMapper tagsMapper;

    public TagsService(TagsRepository tagsRepository, TagsMapper tagsMapper) {
        this.tagsRepository = tagsRepository;
        this.tagsMapper = tagsMapper;
    }

    /**
     * Save a tags.
     *
     * @param tagsDTO the entity to save.
     * @return the persisted entity.
     */
    public TagsDTO save(TagsDTO tagsDTO) {
        log.debug("Request to save Tags : {}", tagsDTO);
        Tags tags = tagsMapper.toEntity(tagsDTO);
        tags = tagsRepository.save(tags);
        return tagsMapper.toDto(tags);
    }

    /**
     * Update a tags.
     *
     * @param tagsDTO the entity to save.
     * @return the persisted entity.
     */
    public TagsDTO update(TagsDTO tagsDTO) {
        log.debug("Request to save Tags : {}", tagsDTO);
        Tags tags = tagsMapper.toEntity(tagsDTO);
        tags = tagsRepository.save(tags);
        return tagsMapper.toDto(tags);
    }

    /**
     * Partially update a tags.
     *
     * @param tagsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TagsDTO> partialUpdate(TagsDTO tagsDTO) {
        log.debug("Request to partially update Tags : {}", tagsDTO);

        return tagsRepository
            .findById(tagsDTO.getId())
            .map(existingTags -> {
                tagsMapper.partialUpdate(existingTags, tagsDTO);

                return existingTags;
            })
            .map(tagsRepository::save)
            .map(tagsMapper::toDto);
    }

    /**
     * Get all the tags.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TagsDTO> findAll() {
        log.debug("Request to get all Tags");
        return tagsRepository.findAll().stream().map(tagsMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one tags by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TagsDTO> findOne(Long id) {
        log.debug("Request to get Tags : {}", id);
        return tagsRepository.findById(id).map(tagsMapper::toDto);
    }

    /**
     * Delete the tags by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Tags : {}", id);
        tagsRepository.deleteById(id);
    }
}
