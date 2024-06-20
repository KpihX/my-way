package com.myway.myway.web.rest;

import static com.myway.myway.domain.PlaceCategoryAsserts.*;
import static com.myway.myway.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myway.myway.IntegrationTest;
import com.myway.myway.domain.PlaceCategory;
import com.myway.myway.repository.EntityManager;
import com.myway.myway.repository.PlaceCategoryRepository;
import com.myway.myway.service.dto.PlaceCategoryDTO;
import com.myway.myway.service.mapper.PlaceCategoryMapper;
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
 * Integration tests for the {@link PlaceCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PlaceCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/place-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PlaceCategoryRepository placeCategoryRepository;

    @Autowired
    private PlaceCategoryMapper placeCategoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private PlaceCategory placeCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlaceCategory createEntity(EntityManager em) {
        PlaceCategory placeCategory = new PlaceCategory().name(DEFAULT_NAME);
        return placeCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlaceCategory createUpdatedEntity(EntityManager em) {
        PlaceCategory placeCategory = new PlaceCategory().name(UPDATED_NAME);
        return placeCategory;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(PlaceCategory.class).block();
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
        placeCategory = createEntity(em);
    }

    @Test
    void createPlaceCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PlaceCategory
        PlaceCategoryDTO placeCategoryDTO = placeCategoryMapper.toDto(placeCategory);
        var returnedPlaceCategoryDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(placeCategoryDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(PlaceCategoryDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the PlaceCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPlaceCategory = placeCategoryMapper.toEntity(returnedPlaceCategoryDTO);
        assertPlaceCategoryUpdatableFieldsEquals(returnedPlaceCategory, getPersistedPlaceCategory(returnedPlaceCategory));
    }

    @Test
    void createPlaceCategoryWithExistingId() throws Exception {
        // Create the PlaceCategory with an existing ID
        placeCategory.setId(1L);
        PlaceCategoryDTO placeCategoryDTO = placeCategoryMapper.toDto(placeCategory);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(placeCategoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PlaceCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        placeCategory.setName(null);

        // Create the PlaceCategory, which fails.
        PlaceCategoryDTO placeCategoryDTO = placeCategoryMapper.toDto(placeCategory);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(placeCategoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllPlaceCategoriesAsStream() {
        // Initialize the database
        placeCategoryRepository.save(placeCategory).block();

        List<PlaceCategory> placeCategoryList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(PlaceCategoryDTO.class)
            .getResponseBody()
            .map(placeCategoryMapper::toEntity)
            .filter(placeCategory::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(placeCategoryList).isNotNull();
        assertThat(placeCategoryList).hasSize(1);
        PlaceCategory testPlaceCategory = placeCategoryList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertPlaceCategoryAllPropertiesEquals(placeCategory, testPlaceCategory);
        assertPlaceCategoryUpdatableFieldsEquals(placeCategory, testPlaceCategory);
    }

    @Test
    void getAllPlaceCategories() {
        // Initialize the database
        placeCategoryRepository.save(placeCategory).block();

        // Get all the placeCategoryList
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
            .value(hasItem(placeCategory.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME));
    }

    @Test
    void getPlaceCategory() {
        // Initialize the database
        placeCategoryRepository.save(placeCategory).block();

        // Get the placeCategory
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, placeCategory.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(placeCategory.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME));
    }

    @Test
    void getNonExistingPlaceCategory() {
        // Get the placeCategory
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPlaceCategory() throws Exception {
        // Initialize the database
        placeCategoryRepository.save(placeCategory).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the placeCategory
        PlaceCategory updatedPlaceCategory = placeCategoryRepository.findById(placeCategory.getId()).block();
        updatedPlaceCategory.name(UPDATED_NAME);
        PlaceCategoryDTO placeCategoryDTO = placeCategoryMapper.toDto(updatedPlaceCategory);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, placeCategoryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(placeCategoryDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PlaceCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPlaceCategoryToMatchAllProperties(updatedPlaceCategory);
    }

    @Test
    void putNonExistingPlaceCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        placeCategory.setId(longCount.incrementAndGet());

        // Create the PlaceCategory
        PlaceCategoryDTO placeCategoryDTO = placeCategoryMapper.toDto(placeCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, placeCategoryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(placeCategoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PlaceCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPlaceCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        placeCategory.setId(longCount.incrementAndGet());

        // Create the PlaceCategory
        PlaceCategoryDTO placeCategoryDTO = placeCategoryMapper.toDto(placeCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(placeCategoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PlaceCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPlaceCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        placeCategory.setId(longCount.incrementAndGet());

        // Create the PlaceCategory
        PlaceCategoryDTO placeCategoryDTO = placeCategoryMapper.toDto(placeCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(placeCategoryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PlaceCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePlaceCategoryWithPatch() throws Exception {
        // Initialize the database
        placeCategoryRepository.save(placeCategory).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the placeCategory using partial update
        PlaceCategory partialUpdatedPlaceCategory = new PlaceCategory();
        partialUpdatedPlaceCategory.setId(placeCategory.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPlaceCategory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedPlaceCategory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PlaceCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlaceCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPlaceCategory, placeCategory),
            getPersistedPlaceCategory(placeCategory)
        );
    }

    @Test
    void fullUpdatePlaceCategoryWithPatch() throws Exception {
        // Initialize the database
        placeCategoryRepository.save(placeCategory).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the placeCategory using partial update
        PlaceCategory partialUpdatedPlaceCategory = new PlaceCategory();
        partialUpdatedPlaceCategory.setId(placeCategory.getId());

        partialUpdatedPlaceCategory.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPlaceCategory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedPlaceCategory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PlaceCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlaceCategoryUpdatableFieldsEquals(partialUpdatedPlaceCategory, getPersistedPlaceCategory(partialUpdatedPlaceCategory));
    }

    @Test
    void patchNonExistingPlaceCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        placeCategory.setId(longCount.incrementAndGet());

        // Create the PlaceCategory
        PlaceCategoryDTO placeCategoryDTO = placeCategoryMapper.toDto(placeCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, placeCategoryDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(placeCategoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PlaceCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPlaceCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        placeCategory.setId(longCount.incrementAndGet());

        // Create the PlaceCategory
        PlaceCategoryDTO placeCategoryDTO = placeCategoryMapper.toDto(placeCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(placeCategoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PlaceCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPlaceCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        placeCategory.setId(longCount.incrementAndGet());

        // Create the PlaceCategory
        PlaceCategoryDTO placeCategoryDTO = placeCategoryMapper.toDto(placeCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(placeCategoryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PlaceCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePlaceCategory() {
        // Initialize the database
        placeCategoryRepository.save(placeCategory).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the placeCategory
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, placeCategory.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return placeCategoryRepository.count().block();
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

    protected PlaceCategory getPersistedPlaceCategory(PlaceCategory placeCategory) {
        return placeCategoryRepository.findById(placeCategory.getId()).block();
    }

    protected void assertPersistedPlaceCategoryToMatchAllProperties(PlaceCategory expectedPlaceCategory) {
        // Test fails because reactive api returns an empty object instead of null
        // assertPlaceCategoryAllPropertiesEquals(expectedPlaceCategory, getPersistedPlaceCategory(expectedPlaceCategory));
        assertPlaceCategoryUpdatableFieldsEquals(expectedPlaceCategory, getPersistedPlaceCategory(expectedPlaceCategory));
    }

    protected void assertPersistedPlaceCategoryToMatchUpdatableProperties(PlaceCategory expectedPlaceCategory) {
        // Test fails because reactive api returns an empty object instead of null
        // assertPlaceCategoryAllUpdatablePropertiesEquals(expectedPlaceCategory, getPersistedPlaceCategory(expectedPlaceCategory));
        assertPlaceCategoryUpdatableFieldsEquals(expectedPlaceCategory, getPersistedPlaceCategory(expectedPlaceCategory));
    }
}
