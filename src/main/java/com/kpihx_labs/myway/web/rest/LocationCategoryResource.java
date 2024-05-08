package com.kpihx_labs.myway.web.rest;

import com.kpihx_labs.myway.repository.LocationCategoryRepository;
import com.kpihx_labs.myway.service.LocationCategoryService;
import com.kpihx_labs.myway.service.dto.LocationCategoryDTO;
import com.kpihx_labs.myway.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.kpihx_labs.myway.domain.LocationCategory}.
 */
@RestController
@RequestMapping("/api/location-categories")
public class LocationCategoryResource {

    private final Logger log = LoggerFactory.getLogger(LocationCategoryResource.class);

    private static final String ENTITY_NAME = "locationCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocationCategoryService locationCategoryService;

    private final LocationCategoryRepository locationCategoryRepository;

    public LocationCategoryResource(
        LocationCategoryService locationCategoryService,
        LocationCategoryRepository locationCategoryRepository
    ) {
        this.locationCategoryService = locationCategoryService;
        this.locationCategoryRepository = locationCategoryRepository;
    }

    /**
     * {@code POST  /location-categories} : Create a new locationCategory.
     *
     * @param locationCategoryDTO the locationCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new locationCategoryDTO, or with status {@code 400 (Bad Request)} if the locationCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<LocationCategoryDTO>> createLocationCategory(@Valid @RequestBody LocationCategoryDTO locationCategoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save LocationCategory : {}", locationCategoryDTO);
        if (locationCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new locationCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return locationCategoryService
            .save(locationCategoryDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/location-categories/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /location-categories/:id} : Updates an existing locationCategory.
     *
     * @param id the id of the locationCategoryDTO to save.
     * @param locationCategoryDTO the locationCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the locationCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the locationCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<LocationCategoryDTO>> updateLocationCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LocationCategoryDTO locationCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LocationCategory : {}, {}", id, locationCategoryDTO);
        if (locationCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locationCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return locationCategoryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return locationCategoryService
                    .update(locationCategoryDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        result ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                                .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /location-categories/:id} : Partial updates given fields of an existing locationCategory, field will ignore if it is null
     *
     * @param id the id of the locationCategoryDTO to save.
     * @param locationCategoryDTO the locationCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the locationCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the locationCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the locationCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<LocationCategoryDTO>> partialUpdateLocationCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LocationCategoryDTO locationCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LocationCategory partially : {}, {}", id, locationCategoryDTO);
        if (locationCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locationCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return locationCategoryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<LocationCategoryDTO> result = locationCategoryService.partialUpdate(locationCategoryDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        res ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                                .body(res)
                    );
            });
    }

    /**
     * {@code GET  /location-categories} : get all the locationCategories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of locationCategories in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<LocationCategoryDTO>> getAllLocationCategories() {
        log.debug("REST request to get all LocationCategories");
        return locationCategoryService.findAll().collectList();
    }

    /**
     * {@code GET  /location-categories} : get all the locationCategories as a stream.
     * @return the {@link Flux} of locationCategories.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<LocationCategoryDTO> getAllLocationCategoriesAsStream() {
        log.debug("REST request to get all LocationCategories as a stream");
        return locationCategoryService.findAll();
    }

    /**
     * {@code GET  /location-categories/:id} : get the "id" locationCategory.
     *
     * @param id the id of the locationCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the locationCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<LocationCategoryDTO>> getLocationCategory(@PathVariable("id") Long id) {
        log.debug("REST request to get LocationCategory : {}", id);
        Mono<LocationCategoryDTO> locationCategoryDTO = locationCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(locationCategoryDTO);
    }

    /**
     * {@code DELETE  /location-categories/:id} : delete the "id" locationCategory.
     *
     * @param id the id of the locationCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteLocationCategory(@PathVariable("id") Long id) {
        log.debug("REST request to delete LocationCategory : {}", id);
        return locationCategoryService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
