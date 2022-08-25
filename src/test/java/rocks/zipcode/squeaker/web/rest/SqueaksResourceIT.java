package rocks.zipcode.squeaker.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import rocks.zipcode.squeaker.IntegrationTest;
import rocks.zipcode.squeaker.domain.Squeaks;
import rocks.zipcode.squeaker.repository.SqueaksRepository;
import rocks.zipcode.squeaker.service.SqueaksService;
import rocks.zipcode.squeaker.service.dto.SqueaksDTO;
import rocks.zipcode.squeaker.service.mapper.SqueaksMapper;

/**
 * Integration tests for the {@link SqueaksResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SqueaksResourceIT {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_LIKES = 1L;
    private static final Long UPDATED_LIKES = 2L;

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_VIDEO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_VIDEO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_VIDEO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_VIDEO_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/squeaks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SqueaksRepository squeaksRepository;

    @Mock
    private SqueaksRepository squeaksRepositoryMock;

    @Autowired
    private SqueaksMapper squeaksMapper;

    @Mock
    private SqueaksService squeaksServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSqueaksMockMvc;

    private Squeaks squeaks;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Squeaks createEntity(EntityManager em) {
        Squeaks squeaks = new Squeaks()
            .content(DEFAULT_CONTENT)
            .createdAt(DEFAULT_CREATED_AT)
            .likes(DEFAULT_LIKES)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .video(DEFAULT_VIDEO)
            .videoContentType(DEFAULT_VIDEO_CONTENT_TYPE);
        return squeaks;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Squeaks createUpdatedEntity(EntityManager em) {
        Squeaks squeaks = new Squeaks()
            .content(UPDATED_CONTENT)
            .createdAt(UPDATED_CREATED_AT)
            .likes(UPDATED_LIKES)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .video(UPDATED_VIDEO)
            .videoContentType(UPDATED_VIDEO_CONTENT_TYPE);
        return squeaks;
    }

    @BeforeEach
    public void initTest() {
        squeaks = createEntity(em);
    }

    @Test
    @Transactional
    void createSqueaks() throws Exception {
        int databaseSizeBeforeCreate = squeaksRepository.findAll().size();
        // Create the Squeaks
        SqueaksDTO squeaksDTO = squeaksMapper.toDto(squeaks);
        restSqueaksMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(squeaksDTO)))
            .andExpect(status().isCreated());

        // Validate the Squeaks in the database
        List<Squeaks> squeaksList = squeaksRepository.findAll();
        assertThat(squeaksList).hasSize(databaseSizeBeforeCreate + 1);
        Squeaks testSqueaks = squeaksList.get(squeaksList.size() - 1);
        assertThat(testSqueaks.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testSqueaks.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSqueaks.getLikes()).isEqualTo(DEFAULT_LIKES);
        assertThat(testSqueaks.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testSqueaks.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testSqueaks.getVideo()).isEqualTo(DEFAULT_VIDEO);
        assertThat(testSqueaks.getVideoContentType()).isEqualTo(DEFAULT_VIDEO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createSqueaksWithExistingId() throws Exception {
        // Create the Squeaks with an existing ID
        squeaks.setId(1L);
        SqueaksDTO squeaksDTO = squeaksMapper.toDto(squeaks);

        int databaseSizeBeforeCreate = squeaksRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSqueaksMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(squeaksDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Squeaks in the database
        List<Squeaks> squeaksList = squeaksRepository.findAll();
        assertThat(squeaksList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = squeaksRepository.findAll().size();
        // set the field null
        squeaks.setContent(null);

        // Create the Squeaks, which fails.
        SqueaksDTO squeaksDTO = squeaksMapper.toDto(squeaks);

        restSqueaksMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(squeaksDTO)))
            .andExpect(status().isBadRequest());

        List<Squeaks> squeaksList = squeaksRepository.findAll();
        assertThat(squeaksList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSqueaks() throws Exception {
        // Initialize the database
        squeaksRepository.saveAndFlush(squeaks);

        // Get all the squeaksList
        restSqueaksMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(squeaks.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].likes").value(hasItem(DEFAULT_LIKES.intValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].videoContentType").value(hasItem(DEFAULT_VIDEO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].video").value(hasItem(Base64Utils.encodeToString(DEFAULT_VIDEO))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSqueaksWithEagerRelationshipsIsEnabled() throws Exception {
        when(squeaksServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSqueaksMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(squeaksServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSqueaksWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(squeaksServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSqueaksMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(squeaksRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSqueaks() throws Exception {
        // Initialize the database
        squeaksRepository.saveAndFlush(squeaks);

        // Get the squeaks
        restSqueaksMockMvc
            .perform(get(ENTITY_API_URL_ID, squeaks.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(squeaks.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.likes").value(DEFAULT_LIKES.intValue()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.videoContentType").value(DEFAULT_VIDEO_CONTENT_TYPE))
            .andExpect(jsonPath("$.video").value(Base64Utils.encodeToString(DEFAULT_VIDEO)));
    }

    @Test
    @Transactional
    void getNonExistingSqueaks() throws Exception {
        // Get the squeaks
        restSqueaksMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSqueaks() throws Exception {
        // Initialize the database
        squeaksRepository.saveAndFlush(squeaks);

        int databaseSizeBeforeUpdate = squeaksRepository.findAll().size();

        // Update the squeaks
        Squeaks updatedSqueaks = squeaksRepository.findById(squeaks.getId()).get();
        // Disconnect from session so that the updates on updatedSqueaks are not directly saved in db
        em.detach(updatedSqueaks);
        updatedSqueaks
            .content(UPDATED_CONTENT)
            .createdAt(UPDATED_CREATED_AT)
            .likes(UPDATED_LIKES)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .video(UPDATED_VIDEO)
            .videoContentType(UPDATED_VIDEO_CONTENT_TYPE);
        SqueaksDTO squeaksDTO = squeaksMapper.toDto(updatedSqueaks);

        restSqueaksMockMvc
            .perform(
                put(ENTITY_API_URL_ID, squeaksDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(squeaksDTO))
            )
            .andExpect(status().isOk());

        // Validate the Squeaks in the database
        List<Squeaks> squeaksList = squeaksRepository.findAll();
        assertThat(squeaksList).hasSize(databaseSizeBeforeUpdate);
        Squeaks testSqueaks = squeaksList.get(squeaksList.size() - 1);
        assertThat(testSqueaks.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testSqueaks.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSqueaks.getLikes()).isEqualTo(UPDATED_LIKES);
        assertThat(testSqueaks.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testSqueaks.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testSqueaks.getVideo()).isEqualTo(UPDATED_VIDEO);
        assertThat(testSqueaks.getVideoContentType()).isEqualTo(UPDATED_VIDEO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingSqueaks() throws Exception {
        int databaseSizeBeforeUpdate = squeaksRepository.findAll().size();
        squeaks.setId(count.incrementAndGet());

        // Create the Squeaks
        SqueaksDTO squeaksDTO = squeaksMapper.toDto(squeaks);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSqueaksMockMvc
            .perform(
                put(ENTITY_API_URL_ID, squeaksDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(squeaksDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Squeaks in the database
        List<Squeaks> squeaksList = squeaksRepository.findAll();
        assertThat(squeaksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSqueaks() throws Exception {
        int databaseSizeBeforeUpdate = squeaksRepository.findAll().size();
        squeaks.setId(count.incrementAndGet());

        // Create the Squeaks
        SqueaksDTO squeaksDTO = squeaksMapper.toDto(squeaks);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSqueaksMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(squeaksDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Squeaks in the database
        List<Squeaks> squeaksList = squeaksRepository.findAll();
        assertThat(squeaksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSqueaks() throws Exception {
        int databaseSizeBeforeUpdate = squeaksRepository.findAll().size();
        squeaks.setId(count.incrementAndGet());

        // Create the Squeaks
        SqueaksDTO squeaksDTO = squeaksMapper.toDto(squeaks);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSqueaksMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(squeaksDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Squeaks in the database
        List<Squeaks> squeaksList = squeaksRepository.findAll();
        assertThat(squeaksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSqueaksWithPatch() throws Exception {
        // Initialize the database
        squeaksRepository.saveAndFlush(squeaks);

        int databaseSizeBeforeUpdate = squeaksRepository.findAll().size();

        // Update the squeaks using partial update
        Squeaks partialUpdatedSqueaks = new Squeaks();
        partialUpdatedSqueaks.setId(squeaks.getId());

        partialUpdatedSqueaks
            .content(UPDATED_CONTENT)
            .likes(UPDATED_LIKES)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restSqueaksMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSqueaks.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSqueaks))
            )
            .andExpect(status().isOk());

        // Validate the Squeaks in the database
        List<Squeaks> squeaksList = squeaksRepository.findAll();
        assertThat(squeaksList).hasSize(databaseSizeBeforeUpdate);
        Squeaks testSqueaks = squeaksList.get(squeaksList.size() - 1);
        assertThat(testSqueaks.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testSqueaks.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSqueaks.getLikes()).isEqualTo(UPDATED_LIKES);
        assertThat(testSqueaks.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testSqueaks.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testSqueaks.getVideo()).isEqualTo(DEFAULT_VIDEO);
        assertThat(testSqueaks.getVideoContentType()).isEqualTo(DEFAULT_VIDEO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateSqueaksWithPatch() throws Exception {
        // Initialize the database
        squeaksRepository.saveAndFlush(squeaks);

        int databaseSizeBeforeUpdate = squeaksRepository.findAll().size();

        // Update the squeaks using partial update
        Squeaks partialUpdatedSqueaks = new Squeaks();
        partialUpdatedSqueaks.setId(squeaks.getId());

        partialUpdatedSqueaks
            .content(UPDATED_CONTENT)
            .createdAt(UPDATED_CREATED_AT)
            .likes(UPDATED_LIKES)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .video(UPDATED_VIDEO)
            .videoContentType(UPDATED_VIDEO_CONTENT_TYPE);

        restSqueaksMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSqueaks.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSqueaks))
            )
            .andExpect(status().isOk());

        // Validate the Squeaks in the database
        List<Squeaks> squeaksList = squeaksRepository.findAll();
        assertThat(squeaksList).hasSize(databaseSizeBeforeUpdate);
        Squeaks testSqueaks = squeaksList.get(squeaksList.size() - 1);
        assertThat(testSqueaks.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testSqueaks.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSqueaks.getLikes()).isEqualTo(UPDATED_LIKES);
        assertThat(testSqueaks.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testSqueaks.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testSqueaks.getVideo()).isEqualTo(UPDATED_VIDEO);
        assertThat(testSqueaks.getVideoContentType()).isEqualTo(UPDATED_VIDEO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingSqueaks() throws Exception {
        int databaseSizeBeforeUpdate = squeaksRepository.findAll().size();
        squeaks.setId(count.incrementAndGet());

        // Create the Squeaks
        SqueaksDTO squeaksDTO = squeaksMapper.toDto(squeaks);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSqueaksMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, squeaksDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(squeaksDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Squeaks in the database
        List<Squeaks> squeaksList = squeaksRepository.findAll();
        assertThat(squeaksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSqueaks() throws Exception {
        int databaseSizeBeforeUpdate = squeaksRepository.findAll().size();
        squeaks.setId(count.incrementAndGet());

        // Create the Squeaks
        SqueaksDTO squeaksDTO = squeaksMapper.toDto(squeaks);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSqueaksMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(squeaksDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Squeaks in the database
        List<Squeaks> squeaksList = squeaksRepository.findAll();
        assertThat(squeaksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSqueaks() throws Exception {
        int databaseSizeBeforeUpdate = squeaksRepository.findAll().size();
        squeaks.setId(count.incrementAndGet());

        // Create the Squeaks
        SqueaksDTO squeaksDTO = squeaksMapper.toDto(squeaks);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSqueaksMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(squeaksDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Squeaks in the database
        List<Squeaks> squeaksList = squeaksRepository.findAll();
        assertThat(squeaksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSqueaks() throws Exception {
        // Initialize the database
        squeaksRepository.saveAndFlush(squeaks);

        int databaseSizeBeforeDelete = squeaksRepository.findAll().size();

        // Delete the squeaks
        restSqueaksMockMvc
            .perform(delete(ENTITY_API_URL_ID, squeaks.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Squeaks> squeaksList = squeaksRepository.findAll();
        assertThat(squeaksList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
