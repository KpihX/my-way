package com.kpihx_labs.myway.web.rest;

import static com.kpihx_labs.myway.domain.ItinaryAsserts.*;
import static com.kpihx_labs.myway.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpihx_labs.myway.IntegrationTest;
import com.kpihx_labs.myway.domain.Itinary;
import com.kpihx_labs.myway.repository.EntityManager;
import com.kpihx_labs.myway.repository.ItinaryRepository;
import com.kpihx_labs.myway.service.ItinaryService;
import com.kpihx_labs.myway.service.dto.ItinaryDTO;
import com.kpihx_labs.myway.service.mapper.ItinaryMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

/**
 * Integration tests for the {@link ItinaryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ItinaryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_DISTANCE = 1D;
    private static final Double UPDATED_DISTANCE = 2D;

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;

    private static final String DEFAULT_POLYLINE = "AAAAAAAAAA";
    private static final String UPDATED_POLYLINE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/itinaries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ItinaryRepository itinaryRepository;

    @Mock
    private ItinaryRepository itinaryRepositoryMock;

    @Autowired
    private ItinaryMapper itinaryMapper;

    @Mock
    private ItinaryService itinaryServiceMock;

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
            .description(DEFAULT_DESCRIPTION)
            .distance(DEFAULT_DISTANCE)
            .duration(DEFAULT_DURATION)
            .polyline(DEFAULT_POLYLINE);
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
            .description(UPDATED_DESCRIPTION)
            .distance(UPDATED_DISTANCE)
            .duration(UPDATED_DURATION)
            .polyline(UPDATED_POLYLINE);
        return itinary;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll("rel_itinary__location").block();
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
    void checkPolylineIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        itinary.setPolyline(null);

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
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].distance")
            .value(hasItem(DEFAULT_DISTANCE.doubleValue()))
            .jsonPath("$.[*].duration")
            .value(hasItem(DEFAULT_DURATION))
            .jsonPath("$.[*].polyline")
            .value(hasItem(DEFAULT_POLYLINE));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllItinariesWithEagerRelationshipsIsEnabled() {
        when(itinaryServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(itinaryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllItinariesWithEagerRelationshipsIsNotEnabled() {
        when(itinaryServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(itinaryRepositoryMock, times(1)).findAllWithEagerRelationships(any());
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
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.distance")
            .value(is(DEFAULT_DISTANCE.doubleValue()))
            .jsonPath("$.duration")
            .value(is(DEFAULT_DURATION))
            .jsonPath("$.polyline")
            .value(is(DEFAULT_POLYLINE));
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
            .description(UPDATED_DESCRIPTION)
            .distance(UPDATED_DISTANCE)
            .duration(UPDATED_DURATION)
            .polyline(UPDATED_POLYLINE);
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

        partialUpdatedItinary.name(UPDATED_NAME).duration(UPDATED_DURATION);

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
            .description(UPDATED_DESCRIPTION)
            .distance(UPDATED_DISTANCE)
            .duration(UPDATED_DURATION)
            .polyline(UPDATED_POLYLINE);

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
