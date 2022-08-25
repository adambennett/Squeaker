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
import rocks.zipcode.squeaker.repository.UtilizerRepository;
import rocks.zipcode.squeaker.service.UtilizerService;
import rocks.zipcode.squeaker.service.dto.UtilizerDTO;
import rocks.zipcode.squeaker.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link rocks.zipcode.squeaker.domain.Utilizer}.
 */
@RestController
@RequestMapping("/api")
public class UtilizerResource {

    private final Logger log = LoggerFactory.getLogger(UtilizerResource.class);

    private static final String ENTITY_NAME = "utilizer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UtilizerService utilizerService;

    private final UtilizerRepository utilizerRepository;

    public UtilizerResource(UtilizerService utilizerService, UtilizerRepository utilizerRepository) {
        this.utilizerService = utilizerService;
        this.utilizerRepository = utilizerRepository;
    }

    /**
     * {@code POST  /utilizers} : Create a new utilizer.
     *
     * @param utilizerDTO the utilizerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new utilizerDTO, or with status {@code 400 (Bad Request)} if the utilizer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/utilizers")
    public ResponseEntity<UtilizerDTO> createUtilizer(@Valid @RequestBody UtilizerDTO utilizerDTO) throws URISyntaxException {
        log.debug("REST request to save Utilizer : {}", utilizerDTO);
        if (utilizerDTO.getId() != null) {
            throw new BadRequestAlertException("A new utilizer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UtilizerDTO result = utilizerService.save(utilizerDTO);
        return ResponseEntity
            .created(new URI("/api/utilizers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /utilizers/:id} : Updates an existing utilizer.
     *
     * @param id the id of the utilizerDTO to save.
     * @param utilizerDTO the utilizerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated utilizerDTO,
     * or with status {@code 400 (Bad Request)} if the utilizerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the utilizerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/utilizers/{id}")
    public ResponseEntity<UtilizerDTO> updateUtilizer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UtilizerDTO utilizerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Utilizer : {}, {}", id, utilizerDTO);
        if (utilizerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, utilizerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!utilizerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UtilizerDTO result = utilizerService.update(utilizerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, utilizerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /utilizers/:id} : Partial updates given fields of an existing utilizer, field will ignore if it is null
     *
     * @param id the id of the utilizerDTO to save.
     * @param utilizerDTO the utilizerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated utilizerDTO,
     * or with status {@code 400 (Bad Request)} if the utilizerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the utilizerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the utilizerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/utilizers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UtilizerDTO> partialUpdateUtilizer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UtilizerDTO utilizerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Utilizer partially : {}, {}", id, utilizerDTO);
        if (utilizerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, utilizerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!utilizerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UtilizerDTO> result = utilizerService.partialUpdate(utilizerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, utilizerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /utilizers} : get all the utilizers.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of utilizers in body.
     */
    @GetMapping("/utilizers")
    public List<UtilizerDTO> getAllUtilizers(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Utilizers");
        return utilizerService.findAll();
    }

    /**
     * {@code GET  /utilizers/:id} : get the "id" utilizer.
     *
     * @param id the id of the utilizerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the utilizerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/utilizers/{id}")
    public ResponseEntity<UtilizerDTO> getUtilizer(@PathVariable Long id) {
        log.debug("REST request to get Utilizer : {}", id);
        Optional<UtilizerDTO> utilizerDTO = utilizerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(utilizerDTO);
    }

    /**
     * {@code DELETE  /utilizers/:id} : delete the "id" utilizer.
     *
     * @param id the id of the utilizerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/utilizers/{id}")
    public ResponseEntity<Void> deleteUtilizer(@PathVariable Long id) {
        log.debug("REST request to delete Utilizer : {}", id);
        utilizerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
