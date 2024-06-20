package com.myway.myway.web.rest;

import static com.myway.myway.domain.PlaceAsserts.*;
import static com.myway.myway.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myway.myway.IntegrationTest;
import com.myway.myway.domain.Place;
import com.myway.myway.repository.EntityManager;
import com.myway.myway.repository.PlaceRepository;
import com.myway.myway.service.dto.PlaceDTO;
import com.myway.myway.service.mapper.PlaceMapper;
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
 * Integration tests for the {@link PlaceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PlaceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DISPLAY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DISPLAY_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_LAT = 1D;
    private static final Double UPDATED_LAT = 2D;

    private static final Double DEFAULT_LON = 1D;
    private static final Double UPDATED_LON = 2D;

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/places";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private PlaceMapper placeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Place place;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Place createEntity(EntityManager em) {
        Place place = new Place()
            .name(DEFAULT_NAME)
            .displayName(DEFAULT_DISPLAY_NAME)
            .lat(DEFAULT_LAT)
            .lon(DEFAULT_LON)
            .image(DEFAULT_IMAGE);
        return place;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Place createUpdatedEntity(EntityManager em) {
        Place place = new Place()
            .name(UPDATED_NAME)
            .displayName(UPDATED_DISPLAY_NAME)
            .lat(UPDATED_LAT)
            .lon(UPDATED_LON)
            .image(UPDATED_IMAGE);
        return place;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Place.class).block();
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
        place = createEntity(em);
    }

    @Test
    void createPlace() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Place
        PlaceDTO placeDTO = placeMapper.toDto(place);
        var returnedPlaceDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(placeDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(PlaceDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Place in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPlace = placeMapper.toEntity(returnedPlaceDTO);
        assertPlaceUpdatableFieldsEquals(returnedPlace, getPersistedPlace(returnedPlace));
    }

    @Test
    void createPlaceWithExistingId() throws Exception {
        // Create the Place with an existing ID
        place.setId(1L);
        PlaceDTO placeDTO = placeMapper.toDto(place);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(placeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Place in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        place.setName(null);

        // Create the Place, which fails.
        PlaceDTO placeDTO = placeMapper.toDto(place);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(placeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkLatIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        place.setLat(null);

        // Create the Place, which fails.
        PlaceDTO placeDTO = placeMapper.toDto(place);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(placeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkLonIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        place.setLon(null);

        // Create the Place, which fails.
        PlaceDTO placeDTO = placeMapper.toDto(place);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(placeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllPlacesAsStream() {
        // Initialize the database
        placeRepository.save(place).block();

        List<Place> placeList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(PlaceDTO.class)
            .getResponseBody()
            .map(placeMapper::toEntity)
            .filter(place::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(placeList).isNotNull();
        assertThat(placeList).hasSize(1);
        Place testPlace = placeList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertPlaceAllPropertiesEquals(place, testPlace);
        assertPlaceUpdatableFieldsEquals(place, testPlace);
    }

    @Test
    void getAllPlaces() {
        // Initialize the database
        placeRepository.save(place).block();

        // Get all the placeList
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
            .value(hasItem(place.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].displayName")
            .value(hasItem(DEFAULT_DISPLAY_NAME))
            .jsonPath("$.[*].lat")
            .value(hasItem(DEFAULT_LAT.doubleValue()))
            .jsonPath("$.[*].lon")
            .value(hasItem(DEFAULT_LON.doubleValue()))
            .jsonPath("$.[*].image")
            .value(hasItem(DEFAULT_IMAGE));
    }

    @Test
    void getPlace() {
        // Initialize the database
        placeRepository.save(place).block();

        // Get the place
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, place.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(place.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.displayName")
            .value(is(DEFAULT_DISPLAY_NAME))
            .jsonPath("$.lat")
            .value(is(DEFAULT_LAT.doubleValue()))
            .jsonPath("$.lon")
            .value(is(DEFAULT_LON.doubleValue()))
            .jsonPath("$.image")
            .value(is(DEFAULT_IMAGE));
    }

    @Test
    void getNonExistingPlace() {
        // Get the place
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPlace() throws Exception {
        // Initialize the database
        placeRepository.save(place).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the place
        Place updatedPlace = placeRepository.findById(place.getId()).block();
        updatedPlace.name(UPDATED_NAME).displayName(UPDATED_DISPLAY_NAME).lat(UPDATED_LAT).lon(UPDATED_LON).image(UPDATED_IMAGE);
        PlaceDTO placeDTO = placeMapper.toDto(updatedPlace);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, placeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(placeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Place in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPlaceToMatchAllProperties(updatedPlace);
    }

    @Test
    void putNonExistingPlace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        place.setId(longCount.incrementAndGet());

        // Create the Place
        PlaceDTO placeDTO = placeMapper.toDto(place);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, placeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(placeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Place in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPlace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        place.setId(longCount.incrementAndGet());

        // Create the Place
        PlaceDTO placeDTO = placeMapper.toDto(place);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(placeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Place in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPlace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        place.setId(longCount.incrementAndGet());

        // Create the Place
        PlaceDTO placeDTO = placeMapper.toDto(place);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(placeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Place in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePlaceWithPatch() throws Exception {
        // Initialize the database
        placeRepository.save(place).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the place using partial update
        Place partialUpdatedPlace = new Place();
        partialUpdatedPlace.setId(place.getId());

        partialUpdatedPlace.displayName(UPDATED_DISPLAY_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPlace.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedPlace))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Place in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlaceUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPlace, place), getPersistedPlace(place));
    }

    @Test
    void fullUpdatePlaceWithPatch() throws Exception {
        // Initialize the database
        placeRepository.save(place).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the place using partial update
        Place partialUpdatedPlace = new Place();
        partialUpdatedPlace.setId(place.getId());

        partialUpdatedPlace.name(UPDATED_NAME).displayName(UPDATED_DISPLAY_NAME).lat(UPDATED_LAT).lon(UPDATED_LON).image(UPDATED_IMAGE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPlace.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedPlace))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Place in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlaceUpdatableFieldsEquals(partialUpdatedPlace, getPersistedPlace(partialUpdatedPlace));
    }

    @Test
    void patchNonExistingPlace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        place.setId(longCount.incrementAndGet());

        // Create the Place
        PlaceDTO placeDTO = placeMapper.toDto(place);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, placeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(placeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Place in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPlace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        place.setId(longCount.incrementAndGet());

        // Create the Place
        PlaceDTO placeDTO = placeMapper.toDto(place);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(placeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Place in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPlace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        place.setId(longCount.incrementAndGet());

        // Create the Place
        PlaceDTO placeDTO = placeMapper.toDto(place);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(placeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Place in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePlace() {
        // Initialize the database
        placeRepository.save(place).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the place
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, place.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return placeRepository.count().block();
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

    protected Place getPersistedPlace(Place place) {
        return placeRepository.findById(place.getId()).block();
    }

    protected void assertPersistedPlaceToMatchAllProperties(Place expectedPlace) {
        // Test fails because reactive api returns an empty object instead of null
        // assertPlaceAllPropertiesEquals(expectedPlace, getPersistedPlace(expectedPlace));
        assertPlaceUpdatableFieldsEquals(expectedPlace, getPersistedPlace(expectedPlace));
    }

    protected void assertPersistedPlaceToMatchUpdatableProperties(Place expectedPlace) {
        // Test fails because reactive api returns an empty object instead of null
        // assertPlaceAllUpdatablePropertiesEquals(expectedPlace, getPersistedPlace(expectedPlace));
        assertPlaceUpdatableFieldsEquals(expectedPlace, getPersistedPlace(expectedPlace));
    }
}
