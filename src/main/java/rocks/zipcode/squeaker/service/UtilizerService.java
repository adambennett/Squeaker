package rocks.zipcode.squeaker.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocks.zipcode.squeaker.domain.Utilizer;
import rocks.zipcode.squeaker.repository.UtilizerRepository;
import rocks.zipcode.squeaker.service.dto.UtilizerDTO;
import rocks.zipcode.squeaker.service.mapper.UtilizerMapper;

/**
 * Service Implementation for managing {@link Utilizer}.
 */
@Service
@Transactional
public class UtilizerService {

    private final Logger log = LoggerFactory.getLogger(UtilizerService.class);

    private final UtilizerRepository utilizerRepository;

    private final UtilizerMapper utilizerMapper;

    public UtilizerService(UtilizerRepository utilizerRepository, UtilizerMapper utilizerMapper) {
        this.utilizerRepository = utilizerRepository;
        this.utilizerMapper = utilizerMapper;
    }

    /**
     * Save a utilizer.
     *
     * @param utilizerDTO the entity to save.
     * @return the persisted entity.
     */
    public UtilizerDTO save(UtilizerDTO utilizerDTO) {
        log.debug("Request to save Utilizer : {}", utilizerDTO);
        Utilizer utilizer = utilizerMapper.toEntity(utilizerDTO);
        utilizer = utilizerRepository.save(utilizer);
        return utilizerMapper.toDto(utilizer);
    }

    /**
     * Update a utilizer.
     *
     * @param utilizerDTO the entity to save.
     * @return the persisted entity.
     */
    public UtilizerDTO update(UtilizerDTO utilizerDTO) {
        log.debug("Request to save Utilizer : {}", utilizerDTO);
        Utilizer utilizer = utilizerMapper.toEntity(utilizerDTO);
        utilizer = utilizerRepository.save(utilizer);
        return utilizerMapper.toDto(utilizer);
    }

    /**
     * Partially update a utilizer.
     *
     * @param utilizerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UtilizerDTO> partialUpdate(UtilizerDTO utilizerDTO) {
        log.debug("Request to partially update Utilizer : {}", utilizerDTO);

        return utilizerRepository
            .findById(utilizerDTO.getId())
            .map(existingUtilizer -> {
                utilizerMapper.partialUpdate(existingUtilizer, utilizerDTO);

                return existingUtilizer;
            })
            .map(utilizerRepository::save)
            .map(utilizerMapper::toDto);
    }

    /**
     * Get all the utilizers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UtilizerDTO> findAll() {
        log.debug("Request to get all Utilizers");
        return utilizerRepository.findAll().stream().map(utilizerMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the utilizers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<UtilizerDTO> findAllWithEagerRelationships(Pageable pageable) {
        return utilizerRepository.findAllWithEagerRelationships(pageable).map(utilizerMapper::toDto);
    }

    /**
     * Get one utilizer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UtilizerDTO> findOne(Long id) {
        log.debug("Request to get Utilizer : {}", id);
        return utilizerRepository.findOneWithEagerRelationships(id).map(utilizerMapper::toDto);
    }

    /**
     * Delete the utilizer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Utilizer : {}", id);
        utilizerRepository.deleteById(id);
    }

    public Utilizer createUtilizer(UtilizerDTO utilizerDTO) {
        return null;
        /* User user = new User();
        user.setLogin(uutilizerDTO.getLogin().toLowerCase());
        user.setFirstName(uutilizerDTO.getFirstName());
        user.setLastName(uutilizerDTO.getLastName());
        if (uutilizerDTO.getEmail() != null) {
            user.setEmail(uutilizerDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(uutilizerDTO.getImageUrl());
        if (uutilizerDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(uutilizerDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (uutilizerDTO.getAuthorities() != null) {
            Set<Authority> authorities = uutilizerDTO
                .getAuthorities()
                .stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        return user;*/
    }
}
