package rocks.zipcode.squeaker.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rocks.zipcode.squeaker.repository.MentionsRepository;
import rocks.zipcode.squeaker.service.MentionsService;
import rocks.zipcode.squeaker.service.dto.MentionsDTO;
import rocks.zipcode.squeaker.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link rocks.zipcode.squeaker.domain.Mentions}.
 */
@RestController
@RequestMapping("/api")
public class MentionsResource {

    private final Logger log = LoggerFactory.getLogger(MentionsResource.class);

    private static final String ENTITY_NAME = "mentions";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MentionsService mentionsService;

    private final MentionsRepository mentionsRepository;

    public MentionsResource(MentionsService mentionsService, MentionsRepository mentionsRepository) {
        this.mentionsService = mentionsService;
        this.mentionsRepository = mentionsRepository;
    }

    /**
     * {@code POST  /mentions} : Create a new mentions.
     *
     * @param mentionsDTO the mentionsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mentionsDTO, or with status {@code 400 (Bad Request)} if the mentions has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mentions")
    public ResponseEntity<MentionsDTO> createMentions(@Valid @RequestBody MentionsDTO mentionsDTO) throws URISyntaxException {
        log.debug("REST request to save Mentions : {}", mentionsDTO);
        if (mentionsDTO.getId() != null) {
            throw new BadRequestAlertException("A new mentions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MentionsDTO result = mentionsService.save(mentionsDTO);
        return ResponseEntity
            .created(new URI("/api/mentions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mentions/:id} : Updates an existing mentions.
     *
     * @param id the id of the mentionsDTO to save.
     * @param mentionsDTO the mentionsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mentionsDTO,
     * or with status {@code 400 (Bad Request)} if the mentionsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mentionsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mentions/{id}")
    public ResponseEntity<MentionsDTO> updateMentions(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MentionsDTO mentionsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Mentions : {}, {}", id, mentionsDTO);
        if (mentionsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mentionsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mentionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MentionsDTO result = mentionsService.update(mentionsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mentionsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mentions/:id} : Partial updates given fields of an existing mentions, field will ignore if it is null
     *
     * @param id the id of the mentionsDTO to save.
     * @param mentionsDTO the mentionsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mentionsDTO,
     * or with status {@code 400 (Bad Request)} if the mentionsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mentionsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mentionsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mentions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MentionsDTO> partialUpdateMentions(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MentionsDTO mentionsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Mentions partially : {}, {}", id, mentionsDTO);
        if (mentionsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mentionsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mentionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MentionsDTO> result = mentionsService.partialUpdate(mentionsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mentionsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /mentions} : get all the mentions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mentions in body.
     */
    @GetMapping("/mentions")
    public List<MentionsDTO> getAllMentions() {
        log.debug("REST request to get all Mentions");
        return mentionsService.findAll();
    }

    /**
     * {@code GET  /mentions/:id} : get the "id" mentions.
     *
     * @param id the id of the mentionsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mentionsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mentions/{id}")
    public ResponseEntity<MentionsDTO> getMentions(@PathVariable Long id) {
        log.debug("REST request to get Mentions : {}", id);
        Optional<MentionsDTO> mentionsDTO = mentionsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mentionsDTO);
    }

    /**
     * {@code DELETE  /mentions/:id} : delete the "id" mentions.
     *
     * @param id the id of the mentionsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mentions/{id}")
    public ResponseEntity<Void> deleteMentions(@PathVariable Long id) {
        log.debug("REST request to delete Mentions : {}", id);
        mentionsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
