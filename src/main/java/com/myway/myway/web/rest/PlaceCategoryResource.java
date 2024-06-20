package com.myway.myway.web.rest;

import com.myway.myway.repository.PlaceCategoryRepository;
import com.myway.myway.service.PlaceCategoryService;
import com.myway.myway.service.dto.PlaceCategoryDTO;
import com.myway.myway.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.myway.myway.domain.PlaceCategory}.
 */
@RestController
@RequestMapping("/api/place-categories")
public class PlaceCategoryResource {

    private final Logger log = LoggerFactory.getLogger(PlaceCategoryResource.class);

    private static final String ENTITY_NAME = "placeCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlaceCategoryService placeCategoryService;

    private final PlaceCategoryRepository placeCategoryRepository;

    public PlaceCategoryResource(PlaceCategoryService placeCategoryService, PlaceCategoryRepository placeCategoryRepository) {
        this.placeCategoryService = placeCategoryService;
        this.placeCategoryRepository = placeCategoryRepository;
    }

    /**
     * {@code POST  /place-categories} : Create a new placeCategory.
     *
     * @param placeCategoryDTO the placeCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new placeCategoryDTO, or with status {@code 400 (Bad Request)} if the placeCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<PlaceCategoryDTO>> createPlaceCategory(@Valid @RequestBody PlaceCategoryDTO placeCategoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save PlaceCategory : {}", placeCategoryDTO);
        if (placeCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new placeCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return placeCategoryService
            .save(placeCategoryDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/place-categories/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /place-categories/:id} : Updates an existing placeCategory.
     *
     * @param id the id of the placeCategoryDTO to save.
     * @param placeCategoryDTO the placeCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated placeCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the placeCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the placeCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<PlaceCategoryDTO>> updatePlaceCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PlaceCategoryDTO placeCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PlaceCategory : {}, {}", id, placeCategoryDTO);
        if (placeCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, placeCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return placeCategoryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return placeCategoryService
                    .update(placeCategoryDTO)
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
     * {@code PATCH  /place-categories/:id} : Partial updates given fields of an existing placeCategory, field will ignore if it is null
     *
     * @param id the id of the placeCategoryDTO to save.
     * @param placeCategoryDTO the placeCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated placeCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the placeCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the placeCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the placeCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PlaceCategoryDTO>> partialUpdatePlaceCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PlaceCategoryDTO placeCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PlaceCategory partially : {}, {}", id, placeCategoryDTO);
        if (placeCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, placeCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return placeCategoryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PlaceCategoryDTO> result = placeCategoryService.partialUpdate(placeCategoryDTO);

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
     * {@code GET  /place-categories} : get all the placeCategories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of placeCategories in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<PlaceCategoryDTO>> getAllPlaceCategories() {
        log.debug("REST request to get all PlaceCategories");
        return placeCategoryService.findAll().collectList();
    }

    /**
     * {@code GET  /place-categories} : get all the placeCategories as a stream.
     * @return the {@link Flux} of placeCategories.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<PlaceCategoryDTO> getAllPlaceCategoriesAsStream() {
        log.debug("REST request to get all PlaceCategories as a stream");
        return placeCategoryService.findAll();
    }

    /**
     * {@code GET  /place-categories/:id} : get the "id" placeCategory.
     *
     * @param id the id of the placeCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the placeCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<PlaceCategoryDTO>> getPlaceCategory(@PathVariable("id") Long id) {
        log.debug("REST request to get PlaceCategory : {}", id);
        Mono<PlaceCategoryDTO> placeCategoryDTO = placeCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(placeCategoryDTO);
    }

    /**
     * {@code DELETE  /place-categories/:id} : delete the "id" placeCategory.
     *
     * @param id the id of the placeCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePlaceCategory(@PathVariable("id") Long id) {
        log.debug("REST request to delete PlaceCategory : {}", id);
        return placeCategoryService
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
