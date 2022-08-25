package rocks.zipcode.squeaker.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import rocks.zipcode.squeaker.IntegrationTest;
import rocks.zipcode.squeaker.domain.Utilizer;
import rocks.zipcode.squeaker.repository.UtilizerRepository;
import rocks.zipcode.squeaker.service.UtilizerService;
import rocks.zipcode.squeaker.service.dto.UtilizerDTO;
import rocks.zipcode.squeaker.service.mapper.UtilizerMapper;

/**
 * Integration tests for the {@link UtilizerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UtilizerResourceIT {

    private static final String DEFAULT_HANDLE = "AAAAAAAAAA";
    private static final String UPDATED_HANDLE = "BBBBBBBBBB";

    private static final Long DEFAULT_FOLLOWERS = 1L;
    private static final Long UPDATED_FOLLOWERS = 2L;

    private static final Long DEFAULT_FOLLOWING = 1L;
    private static final Long UPDATED_FOLLOWING = 2L;

    private static final String ENTITY_API_URL = "/api/utilizers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UtilizerRepository utilizerRepository;

    @Mock
    private UtilizerRepository utilizerRepositoryMock;

    @Autowired
    private UtilizerMapper utilizerMapper;

    @Mock
    private UtilizerService utilizerServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUtilizerMockMvc;

    private Utilizer utilizer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Utilizer createEntity(EntityManager em) {
        Utilizer utilizer = new Utilizer().handle(DEFAULT_HANDLE).followers(DEFAULT_FOLLOWERS).following(DEFAULT_FOLLOWING);
        return utilizer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Utilizer createUpdatedEntity(EntityManager em) {
        Utilizer utilizer = new Utilizer().handle(UPDATED_HANDLE).followers(UPDATED_FOLLOWERS).following(UPDATED_FOLLOWING);
        return utilizer;
    }

    @BeforeEach
    public void initTest() {
        utilizer = createEntity(em);
    }

    @Test
    @Transactional
    void createUtilizer() throws Exception {
        int databaseSizeBeforeCreate = utilizerRepository.findAll().size();
        // Create the Utilizer
        UtilizerDTO utilizerDTO = utilizerMapper.toDto(utilizer);
        restUtilizerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(utilizerDTO)))
            .andExpect(status().isCreated());

        // Validate the Utilizer in the database
        List<Utilizer> utilizerList = utilizerRepository.findAll();
        assertThat(utilizerList).hasSize(databaseSizeBeforeCreate + 1);
        Utilizer testUtilizer = utilizerList.get(utilizerList.size() - 1);
        assertThat(testUtilizer.getHandle()).isEqualTo(DEFAULT_HANDLE);
        assertThat(testUtilizer.getFollowers()).isEqualTo(DEFAULT_FOLLOWERS);
        assertThat(testUtilizer.getFollowing()).isEqualTo(DEFAULT_FOLLOWING);
    }

    @Test
    @Transactional
    void createUtilizerWithExistingId() throws Exception {
        // Create the Utilizer with an existing ID
        utilizer.setId(1L);
        UtilizerDTO utilizerDTO = utilizerMapper.toDto(utilizer);

        int databaseSizeBeforeCreate = utilizerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUtilizerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(utilizerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Utilizer in the database
        List<Utilizer> utilizerList = utilizerRepository.findAll();
        assertThat(utilizerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkHandleIsRequired() throws Exception {
        int databaseSizeBeforeTest = utilizerRepository.findAll().size();
        // set the field null
        utilizer.setHandle(null);

        // Create the Utilizer, which fails.
        UtilizerDTO utilizerDTO = utilizerMapper.toDto(utilizer);

        restUtilizerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(utilizerDTO)))
            .andExpect(status().isBadRequest());

        List<Utilizer> utilizerList = utilizerRepository.findAll();
        assertThat(utilizerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUtilizers() throws Exception {
        // Initialize the database
        utilizerRepository.saveAndFlush(utilizer);

        // Get all the utilizerList
        restUtilizerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(utilizer.getId().intValue())))
            .andExpect(jsonPath("$.[*].handle").value(hasItem(DEFAULT_HANDLE)))
            .andExpect(jsonPath("$.[*].followers").value(hasItem(DEFAULT_FOLLOWERS.intValue())))
            .andExpect(jsonPath("$.[*].following").value(hasItem(DEFAULT_FOLLOWING.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUtilizersWithEagerRelationshipsIsEnabled() throws Exception {
        when(utilizerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUtilizerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(utilizerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUtilizersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(utilizerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUtilizerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(utilizerRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUtilizer() throws Exception {
        // Initialize the database
        utilizerRepository.saveAndFlush(utilizer);

        // Get the utilizer
        restUtilizerMockMvc
            .perform(get(ENTITY_API_URL_ID, utilizer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(utilizer.getId().intValue()))
            .andExpect(jsonPath("$.handle").value(DEFAULT_HANDLE))
            .andExpect(jsonPath("$.followers").value(DEFAULT_FOLLOWERS.intValue()))
            .andExpect(jsonPath("$.following").value(DEFAULT_FOLLOWING.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingUtilizer() throws Exception {
        // Get the utilizer
        restUtilizerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUtilizer() throws Exception {
        // Initialize the database
        utilizerRepository.saveAndFlush(utilizer);

        int databaseSizeBeforeUpdate = utilizerRepository.findAll().size();

        // Update the utilizer
        Utilizer updatedUtilizer = utilizerRepository.findById(utilizer.getId()).get();
        // Disconnect from session so that the updates on updatedUtilizer are not directly saved in db
        em.detach(updatedUtilizer);
        updatedUtilizer.handle(UPDATED_HANDLE).followers(UPDATED_FOLLOWERS).following(UPDATED_FOLLOWING);
        UtilizerDTO utilizerDTO = utilizerMapper.toDto(updatedUtilizer);

        restUtilizerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, utilizerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utilizerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Utilizer in the database
        List<Utilizer> utilizerList = utilizerRepository.findAll();
        assertThat(utilizerList).hasSize(databaseSizeBeforeUpdate);
        Utilizer testUtilizer = utilizerList.get(utilizerList.size() - 1);
        assertThat(testUtilizer.getHandle()).isEqualTo(UPDATED_HANDLE);
        assertThat(testUtilizer.getFollowers()).isEqualTo(UPDATED_FOLLOWERS);
        assertThat(testUtilizer.getFollowing()).isEqualTo(UPDATED_FOLLOWING);
    }

    @Test
    @Transactional
    void putNonExistingUtilizer() throws Exception {
        int databaseSizeBeforeUpdate = utilizerRepository.findAll().size();
        utilizer.setId(count.incrementAndGet());

        // Create the Utilizer
        UtilizerDTO utilizerDTO = utilizerMapper.toDto(utilizer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUtilizerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, utilizerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utilizerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utilizer in the database
        List<Utilizer> utilizerList = utilizerRepository.findAll();
        assertThat(utilizerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUtilizer() throws Exception {
        int databaseSizeBeforeUpdate = utilizerRepository.findAll().size();
        utilizer.setId(count.incrementAndGet());

        // Create the Utilizer
        UtilizerDTO utilizerDTO = utilizerMapper.toDto(utilizer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilizerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utilizerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utilizer in the database
        List<Utilizer> utilizerList = utilizerRepository.findAll();
        assertThat(utilizerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUtilizer() throws Exception {
        int databaseSizeBeforeUpdate = utilizerRepository.findAll().size();
        utilizer.setId(count.incrementAndGet());

        // Create the Utilizer
        UtilizerDTO utilizerDTO = utilizerMapper.toDto(utilizer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilizerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(utilizerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Utilizer in the database
        List<Utilizer> utilizerList = utilizerRepository.findAll();
        assertThat(utilizerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUtilizerWithPatch() throws Exception {
        // Initialize the database
        utilizerRepository.saveAndFlush(utilizer);

        int databaseSizeBeforeUpdate = utilizerRepository.findAll().size();

        // Update the utilizer using partial update
        Utilizer partialUpdatedUtilizer = new Utilizer();
        partialUpdatedUtilizer.setId(utilizer.getId());

        partialUpdatedUtilizer.following(UPDATED_FOLLOWING);

        restUtilizerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUtilizer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUtilizer))
            )
            .andExpect(status().isOk());

        // Validate the Utilizer in the database
        List<Utilizer> utilizerList = utilizerRepository.findAll();
        assertThat(utilizerList).hasSize(databaseSizeBeforeUpdate);
        Utilizer testUtilizer = utilizerList.get(utilizerList.size() - 1);
        assertThat(testUtilizer.getHandle()).isEqualTo(DEFAULT_HANDLE);
        assertThat(testUtilizer.getFollowers()).isEqualTo(DEFAULT_FOLLOWERS);
        assertThat(testUtilizer.getFollowing()).isEqualTo(UPDATED_FOLLOWING);
    }

    @Test
    @Transactional
    void fullUpdateUtilizerWithPatch() throws Exception {
        // Initialize the database
        utilizerRepository.saveAndFlush(utilizer);

        int databaseSizeBeforeUpdate = utilizerRepository.findAll().size();

        // Update the utilizer using partial update
        Utilizer partialUpdatedUtilizer = new Utilizer();
        partialUpdatedUtilizer.setId(utilizer.getId());

        partialUpdatedUtilizer.handle(UPDATED_HANDLE).followers(UPDATED_FOLLOWERS).following(UPDATED_FOLLOWING);

        restUtilizerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUtilizer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUtilizer))
            )
            .andExpect(status().isOk());

        // Validate the Utilizer in the database
        List<Utilizer> utilizerList = utilizerRepository.findAll();
        assertThat(utilizerList).hasSize(databaseSizeBeforeUpdate);
        Utilizer testUtilizer = utilizerList.get(utilizerList.size() - 1);
        assertThat(testUtilizer.getHandle()).isEqualTo(UPDATED_HANDLE);
        assertThat(testUtilizer.getFollowers()).isEqualTo(UPDATED_FOLLOWERS);
        assertThat(testUtilizer.getFollowing()).isEqualTo(UPDATED_FOLLOWING);
    }

    @Test
    @Transactional
    void patchNonExistingUtilizer() throws Exception {
        int databaseSizeBeforeUpdate = utilizerRepository.findAll().size();
        utilizer.setId(count.incrementAndGet());

        // Create the Utilizer
        UtilizerDTO utilizerDTO = utilizerMapper.toDto(utilizer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUtilizerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, utilizerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(utilizerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utilizer in the database
        List<Utilizer> utilizerList = utilizerRepository.findAll();
        assertThat(utilizerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUtilizer() throws Exception {
        int databaseSizeBeforeUpdate = utilizerRepository.findAll().size();
        utilizer.setId(count.incrementAndGet());

        // Create the Utilizer
        UtilizerDTO utilizerDTO = utilizerMapper.toDto(utilizer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilizerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(utilizerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utilizer in the database
        List<Utilizer> utilizerList = utilizerRepository.findAll();
        assertThat(utilizerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUtilizer() throws Exception {
        int databaseSizeBeforeUpdate = utilizerRepository.findAll().size();
        utilizer.setId(count.incrementAndGet());

        // Create the Utilizer
        UtilizerDTO utilizerDTO = utilizerMapper.toDto(utilizer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilizerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(utilizerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Utilizer in the database
        List<Utilizer> utilizerList = utilizerRepository.findAll();
        assertThat(utilizerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUtilizer() throws Exception {
        // Initialize the database
        utilizerRepository.saveAndFlush(utilizer);

        int databaseSizeBeforeDelete = utilizerRepository.findAll().size();

        // Delete the utilizer
        restUtilizerMockMvc
            .perform(delete(ENTITY_API_URL_ID, utilizer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Utilizer> utilizerList = utilizerRepository.findAll();
        assertThat(utilizerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
