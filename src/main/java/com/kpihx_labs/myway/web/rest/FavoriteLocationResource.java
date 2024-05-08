package com.kpihx_labs.myway.web.rest;

import com.kpihx_labs.myway.repository.FavoriteLocationRepository;
import com.kpihx_labs.myway.service.FavoriteLocationService;
import com.kpihx_labs.myway.service.dto.FavoriteLocationDTO;
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
 * REST controller for managing {@link com.kpihx_labs.myway.domain.FavoriteLocation}.
 */
@RestController
@RequestMapping("/api/favorite-locations")
public class FavoriteLocationResource {

    private final Logger log = LoggerFactory.getLogger(FavoriteLocationResource.class);

    private static final String ENTITY_NAME = "favoriteLocation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FavoriteLocationService favoriteLocationService;

    private final FavoriteLocationRepository favoriteLocationRepository;

    public FavoriteLocationResource(
        FavoriteLocationService favoriteLocationService,
        FavoriteLocationRepository favoriteLocationRepository
    ) {
        this.favoriteLocationService = favoriteLocationService;
        this.favoriteLocationRepository = favoriteLocationRepository;
    }

    /**
     * {@code POST  /favorite-locations} : Create a new favoriteLocation.
     *
     * @param favoriteLocationDTO the favoriteLocationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new favoriteLocationDTO, or with status {@code 400 (Bad Request)} if the favoriteLocation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<FavoriteLocationDTO>> createFavoriteLocation(@Valid @RequestBody FavoriteLocationDTO favoriteLocationDTO)
        throws URISyntaxException {
        log.debug("REST request to save FavoriteLocation : {}", favoriteLocationDTO);
        if (favoriteLocationDTO.getId() != null) {
            throw new BadRequestAlertException("A new favoriteLocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return favoriteLocationService
            .save(favoriteLocationDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/favorite-locations/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /favorite-locations/:id} : Updates an existing favoriteLocation.
     *
     * @param id the id of the favoriteLocationDTO to save.
     * @param favoriteLocationDTO the favoriteLocationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated favoriteLocationDTO,
     * or with status {@code 400 (Bad Request)} if the favoriteLocationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the favoriteLocationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<FavoriteLocationDTO>> updateFavoriteLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FavoriteLocationDTO favoriteLocationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FavoriteLocation : {}, {}", id, favoriteLocationDTO);
        if (favoriteLocationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, favoriteLocationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return favoriteLocationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return favoriteLocationService
                    .update(favoriteLocationDTO)
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
     * {@code PATCH  /favorite-locations/:id} : Partial updates given fields of an existing favoriteLocation, field will ignore if it is null
     *
     * @param id the id of the favoriteLocationDTO to save.
     * @param favoriteLocationDTO the favoriteLocationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated favoriteLocationDTO,
     * or with status {@code 400 (Bad Request)} if the favoriteLocationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the favoriteLocationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the favoriteLocationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<FavoriteLocationDTO>> partialUpdateFavoriteLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FavoriteLocationDTO favoriteLocationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FavoriteLocation partially : {}, {}", id, favoriteLocationDTO);
        if (favoriteLocationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, favoriteLocationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return favoriteLocationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<FavoriteLocationDTO> result = favoriteLocationService.partialUpdate(favoriteLocationDTO);

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
     * {@code GET  /favorite-locations} : get all the favoriteLocations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of favoriteLocations in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<FavoriteLocationDTO>> getAllFavoriteLocations() {
        log.debug("REST request to get all FavoriteLocations");
        return favoriteLocationService.findAll().collectList();
    }

    /**
     * {@code GET  /favorite-locations} : get all the favoriteLocations as a stream.
     * @return the {@link Flux} of favoriteLocations.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<FavoriteLocationDTO> getAllFavoriteLocationsAsStream() {
        log.debug("REST request to get all FavoriteLocations as a stream");
        return favoriteLocationService.findAll();
    }

    /**
     * {@code GET  /favorite-locations/:id} : get the "id" favoriteLocation.
     *
     * @param id the id of the favoriteLocationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the favoriteLocationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<FavoriteLocationDTO>> getFavoriteLocation(@PathVariable("id") Long id) {
        log.debug("REST request to get FavoriteLocation : {}", id);
        Mono<FavoriteLocationDTO> favoriteLocationDTO = favoriteLocationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(favoriteLocationDTO);
    }

    /**
     * {@code DELETE  /favorite-locations/:id} : delete the "id" favoriteLocation.
     *
     * @param id the id of the favoriteLocationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteFavoriteLocation(@PathVariable("id") Long id) {
        log.debug("REST request to delete FavoriteLocation : {}", id);
        return favoriteLocationService
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
