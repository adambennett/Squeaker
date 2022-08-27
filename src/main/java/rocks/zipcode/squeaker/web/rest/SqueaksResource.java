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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import rocks.zipcode.squeaker.repository.SqueaksRepository;
import rocks.zipcode.squeaker.service.SqueaksService;
import rocks.zipcode.squeaker.service.dto.SqueaksDTO;
import rocks.zipcode.squeaker.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link rocks.zipcode.squeaker.domain.Squeaks}.
 */
@RestController
@RequestMapping("/api")
public class SqueaksResource {

    private final Logger log = LoggerFactory.getLogger(SqueaksResource.class);

    private static final String ENTITY_NAME = "squeaks";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SqueaksService squeaksService;

    private final SqueaksRepository squeaksRepository;

    public SqueaksResource(SqueaksService squeaksService, SqueaksRepository squeaksRepository) {
        this.squeaksService = squeaksService;
        this.squeaksRepository = squeaksRepository;
    }

    /**
     * {@code POST  /squeaks} : Create a new squeaks.
     *
     * @param squeaksDTO the squeaksDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new squeaksDTO, or with status {@code 400 (Bad Request)} if the squeaks has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/squeaks")
    public ResponseEntity<SqueaksDTO> createSqueaks(@Valid @RequestBody SqueaksDTO squeaksDTO) throws URISyntaxException {
        log.debug("REST request to save Squeaks : {}", squeaksDTO);
        if (squeaksDTO.getId() != null) {
            throw new BadRequestAlertException("A new squeaks cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SqueaksDTO result = squeaksService.save(squeaksDTO);
        return ResponseEntity
            .created(new URI("/api/squeaks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /squeaks/:id} : Updates an existing squeaks.
     *
     * @param id the id of the squeaksDTO to save.
     * @param squeaksDTO the squeaksDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated squeaksDTO,
     * or with status {@code 400 (Bad Request)} if the squeaksDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the squeaksDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/squeaks/{id}")
    public ResponseEntity<SqueaksDTO> updateSqueaks(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SqueaksDTO squeaksDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Squeaks : {}, {}", id, squeaksDTO);
        if (squeaksDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, squeaksDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!squeaksRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SqueaksDTO result = squeaksService.update(squeaksDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, squeaksDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /squeaks/:id} : Partial updates given fields of an existing squeaks, field will ignore if it is null
     *
     * @param id the id of the squeaksDTO to save.
     * @param squeaksDTO the squeaksDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated squeaksDTO,
     * or with status {@code 400 (Bad Request)} if the squeaksDTO is not valid,
     * or with status {@code 404 (Not Found)} if the squeaksDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the squeaksDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/squeaks/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SqueaksDTO> partialUpdateSqueaks(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SqueaksDTO squeaksDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Squeaks partially : {}, {}", id, squeaksDTO);
        if (squeaksDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, squeaksDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!squeaksRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SqueaksDTO> result = squeaksService.partialUpdate(squeaksDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, squeaksDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /squeaks} : get all the squeaks.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of squeaks in body.
     */
    @GetMapping("/squeaks")
    public ResponseEntity<List<SqueaksDTO>> getAllSqueaks(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Squeaks");
        Page<SqueaksDTO> page;
        if (eagerload) {
            page = squeaksService.findAllWithEagerRelationships(pageable);
        } else {
            page = squeaksService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /squeaks/:id} : get the "id" squeaks.
     *
     * @param id the id of the squeaksDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the squeaksDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/squeaks/{id}")
    @CrossOrigin(origins = { "http://localhost:9000" })
    public ResponseEntity<SqueaksDTO> getSqueaks(@PathVariable Long id) {
        log.debug("REST request to get Squeaks : {}", id);
        Optional<SqueaksDTO> squeaksDTO = squeaksService.findOne(id);
        return ResponseUtil.wrapOrNotFound(squeaksDTO);
    }

    /**
     * {@code DELETE  /squeaks/:id} : delete the "id" squeaks.
     *
     * @param id the id of the squeaksDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/squeaks/{id}")
    public ResponseEntity<Void> deleteSqueaks(@PathVariable Long id) {
        log.debug("REST request to delete Squeaks : {}", id);
        squeaksService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
