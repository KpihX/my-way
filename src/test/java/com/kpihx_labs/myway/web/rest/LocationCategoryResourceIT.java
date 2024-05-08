package com.kpihx_labs.myway.web.rest;

import static com.kpihx_labs.myway.domain.LocationCategoryAsserts.*;
import static com.kpihx_labs.myway.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpihx_labs.myway.IntegrationTest;
import com.kpihx_labs.myway.domain.LocationCategory;
import com.kpihx_labs.myway.repository.EntityManager;
import com.kpihx_labs.myway.repository.LocationCategoryRepository;
import com.kpihx_labs.myway.service.dto.LocationCategoryDTO;
import com.kpihx_labs.myway.service.mapper.LocationCategoryMapper;
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
 * Integration tests for the {@link LocationCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class LocationCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/location-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LocationCategoryRepository locationCategoryRepository;

    @Autowired
    private LocationCategoryMapper locationCategoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private LocationCategory locationCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocationCategory createEntity(EntityManager em) {
        LocationCategory locationCategory = new LocationCategory().name(DEFAULT_NAME);
        return locationCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocationCategory createUpdatedEntity(EntityManager em) {
        LocationCategory locationCategory = new LocationCategory().name(UPDATED_NAME);
        return locationCategory;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(LocationCategory.class).block();
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
        locationCategory = createEntity(em);
    }

    @Test
    void createLocationCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LocationCategory
        LocationCategoryDTO locationCategoryDTO = locationCategoryMapper.toDto(locationCategory);
        var returnedLocationCategoryDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(locationCategoryDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(LocationCategoryDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the LocationCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLocationCategory = locationCategoryMapper.toEntity(returnedLocationCategoryDTO);
        assertLocationCategoryUpdatableFieldsEquals(returnedLocationCategory, getPersistedLocationCategory(returnedLocationCategory));
    }

    @Test
    void createLocationCategoryWithExistingId() throws Exception {
        // Create the LocationCategory with an existing ID
        locationCategory.setId(1L);
        LocationCategoryDTO locationCategoryDTO = locationCategoryMapper.toDto(locationCategory);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(locationCategoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the LocationCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        locationCategory.setName(null);

        // Create the LocationCategory, which fails.
        LocationCategoryDTO locationCategoryDTO = locationCategoryMapper.toDto(locationCategory);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(locationCategoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllLocationCategoriesAsStream() {
        // Initialize the database
        locationCategoryRepository.save(locationCategory).block();

        List<LocationCategory> locationCategoryList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(LocationCategoryDTO.class)
            .getResponseBody()
            .map(locationCategoryMapper::toEntity)
            .filter(locationCategory::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(locationCategoryList).isNotNull();
        assertThat(locationCategoryList).hasSize(1);
        LocationCategory testLocationCategory = locationCategoryList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertLocationCategoryAllPropertiesEquals(locationCategory, testLocationCategory);
        assertLocationCategoryUpdatableFieldsEquals(locationCategory, testLocationCategory);
    }

    @Test
    void getAllLocationCategories() {
        // Initialize the database
        locationCategoryRepository.save(locationCategory).block();

        // Get all the locationCategoryList
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
            .value(hasItem(locationCategory.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME));
    }

    @Test
    void getLocationCategory() {
        // Initialize the database
        locationCategoryRepository.save(locationCategory).block();

        // Get the locationCategory
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, locationCategory.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(locationCategory.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME));
    }

    @Test
    void getNonExistingLocationCategory() {
        // Get the locationCategory
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingLocationCategory() throws Exception {
        // Initialize the database
        locationCategoryRepository.save(locationCategory).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the locationCategory
        LocationCategory updatedLocationCategory = locationCategoryRepository.findById(locationCategory.getId()).block();
        updatedLocationCategory.name(UPDATED_NAME);
        LocationCategoryDTO locationCategoryDTO = locationCategoryMapper.toDto(updatedLocationCategory);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, locationCategoryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(locationCategoryDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the LocationCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLocationCategoryToMatchAllProperties(updatedLocationCategory);
    }

    @Test
    void putNonExistingLocationCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        locationCategory.setId(longCount.incrementAndGet());

        // Create the LocationCategory
        LocationCategoryDTO locationCategoryDTO = locationCategoryMapper.toDto(locationCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, locationCategoryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(locationCategoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the LocationCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchLocationCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        locationCategory.setId(longCount.incrementAndGet());

        // Create the LocationCategory
        LocationCategoryDTO locationCategoryDTO = locationCategoryMapper.toDto(locationCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(locationCategoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the LocationCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamLocationCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        locationCategory.setId(longCount.incrementAndGet());

        // Create the LocationCategory
        LocationCategoryDTO locationCategoryDTO = locationCategoryMapper.toDto(locationCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(locationCategoryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the LocationCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateLocationCategoryWithPatch() throws Exception {
        // Initialize the database
        locationCategoryRepository.save(locationCategory).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the locationCategory using partial update
        LocationCategory partialUpdatedLocationCategory = new LocationCategory();
        partialUpdatedLocationCategory.setId(locationCategory.getId());

        partialUpdatedLocationCategory.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLocationCategory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedLocationCategory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the LocationCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLocationCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLocationCategory, locationCategory),
            getPersistedLocationCategory(locationCategory)
        );
    }

    @Test
    void fullUpdateLocationCategoryWithPatch() throws Exception {
        // Initialize the database
        locationCategoryRepository.save(locationCategory).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the locationCategory using partial update
        LocationCategory partialUpdatedLocationCategory = new LocationCategory();
        partialUpdatedLocationCategory.setId(locationCategory.getId());

        partialUpdatedLocationCategory.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLocationCategory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedLocationCategory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the LocationCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLocationCategoryUpdatableFieldsEquals(
            partialUpdatedLocationCategory,
            getPersistedLocationCategory(partialUpdatedLocationCategory)
        );
    }

    @Test
    void patchNonExistingLocationCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        locationCategory.setId(longCount.incrementAndGet());

        // Create the LocationCategory
        LocationCategoryDTO locationCategoryDTO = locationCategoryMapper.toDto(locationCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, locationCategoryDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(locationCategoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the LocationCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchLocationCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        locationCategory.setId(longCount.incrementAndGet());

        // Create the LocationCategory
        LocationCategoryDTO locationCategoryDTO = locationCategoryMapper.toDto(locationCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(locationCategoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the LocationCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamLocationCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        locationCategory.setId(longCount.incrementAndGet());

        // Create the LocationCategory
        LocationCategoryDTO locationCategoryDTO = locationCategoryMapper.toDto(locationCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(locationCategoryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the LocationCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteLocationCategory() {
        // Initialize the database
        locationCategoryRepository.save(locationCategory).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the locationCategory
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, locationCategory.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return locationCategoryRepository.count().block();
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

    protected LocationCategory getPersistedLocationCategory(LocationCategory locationCategory) {
        return locationCategoryRepository.findById(locationCategory.getId()).block();
    }

    protected void assertPersistedLocationCategoryToMatchAllProperties(LocationCategory expectedLocationCategory) {
        // Test fails because reactive api returns an empty object instead of null
        // assertLocationCategoryAllPropertiesEquals(expectedLocationCategory, getPersistedLocationCategory(expectedLocationCategory));
        assertLocationCategoryUpdatableFieldsEquals(expectedLocationCategory, getPersistedLocationCategory(expectedLocationCategory));
    }

    protected void assertPersistedLocationCategoryToMatchUpdatableProperties(LocationCategory expectedLocationCategory) {
        // Test fails because reactive api returns an empty object instead of null
        // assertLocationCategoryAllUpdatablePropertiesEquals(expectedLocationCategory, getPersistedLocationCategory(expectedLocationCategory));
        assertLocationCategoryUpdatableFieldsEquals(expectedLocationCategory, getPersistedLocationCategory(expectedLocationCategory));
    }
}
