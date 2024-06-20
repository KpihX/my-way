package com.myway.myway.web.rest;

import static com.myway.myway.domain.ItinaryAsserts.*;
import static com.myway.myway.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myway.myway.IntegrationTest;
import com.myway.myway.domain.Itinary;
import com.myway.myway.repository.EntityManager;
import com.myway.myway.repository.ItinaryRepository;
import com.myway.myway.service.dto.ItinaryDTO;
import com.myway.myway.service.mapper.ItinaryMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link ItinaryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ItinaryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PREF_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PREF_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_TOTAL_DISTANCE = 1D;
    private static final Double UPDATED_TOTAL_DISTANCE = 2D;

    private static final Double DEFAULT_TOTAL_TIME = 1D;
    private static final Double UPDATED_TOTAL_TIME = 2D;

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/itinaries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ItinaryRepository itinaryRepository;

    @Autowired
    private ItinaryMapper itinaryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Itinary itinary;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Itinary createEntity(EntityManager em) {
        Itinary itinary = new Itinary()
            .name(DEFAULT_NAME)
            .prefName(DEFAULT_PREF_NAME)
            .description(DEFAULT_DESCRIPTION)
            .totalDistance(DEFAULT_TOTAL_DISTANCE)
            .totalTime(DEFAULT_TOTAL_TIME)
            .image(DEFAULT_IMAGE);
        return itinary;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Itinary createUpdatedEntity(EntityManager em) {
        Itinary itinary = new Itinary()
            .name(UPDATED_NAME)
            .prefName(UPDATED_PREF_NAME)
            .description(UPDATED_DESCRIPTION)
            .totalDistance(UPDATED_TOTAL_DISTANCE)
            .totalTime(UPDATED_TOTAL_TIME)
            .image(UPDATED_IMAGE);
        return itinary;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Itinary.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        itinary = createEntity(em);
    }

    @Test
    void createItinary() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Itinary
        ItinaryDTO itinaryDTO = itinaryMapper.toDto(itinary);
        var returnedItinaryDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(itinaryDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ItinaryDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Itinary in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedItinary = itinaryMapper.toEntity(returnedItinaryDTO);
        assertItinaryUpdatableFieldsEquals(returnedItinary, getPersistedItinary(returnedItinary));
    }

    @Test
    void createItinaryWithExistingId() throws Exception {
        // Create the Itinary with an existing ID
        itinary.setId(1L);
        ItinaryDTO itinaryDTO = itinaryMapper.toDto(itinary);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(itinaryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Itinary in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        itinary.setName(null);

        // Create the Itinary, which fails.
        ItinaryDTO itinaryDTO = itinaryMapper.toDto(itinary);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(itinaryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkTotalDistanceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        itinary.setTotalDistance(null);

        // Create the Itinary, which fails.
        ItinaryDTO itinaryDTO = itinaryMapper.toDto(itinary);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(itinaryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkTotalTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        itinary.setTotalTime(null);

        // Create the Itinary, which fails.
        ItinaryDTO itinaryDTO = itinaryMapper.toDto(itinary);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(itinaryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllItinariesAsStream() {
        // Initialize the database
        itinaryRepository.save(itinary).block();

        List<Itinary> itinaryList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ItinaryDTO.class)
            .getResponseBody()
            .map(itinaryMapper::toEntity)
            .filter(itinary::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(itinaryList).isNotNull();
        assertThat(itinaryList).hasSize(1);
        Itinary testItinary = itinaryList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertItinaryAllPropertiesEquals(itinary, testItinary);
        assertItinaryUpdatableFieldsEquals(itinary, testItinary);
    }

    @Test
    void getAllItinaries() {
        // Initialize the database
        itinaryRepository.save(itinary).block();

        // Get all the itinaryList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(itinary.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].prefName")
            .value(hasItem(DEFAULT_PREF_NAME))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].totalDistance")
            .value(hasItem(DEFAULT_TOTAL_DISTANCE.doubleValue()))
            .jsonPath("$.[*].totalTime")
            .value(hasItem(DEFAULT_TOTAL_TIME.doubleValue()))
            .jsonPath("$.[*].image")
            .value(hasItem(DEFAULT_IMAGE));
    }

    @Test
    void getItinary() {
        // Initialize the database
        itinaryRepository.save(itinary).block();

        // Get the itinary
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, itinary.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(itinary.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.prefName")
            .value(is(DEFAULT_PREF_NAME))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.totalDistance")
            .value(is(DEFAULT_TOTAL_DISTANCE.doubleValue()))
            .jsonPath("$.totalTime")
            .value(is(DEFAULT_TOTAL_TIME.doubleValue()))
            .jsonPath("$.image")
            .value(is(DEFAULT_IMAGE));
    }

    @Test
    void getNonExistingItinary() {
        // Get the itinary
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingItinary() throws Exception {
        // Initialize the database
        itinaryRepository.save(itinary).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the itinary
        Itinary updatedItinary = itinaryRepository.findById(itinary.getId()).block();
        updatedItinary
            .name(UPDATED_NAME)
            .prefName(UPDATED_PREF_NAME)
            .description(UPDATED_DESCRIPTION)
            .totalDistance(UPDATED_TOTAL_DISTANCE)
            .totalTime(UPDATED_TOTAL_TIME)
            .image(UPDATED_IMAGE);
        ItinaryDTO itinaryDTO = itinaryMapper.toDto(updatedItinary);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, itinaryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(itinaryDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Itinary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedItinaryToMatchAllProperties(updatedItinary);
    }

    @Test
    void putNonExistingItinary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        itinary.setId(longCount.incrementAndGet());

        // Create the Itinary
        ItinaryDTO itinaryDTO = itinaryMapper.toDto(itinary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, itinaryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(itinaryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Itinary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchItinary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        itinary.setId(longCount.incrementAndGet());

        // Create the Itinary
        ItinaryDTO itinaryDTO = itinaryMapper.toDto(itinary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(itinaryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Itinary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamItinary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        itinary.setId(longCount.incrementAndGet());

        // Create the Itinary
        ItinaryDTO itinaryDTO = itinaryMapper.toDto(itinary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(itinaryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Itinary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateItinaryWithPatch() throws Exception {
        // Initialize the database
        itinaryRepository.save(itinary).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the itinary using partial update
        Itinary partialUpdatedItinary = new Itinary();
        partialUpdatedItinary.setId(itinary.getId());

        partialUpdatedItinary
            .prefName(UPDATED_PREF_NAME)
            .description(UPDATED_DESCRIPTION)
            .totalDistance(UPDATED_TOTAL_DISTANCE)
            .image(UPDATED_IMAGE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedItinary.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedItinary))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Itinary in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertItinaryUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedItinary, itinary), getPersistedItinary(itinary));
    }

    @Test
    void fullUpdateItinaryWithPatch() throws Exception {
        // Initialize the database
        itinaryRepository.save(itinary).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the itinary using partial update
        Itinary partialUpdatedItinary = new Itinary();
        partialUpdatedItinary.setId(itinary.getId());

        partialUpdatedItinary
            .name(UPDATED_NAME)
            .prefName(UPDATED_PREF_NAME)
            .description(UPDATED_DESCRIPTION)
            .totalDistance(UPDATED_TOTAL_DISTANCE)
            .totalTime(UPDATED_TOTAL_TIME)
            .image(UPDATED_IMAGE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedItinary.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedItinary))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Itinary in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertItinaryUpdatableFieldsEquals(partialUpdatedItinary, getPersistedItinary(partialUpdatedItinary));
    }

    @Test
    void patchNonExistingItinary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        itinary.setId(longCount.incrementAndGet());

        // Create the Itinary
        ItinaryDTO itinaryDTO = itinaryMapper.toDto(itinary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, itinaryDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(itinaryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Itinary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchItinary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        itinary.setId(longCount.incrementAndGet());

        // Create the Itinary
        ItinaryDTO itinaryDTO = itinaryMapper.toDto(itinary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(itinaryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Itinary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamItinary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        itinary.setId(longCount.incrementAndGet());

        // Create the Itinary
        ItinaryDTO itinaryDTO = itinaryMapper.toDto(itinary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(itinaryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Itinary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteItinary() {
        // Initialize the database
        itinaryRepository.save(itinary).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the itinary
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, itinary.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return itinaryRepository.count().block();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Itinary getPersistedItinary(Itinary itinary) {
        return itinaryRepository.findById(itinary.getId()).block();
    }

    protected void assertPersistedItinaryToMatchAllProperties(Itinary expectedItinary) {
        // Test fails because reactive api returns an empty object instead of null
        // assertItinaryAllPropertiesEquals(expectedItinary, getPersistedItinary(expectedItinary));
        assertItinaryUpdatableFieldsEquals(expectedItinary, getPersistedItinary(expectedItinary));
    }

    protected void assertPersistedItinaryToMatchUpdatableProperties(Itinary expectedItinary) {
        // Test fails because reactive api returns an empty object instead of null
        // assertItinaryAllUpdatablePropertiesEquals(expectedItinary, getPersistedItinary(expectedItinary));
        assertItinaryUpdatableFieldsEquals(expectedItinary, getPersistedItinary(expectedItinary));
    }
}
