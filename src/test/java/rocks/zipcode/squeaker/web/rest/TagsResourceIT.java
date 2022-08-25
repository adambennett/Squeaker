package rocks.zipcode.squeaker.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import rocks.zipcode.squeaker.IntegrationTest;
import rocks.zipcode.squeaker.domain.Tags;
import rocks.zipcode.squeaker.repository.TagsRepository;
import rocks.zipcode.squeaker.service.dto.TagsDTO;
import rocks.zipcode.squeaker.service.mapper.TagsMapper;

/**
 * Integration tests for the {@link TagsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TagsResourceIT {

    private static final String DEFAULT_HASHTAG = "AAAAAAAAAA";
    private static final String UPDATED_HASHTAG = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private TagsMapper tagsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTagsMockMvc;

    private Tags tags;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tags createEntity(EntityManager em) {
        Tags tags = new Tags().hashtag(DEFAULT_HASHTAG);
        return tags;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tags createUpdatedEntity(EntityManager em) {
        Tags tags = new Tags().hashtag(UPDATED_HASHTAG);
        return tags;
    }

    @BeforeEach
    public void initTest() {
        tags = createEntity(em);
    }

    @Test
    @Transactional
    void createTags() throws Exception {
        int databaseSizeBeforeCreate = tagsRepository.findAll().size();
        // Create the Tags
        TagsDTO tagsDTO = tagsMapper.toDto(tags);
        restTagsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tagsDTO)))
            .andExpect(status().isCreated());

        // Validate the Tags in the database
        List<Tags> tagsList = tagsRepository.findAll();
        assertThat(tagsList).hasSize(databaseSizeBeforeCreate + 1);
        Tags testTags = tagsList.get(tagsList.size() - 1);
        assertThat(testTags.getHashtag()).isEqualTo(DEFAULT_HASHTAG);
    }

    @Test
    @Transactional
    void createTagsWithExistingId() throws Exception {
        // Create the Tags with an existing ID
        tags.setId(1L);
        TagsDTO tagsDTO = tagsMapper.toDto(tags);

        int databaseSizeBeforeCreate = tagsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTagsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tagsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tags in the database
        List<Tags> tagsList = tagsRepository.findAll();
        assertThat(tagsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkHashtagIsRequired() throws Exception {
        int databaseSizeBeforeTest = tagsRepository.findAll().size();
        // set the field null
        tags.setHashtag(null);

        // Create the Tags, which fails.
        TagsDTO tagsDTO = tagsMapper.toDto(tags);

        restTagsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tagsDTO)))
            .andExpect(status().isBadRequest());

        List<Tags> tagsList = tagsRepository.findAll();
        assertThat(tagsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTags() throws Exception {
        // Initialize the database
        tagsRepository.saveAndFlush(tags);

        // Get all the tagsList
        restTagsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tags.getId().intValue())))
            .andExpect(jsonPath("$.[*].hashtag").value(hasItem(DEFAULT_HASHTAG)));
    }

    @Test
    @Transactional
    void getTags() throws Exception {
        // Initialize the database
        tagsRepository.saveAndFlush(tags);

        // Get the tags
        restTagsMockMvc
            .perform(get(ENTITY_API_URL_ID, tags.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tags.getId().intValue()))
            .andExpect(jsonPath("$.hashtag").value(DEFAULT_HASHTAG));
    }

    @Test
    @Transactional
    void getNonExistingTags() throws Exception {
        // Get the tags
        restTagsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTags() throws Exception {
        // Initialize the database
        tagsRepository.saveAndFlush(tags);

        int databaseSizeBeforeUpdate = tagsRepository.findAll().size();

        // Update the tags
        Tags updatedTags = tagsRepository.findById(tags.getId()).get();
        // Disconnect from session so that the updates on updatedTags are not directly saved in db
        em.detach(updatedTags);
        updatedTags.hashtag(UPDATED_HASHTAG);
        TagsDTO tagsDTO = tagsMapper.toDto(updatedTags);

        restTagsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tagsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tagsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Tags in the database
        List<Tags> tagsList = tagsRepository.findAll();
        assertThat(tagsList).hasSize(databaseSizeBeforeUpdate);
        Tags testTags = tagsList.get(tagsList.size() - 1);
        assertThat(testTags.getHashtag()).isEqualTo(UPDATED_HASHTAG);
    }

    @Test
    @Transactional
    void putNonExistingTags() throws Exception {
        int databaseSizeBeforeUpdate = tagsRepository.findAll().size();
        tags.setId(count.incrementAndGet());

        // Create the Tags
        TagsDTO tagsDTO = tagsMapper.toDto(tags);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTagsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tagsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tagsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tags in the database
        List<Tags> tagsList = tagsRepository.findAll();
        assertThat(tagsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTags() throws Exception {
        int databaseSizeBeforeUpdate = tagsRepository.findAll().size();
        tags.setId(count.incrementAndGet());

        // Create the Tags
        TagsDTO tagsDTO = tagsMapper.toDto(tags);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tagsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tags in the database
        List<Tags> tagsList = tagsRepository.findAll();
        assertThat(tagsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTags() throws Exception {
        int databaseSizeBeforeUpdate = tagsRepository.findAll().size();
        tags.setId(count.incrementAndGet());

        // Create the Tags
        TagsDTO tagsDTO = tagsMapper.toDto(tags);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tagsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tags in the database
        List<Tags> tagsList = tagsRepository.findAll();
        assertThat(tagsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTagsWithPatch() throws Exception {
        // Initialize the database
        tagsRepository.saveAndFlush(tags);

        int databaseSizeBeforeUpdate = tagsRepository.findAll().size();

        // Update the tags using partial update
        Tags partialUpdatedTags = new Tags();
        partialUpdatedTags.setId(tags.getId());

        restTagsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTags.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTags))
            )
            .andExpect(status().isOk());

        // Validate the Tags in the database
        List<Tags> tagsList = tagsRepository.findAll();
        assertThat(tagsList).hasSize(databaseSizeBeforeUpdate);
        Tags testTags = tagsList.get(tagsList.size() - 1);
        assertThat(testTags.getHashtag()).isEqualTo(DEFAULT_HASHTAG);
    }

    @Test
    @Transactional
    void fullUpdateTagsWithPatch() throws Exception {
        // Initialize the database
        tagsRepository.saveAndFlush(tags);

        int databaseSizeBeforeUpdate = tagsRepository.findAll().size();

        // Update the tags using partial update
        Tags partialUpdatedTags = new Tags();
        partialUpdatedTags.setId(tags.getId());

        partialUpdatedTags.hashtag(UPDATED_HASHTAG);

        restTagsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTags.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTags))
            )
            .andExpect(status().isOk());

        // Validate the Tags in the database
        List<Tags> tagsList = tagsRepository.findAll();
        assertThat(tagsList).hasSize(databaseSizeBeforeUpdate);
        Tags testTags = tagsList.get(tagsList.size() - 1);
        assertThat(testTags.getHashtag()).isEqualTo(UPDATED_HASHTAG);
    }

    @Test
    @Transactional
    void patchNonExistingTags() throws Exception {
        int databaseSizeBeforeUpdate = tagsRepository.findAll().size();
        tags.setId(count.incrementAndGet());

        // Create the Tags
        TagsDTO tagsDTO = tagsMapper.toDto(tags);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTagsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tagsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tagsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tags in the database
        List<Tags> tagsList = tagsRepository.findAll();
        assertThat(tagsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTags() throws Exception {
        int databaseSizeBeforeUpdate = tagsRepository.findAll().size();
        tags.setId(count.incrementAndGet());

        // Create the Tags
        TagsDTO tagsDTO = tagsMapper.toDto(tags);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tagsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tags in the database
        List<Tags> tagsList = tagsRepository.findAll();
        assertThat(tagsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTags() throws Exception {
        int databaseSizeBeforeUpdate = tagsRepository.findAll().size();
        tags.setId(count.incrementAndGet());

        // Create the Tags
        TagsDTO tagsDTO = tagsMapper.toDto(tags);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tagsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tags in the database
        List<Tags> tagsList = tagsRepository.findAll();
        assertThat(tagsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTags() throws Exception {
        // Initialize the database
        tagsRepository.saveAndFlush(tags);

        int databaseSizeBeforeDelete = tagsRepository.findAll().size();

        // Delete the tags
        restTagsMockMvc
            .perform(delete(ENTITY_API_URL_ID, tags.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tags> tagsList = tagsRepository.findAll();
        assertThat(tagsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
