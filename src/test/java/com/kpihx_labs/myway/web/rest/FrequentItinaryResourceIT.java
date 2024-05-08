package com.kpihx_labs.myway.web.rest;

import static com.kpihx_labs.myway.domain.FrequentItinaryAsserts.*;
import static com.kpihx_labs.myway.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpihx_labs.myway.IntegrationTest;
import com.kpihx_labs.myway.domain.FrequentItinary;
import com.kpihx_labs.myway.repository.EntityManager;
import com.kpihx_labs.myway.repository.FrequentItinaryRepository;
import com.kpihx_labs.myway.service.dto.FrequentItinaryDTO;
import com.kpihx_labs.myway.service.mapper.FrequentItinaryMapper;
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
 * Integration tests for the {@link FrequentItinaryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class FrequentItinaryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/frequent-itinaries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FrequentItinaryRepository frequentItinaryRepository;

    @Autowired
    private FrequentItinaryMapper frequentItinaryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private FrequentItinary frequentItinary;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FrequentItinary createEntity(EntityManager em) {
        FrequentItinary frequentItinary = new FrequentItinary().name(DEFAULT_NAME);
        return frequentItinary;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FrequentItinary createUpdatedEntity(EntityManager em) {
        FrequentItinary frequentItinary = new FrequentItinary().name(UPDATED_NAME);
        return frequentItinary;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(FrequentItinary.class).block();
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
        frequentItinary = createEntity(em);
    }

    @Test
    void createFrequentItinary() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FrequentItinary
        FrequentItinaryDTO frequentItinaryDTO = frequentItinaryMapper.toDto(frequentItinary);
        var returnedFrequentItinaryDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(frequentItinaryDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(FrequentItinaryDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the FrequentItinary in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFrequentItinary = frequentItinaryMapper.toEntity(returnedFrequentItinaryDTO);
        assertFrequentItinaryUpdatableFieldsEquals(returnedFrequentItinary, getPersistedFrequentItinary(returnedFrequentItinary));
    }

    @Test
    void createFrequentItinaryWithExistingId() throws Exception {
        // Create the FrequentItinary with an existing ID
        frequentItinary.setId(1L);
        FrequentItinaryDTO frequentItinaryDTO = frequentItinaryMapper.toDto(frequentItinary);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(frequentItinaryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FrequentItinary in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        frequentItinary.setName(null);

        // Create the FrequentItinary, which fails.
        FrequentItinaryDTO frequentItinaryDTO = frequentItinaryMapper.toDto(frequentItinary);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(frequentItinaryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllFrequentItinariesAsStream() {
        // Initialize the database
        frequentItinaryRepository.save(frequentItinary).block();

        List<FrequentItinary> frequentItinaryList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(FrequentItinaryDTO.class)
            .getResponseBody()
            .map(frequentItinaryMapper::toEntity)
            .filter(frequentItinary::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(frequentItinaryList).isNotNull();
        assertThat(frequentItinaryList).hasSize(1);
        FrequentItinary testFrequentItinary = frequentItinaryList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertFrequentItinaryAllPropertiesEquals(frequentItinary, testFrequentItinary);
        assertFrequentItinaryUpdatableFieldsEquals(frequentItinary, testFrequentItinary);
    }

    @Test
    void getAllFrequentItinaries() {
        // Initialize the database
        frequentItinaryRepository.save(frequentItinary).block();

        // Get all the frequentItinaryList
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
            .value(hasItem(frequentItinary.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME));
    }

    @Test
    void getFrequentItinary() {
        // Initialize the database
        frequentItinaryRepository.save(frequentItinary).block();

        // Get the frequentItinary
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, frequentItinary.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(frequentItinary.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME));
    }

    @Test
    void getNonExistingFrequentItinary() {
        // Get the frequentItinary
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingFrequentItinary() throws Exception {
        // Initialize the database
        frequentItinaryRepository.save(frequentItinary).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the frequentItinary
        FrequentItinary updatedFrequentItinary = frequentItinaryRepository.findById(frequentItinary.getId()).block();
        updatedFrequentItinary.name(UPDATED_NAME);
        FrequentItinaryDTO frequentItinaryDTO = frequentItinaryMapper.toDto(updatedFrequentItinary);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, frequentItinaryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(frequentItinaryDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FrequentItinary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFrequentItinaryToMatchAllProperties(updatedFrequentItinary);
    }

    @Test
    void putNonExistingFrequentItinary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        frequentItinary.setId(longCount.incrementAndGet());

        // Create the FrequentItinary
        FrequentItinaryDTO frequentItinaryDTO = frequentItinaryMapper.toDto(frequentItinary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, frequentItinaryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(frequentItinaryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FrequentItinary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFrequentItinary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        frequentItinary.setId(longCount.incrementAndGet());

        // Create the FrequentItinary
        FrequentItinaryDTO frequentItinaryDTO = frequentItinaryMapper.toDto(frequentItinary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(frequentItinaryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FrequentItinary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFrequentItinary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        frequentItinary.setId(longCount.incrementAndGet());

        // Create the FrequentItinary
        FrequentItinaryDTO frequentItinaryDTO = frequentItinaryMapper.toDto(frequentItinary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(frequentItinaryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FrequentItinary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFrequentItinaryWithPatch() throws Exception {
        // Initialize the database
        frequentItinaryRepository.save(frequentItinary).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the frequentItinary using partial update
        FrequentItinary partialUpdatedFrequentItinary = new FrequentItinary();
        partialUpdatedFrequentItinary.setId(frequentItinary.getId());

        partialUpdatedFrequentItinary.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFrequentItinary.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedFrequentItinary))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FrequentItinary in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFrequentItinaryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFrequentItinary, frequentItinary),
            getPersistedFrequentItinary(frequentItinary)
        );
    }

    @Test
    void fullUpdateFrequentItinaryWithPatch() throws Exception {
        // Initialize the database
        frequentItinaryRepository.save(frequentItinary).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the frequentItinary using partial update
        FrequentItinary partialUpdatedFrequentItinary = new FrequentItinary();
        partialUpdatedFrequentItinary.setId(frequentItinary.getId());

        partialUpdatedFrequentItinary.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFrequentItinary.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedFrequentItinary))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FrequentItinary in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFrequentItinaryUpdatableFieldsEquals(
            partialUpdatedFrequentItinary,
            getPersistedFrequentItinary(partialUpdatedFrequentItinary)
        );
    }

    @Test
    void patchNonExistingFrequentItinary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        frequentItinary.setId(longCount.incrementAndGet());

        // Create the FrequentItinary
        FrequentItinaryDTO frequentItinaryDTO = frequentItinaryMapper.toDto(frequentItinary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, frequentItinaryDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(frequentItinaryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FrequentItinary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFrequentItinary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        frequentItinary.setId(longCount.incrementAndGet());

        // Create the FrequentItinary
        FrequentItinaryDTO frequentItinaryDTO = frequentItinaryMapper.toDto(frequentItinary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(frequentItinaryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FrequentItinary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFrequentItinary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        frequentItinary.setId(longCount.incrementAndGet());

        // Create the FrequentItinary
        FrequentItinaryDTO frequentItinaryDTO = frequentItinaryMapper.toDto(frequentItinary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(frequentItinaryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FrequentItinary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFrequentItinary() {
        // Initialize the database
        frequentItinaryRepository.save(frequentItinary).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the frequentItinary
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, frequentItinary.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return frequentItinaryRepository.count().block();
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

    protected FrequentItinary getPersistedFrequentItinary(FrequentItinary frequentItinary) {
        return frequentItinaryRepository.findById(frequentItinary.getId()).block();
    }

    protected void assertPersistedFrequentItinaryToMatchAllProperties(FrequentItinary expectedFrequentItinary) {
        // Test fails because reactive api returns an empty object instead of null
        // assertFrequentItinaryAllPropertiesEquals(expectedFrequentItinary, getPersistedFrequentItinary(expectedFrequentItinary));
        assertFrequentItinaryUpdatableFieldsEquals(expectedFrequentItinary, getPersistedFrequentItinary(expectedFrequentItinary));
    }

    protected void assertPersistedFrequentItinaryToMatchUpdatableProperties(FrequentItinary expectedFrequentItinary) {
        // Test fails because reactive api returns an empty object instead of null
        // assertFrequentItinaryAllUpdatablePropertiesEquals(expectedFrequentItinary, getPersistedFrequentItinary(expectedFrequentItinary));
        assertFrequentItinaryUpdatableFieldsEquals(expectedFrequentItinary, getPersistedFrequentItinary(expectedFrequentItinary));
    }
}
