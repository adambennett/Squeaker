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
import rocks.zipcode.squeaker.repository.TagsRepository;
import rocks.zipcode.squeaker.service.TagsService;
import rocks.zipcode.squeaker.service.dto.TagsDTO;
import rocks.zipcode.squeaker.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link rocks.zipcode.squeaker.domain.Tags}.
 */
@RestController
@RequestMapping("/api")
public class TagsResource {

    private final Logger log = LoggerFactory.getLogger(TagsResource.class);

    private static final String ENTITY_NAME = "tags";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TagsService tagsService;

    private final TagsRepository tagsRepository;

    public TagsResource(TagsService tagsService, TagsRepository tagsRepository) {
        this.tagsService = tagsService;
        this.tagsRepository = tagsRepository;
    }

    /**
     * {@code POST  /tags} : Create a new tags.
     *
     * @param tagsDTO the tagsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tagsDTO, or with status {@code 400 (Bad Request)} if the tags has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tags")
    public ResponseEntity<TagsDTO> createTags(@Valid @RequestBody TagsDTO tagsDTO) throws URISyntaxException {
        log.debug("REST request to save Tags : {}", tagsDTO);
        if (tagsDTO.getId() != null) {
            throw new BadRequestAlertException("A new tags cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TagsDTO result = tagsService.save(tagsDTO);
        return ResponseEntity
            .created(new URI("/api/tags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tags/:id} : Updates an existing tags.
     *
     * @param id the id of the tagsDTO to save.
     * @param tagsDTO the tagsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tagsDTO,
     * or with status {@code 400 (Bad Request)} if the tagsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tagsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tags/{id}")
    public ResponseEntity<TagsDTO> updateTags(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TagsDTO tagsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Tags : {}, {}", id, tagsDTO);
        if (tagsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tagsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tagsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TagsDTO result = tagsService.update(tagsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tagsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tags/:id} : Partial updates given fields of an existing tags, field will ignore if it is null
     *
     * @param id the id of the tagsDTO to save.
     * @param tagsDTO the tagsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tagsDTO,
     * or with status {@code 400 (Bad Request)} if the tagsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tagsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tagsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tags/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TagsDTO> partialUpdateTags(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TagsDTO tagsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Tags partially : {}, {}", id, tagsDTO);
        if (tagsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tagsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tagsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TagsDTO> result = tagsService.partialUpdate(tagsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tagsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tags} : get all the tags.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tags in body.
     */
    @GetMapping("/tags")
    public List<TagsDTO> getAllTags() {
        log.debug("REST request to get all Tags");
        return tagsService.findAll();
    }

    /**
     * {@code GET  /tags/:id} : get the "id" tags.
     *
     * @param id the id of the tagsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tagsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tags/{id}")
    public ResponseEntity<TagsDTO> getTags(@PathVariable Long id) {
        log.debug("REST request to get Tags : {}", id);
        Optional<TagsDTO> tagsDTO = tagsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tagsDTO);
    }

    /**
     * {@code DELETE  /tags/:id} : delete the "id" tags.
     *
     * @param id the id of the tagsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tags/{id}")
    public ResponseEntity<Void> deleteTags(@PathVariable Long id) {
        log.debug("REST request to delete Tags : {}", id);
        tagsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
