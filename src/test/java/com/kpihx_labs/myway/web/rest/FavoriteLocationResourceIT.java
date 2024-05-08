package com.kpihx_labs.myway.web.rest;

import static com.kpihx_labs.myway.domain.FavoriteLocationAsserts.*;
import static com.kpihx_labs.myway.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpihx_labs.myway.IntegrationTest;
import com.kpihx_labs.myway.domain.FavoriteLocation;
import com.kpihx_labs.myway.repository.EntityManager;
import com.kpihx_labs.myway.repository.FavoriteLocationRepository;
import com.kpihx_labs.myway.service.dto.FavoriteLocationDTO;
import com.kpihx_labs.myway.service.mapper.FavoriteLocationMapper;
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
 * Integration tests for the {@link FavoriteLocationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class FavoriteLocationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/favorite-locations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FavoriteLocationRepository favoriteLocationRepository;

    @Autowired
    private FavoriteLocationMapper favoriteLocationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private FavoriteLocation favoriteLocation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FavoriteLocation createEntity(EntityManager em) {
        FavoriteLocation favoriteLocation = new FavoriteLocation().name(DEFAULT_NAME);
        return favoriteLocation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FavoriteLocation createUpdatedEntity(EntityManager em) {
        FavoriteLocation favoriteLocation = new FavoriteLocation().name(UPDATED_NAME);
        return favoriteLocation;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(FavoriteLocation.class).block();
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
        favoriteLocation = createEntity(em);
    }

    @Test
    void createFavoriteLocation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FavoriteLocation
        FavoriteLocationDTO favoriteLocationDTO = favoriteLocationMapper.toDto(favoriteLocation);
        var returnedFavoriteLocationDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(favoriteLocationDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(FavoriteLocationDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the FavoriteLocation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFavoriteLocation = favoriteLocationMapper.toEntity(returnedFavoriteLocationDTO);
        assertFavoriteLocationUpdatableFieldsEquals(returnedFavoriteLocation, getPersistedFavoriteLocation(returnedFavoriteLocation));
    }

    @Test
    void createFavoriteLocationWithExistingId() throws Exception {
        // Create the FavoriteLocation with an existing ID
        favoriteLocation.setId(1L);
        FavoriteLocationDTO favoriteLocationDTO = favoriteLocationMapper.toDto(favoriteLocation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(favoriteLocationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FavoriteLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        favoriteLocation.setName(null);

        // Create the FavoriteLocation, which fails.
        FavoriteLocationDTO favoriteLocationDTO = favoriteLocationMapper.toDto(favoriteLocation);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(favoriteLocationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllFavoriteLocationsAsStream() {
        // Initialize the database
        favoriteLocationRepository.save(favoriteLocation).block();

        List<FavoriteLocation> favoriteLocationList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(FavoriteLocationDTO.class)
            .getResponseBody()
            .map(favoriteLocationMapper::toEntity)
            .filter(favoriteLocation::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(favoriteLocationList).isNotNull();
        assertThat(favoriteLocationList).hasSize(1);
        FavoriteLocation testFavoriteLocation = favoriteLocationList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertFavoriteLocationAllPropertiesEquals(favoriteLocation, testFavoriteLocation);
        assertFavoriteLocationUpdatableFieldsEquals(favoriteLocation, testFavoriteLocation);
    }

    @Test
    void getAllFavoriteLocations() {
        // Initialize the database
        favoriteLocationRepository.save(favoriteLocation).block();

        // Get all the favoriteLocationList
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
            .value(hasItem(favoriteLocation.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME));
    }

    @Test
    void getFavoriteLocation() {
        // Initialize the database
        favoriteLocationRepository.save(favoriteLocation).block();

        // Get the favoriteLocation
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, favoriteLocation.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(favoriteLocation.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME));
    }

    @Test
    void getNonExistingFavoriteLocation() {
        // Get the favoriteLocation
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingFavoriteLocation() throws Exception {
        // Initialize the database
        favoriteLocationRepository.save(favoriteLocation).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the favoriteLocation
        FavoriteLocation updatedFavoriteLocation = favoriteLocationRepository.findById(favoriteLocation.getId()).block();
        updatedFavoriteLocation.name(UPDATED_NAME);
        FavoriteLocationDTO favoriteLocationDTO = favoriteLocationMapper.toDto(updatedFavoriteLocation);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, favoriteLocationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(favoriteLocationDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FavoriteLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFavoriteLocationToMatchAllProperties(updatedFavoriteLocation);
    }

    @Test
    void putNonExistingFavoriteLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        favoriteLocation.setId(longCount.incrementAndGet());

        // Create the FavoriteLocation
        FavoriteLocationDTO favoriteLocationDTO = favoriteLocationMapper.toDto(favoriteLocation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, favoriteLocationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(favoriteLocationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FavoriteLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFavoriteLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        favoriteLocation.setId(longCount.incrementAndGet());

        // Create the FavoriteLocation
        FavoriteLocationDTO favoriteLocationDTO = favoriteLocationMapper.toDto(favoriteLocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(favoriteLocationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FavoriteLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFavoriteLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        favoriteLocation.setId(longCount.incrementAndGet());

        // Create the FavoriteLocation
        FavoriteLocationDTO favoriteLocationDTO = favoriteLocationMapper.toDto(favoriteLocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(favoriteLocationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FavoriteLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFavoriteLocationWithPatch() throws Exception {
        // Initialize the database
        favoriteLocationRepository.save(favoriteLocation).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the favoriteLocation using partial update
        FavoriteLocation partialUpdatedFavoriteLocation = new FavoriteLocation();
        partialUpdatedFavoriteLocation.setId(favoriteLocation.getId());

        partialUpdatedFavoriteLocation.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFavoriteLocation.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedFavoriteLocation))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FavoriteLocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFavoriteLocationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFavoriteLocation, favoriteLocation),
            getPersistedFavoriteLocation(favoriteLocation)
        );
    }

    @Test
    void fullUpdateFavoriteLocationWithPatch() throws Exception {
        // Initialize the database
        favoriteLocationRepository.save(favoriteLocation).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the favoriteLocation using partial update
        FavoriteLocation partialUpdatedFavoriteLocation = new FavoriteLocation();
        partialUpdatedFavoriteLocation.setId(favoriteLocation.getId());

        partialUpdatedFavoriteLocation.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFavoriteLocation.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedFavoriteLocation))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FavoriteLocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFavoriteLocationUpdatableFieldsEquals(
            partialUpdatedFavoriteLocation,
            getPersistedFavoriteLocation(partialUpdatedFavoriteLocation)
        );
    }

    @Test
    void patchNonExistingFavoriteLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        favoriteLocation.setId(longCount.incrementAndGet());

        // Create the FavoriteLocation
        FavoriteLocationDTO favoriteLocationDTO = favoriteLocationMapper.toDto(favoriteLocation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, favoriteLocationDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(favoriteLocationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FavoriteLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFavoriteLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        favoriteLocation.setId(longCount.incrementAndGet());

        // Create the FavoriteLocation
        FavoriteLocationDTO favoriteLocationDTO = favoriteLocationMapper.toDto(favoriteLocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(favoriteLocationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FavoriteLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFavoriteLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        favoriteLocation.setId(longCount.incrementAndGet());

        // Create the FavoriteLocation
        FavoriteLocationDTO favoriteLocationDTO = favoriteLocationMapper.toDto(favoriteLocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(favoriteLocationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FavoriteLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFavoriteLocation() {
        // Initialize the database
        favoriteLocationRepository.save(favoriteLocation).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the favoriteLocation
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, favoriteLocation.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return favoriteLocationRepository.count().block();
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

    protected FavoriteLocation getPersistedFavoriteLocation(FavoriteLocation favoriteLocation) {
        return favoriteLocationRepository.findById(favoriteLocation.getId()).block();
    }

    protected void assertPersistedFavoriteLocationToMatchAllProperties(FavoriteLocation expectedFavoriteLocation) {
        // Test fails because reactive api returns an empty object instead of null
        // assertFavoriteLocationAllPropertiesEquals(expectedFavoriteLocation, getPersistedFavoriteLocation(expectedFavoriteLocation));
        assertFavoriteLocationUpdatableFieldsEquals(expectedFavoriteLocation, getPersistedFavoriteLocation(expectedFavoriteLocation));
    }

    protected void assertPersistedFavoriteLocationToMatchUpdatableProperties(FavoriteLocation expectedFavoriteLocation) {
        // Test fails because reactive api returns an empty object instead of null
        // assertFavoriteLocationAllUpdatablePropertiesEquals(expectedFavoriteLocation, getPersistedFavoriteLocation(expectedFavoriteLocation));
        assertFavoriteLocationUpdatableFieldsEquals(expectedFavoriteLocation, getPersistedFavoriteLocation(expectedFavoriteLocation));
    }
}
