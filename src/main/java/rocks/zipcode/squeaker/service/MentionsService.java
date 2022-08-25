package rocks.zipcode.squeaker.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocks.zipcode.squeaker.domain.Mentions;
import rocks.zipcode.squeaker.repository.MentionsRepository;
import rocks.zipcode.squeaker.service.dto.MentionsDTO;
import rocks.zipcode.squeaker.service.mapper.MentionsMapper;

/**
 * Service Implementation for managing {@link Mentions}.
 */
@Service
@Transactional
public class MentionsService {

    private final Logger log = LoggerFactory.getLogger(MentionsService.class);

    private final MentionsRepository mentionsRepository;

    private final MentionsMapper mentionsMapper;

    public MentionsService(MentionsRepository mentionsRepository, MentionsMapper mentionsMapper) {
        this.mentionsRepository = mentionsRepository;
        this.mentionsMapper = mentionsMapper;
    }

    /**
     * Save a mentions.
     *
     * @param mentionsDTO the entity to save.
     * @return the persisted entity.
     */
    public MentionsDTO save(MentionsDTO mentionsDTO) {
        log.debug("Request to save Mentions : {}", mentionsDTO);
        Mentions mentions = mentionsMapper.toEntity(mentionsDTO);
        mentions = mentionsRepository.save(mentions);
        return mentionsMapper.toDto(mentions);
    }

    /**
     * Update a mentions.
     *
     * @param mentionsDTO the entity to save.
     * @return the persisted entity.
     */
    public MentionsDTO update(MentionsDTO mentionsDTO) {
        log.debug("Request to save Mentions : {}", mentionsDTO);
        Mentions mentions = mentionsMapper.toEntity(mentionsDTO);
        mentions = mentionsRepository.save(mentions);
        return mentionsMapper.toDto(mentions);
    }

    /**
     * Partially update a mentions.
     *
     * @param mentionsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MentionsDTO> partialUpdate(MentionsDTO mentionsDTO) {
        log.debug("Request to partially update Mentions : {}", mentionsDTO);

        return mentionsRepository
            .findById(mentionsDTO.getId())
            .map(existingMentions -> {
                mentionsMapper.partialUpdate(existingMentions, mentionsDTO);

                return existingMentions;
            })
            .map(mentionsRepository::save)
            .map(mentionsMapper::toDto);
    }

    /**
     * Get all the mentions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MentionsDTO> findAll() {
        log.debug("Request to get all Mentions");
        return mentionsRepository.findAll().stream().map(mentionsMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one mentions by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MentionsDTO> findOne(Long id) {
        log.debug("Request to get Mentions : {}", id);
        return mentionsRepository.findById(id).map(mentionsMapper::toDto);
    }

    /**
     * Delete the mentions by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Mentions : {}", id);
        mentionsRepository.deleteById(id);
    }
}
