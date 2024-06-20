package com.myway.myway.web.rest;

import com.myway.myway.repository.PlaceRepository;
import com.myway.myway.service.PlaceService;
import com.myway.myway.service.dto.PlaceDTO;
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
 * REST controller for managing {@link com.myway.myway.domain.Place}.
 */
@RestController
@RequestMapping("/api/places")
public class PlaceResource {

    private final Logger log = LoggerFactory.getLogger(PlaceResource.class);

    private static final String ENTITY_NAME = "place";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlaceService placeService;

    private final PlaceRepository placeRepository;

    public PlaceResource(PlaceService placeService, PlaceRepository placeRepository) {
        this.placeService = placeService;
        this.placeRepository = placeRepository;
    }

    /**
     * {@code POST  /places} : Create a new place.
     *
     * @param placeDTO the placeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new placeDTO, or with status {@code 400 (Bad Request)} if the place has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<PlaceDTO>> createPlace(@Valid @RequestBody PlaceDTO placeDTO) throws URISyntaxException {
        log.debug("REST request to save Place : {}", placeDTO);
        if (placeDTO.getId() != null) {
            throw new BadRequestAlertException("A new place cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return placeService
            .save(placeDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/places/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /places/:id} : Updates an existing place.
     *
     * @param id the id of the placeDTO to save.
     * @param placeDTO the placeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated placeDTO,
     * or with status {@code 400 (Bad Request)} if the placeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the placeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<PlaceDTO>> updatePlace(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PlaceDTO placeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Place : {}, {}", id, placeDTO);
        if (placeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, placeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return placeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return placeService
                    .update(placeDTO)
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
     * {@code PATCH  /places/:id} : Partial updates given fields of an existing place, field will ignore if it is null
     *
     * @param id the id of the placeDTO to save.
     * @param placeDTO the placeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated placeDTO,
     * or with status {@code 400 (Bad Request)} if the placeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the placeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the placeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PlaceDTO>> partialUpdatePlace(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PlaceDTO placeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Place partially : {}, {}", id, placeDTO);
        if (placeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, placeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return placeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PlaceDTO> result = placeService.partialUpdate(placeDTO);

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
     * {@code GET  /places} : get all the places.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of places in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<PlaceDTO>> getAllPlaces() {
        log.debug("REST request to get all Places");
        return placeService.findAll().collectList();
    }

    /**
     * {@code GET  /places} : get all the places as a stream.
     * @return the {@link Flux} of places.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<PlaceDTO> getAllPlacesAsStream() {
        log.debug("REST request to get all Places as a stream");
        return placeService.findAll();
    }

    /**
     * {@code GET  /places/:id} : get the "id" place.
     *
     * @param id the id of the placeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the placeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<PlaceDTO>> getPlace(@PathVariable("id") Long id) {
        log.debug("REST request to get Place : {}", id);
        Mono<PlaceDTO> placeDTO = placeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(placeDTO);
    }

    /**
     * {@code DELETE  /places/:id} : delete the "id" place.
     *
     * @param id the id of the placeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePlace(@PathVariable("id") Long id) {
        log.debug("REST request to delete Place : {}", id);
        return placeService
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
