package com.myway.myway.web.rest;

import com.myway.myway.repository.ItinaryRepository;
import com.myway.myway.service.ItinaryService;
import com.myway.myway.service.dto.ItinaryDTO;
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
 * REST controller for managing {@link com.myway.myway.domain.Itinary}.
 */
@RestController
@RequestMapping("/api/itinaries")
public class ItinaryResource {

    private final Logger log = LoggerFactory.getLogger(ItinaryResource.class);

    private static final String ENTITY_NAME = "itinary";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItinaryService itinaryService;

    private final ItinaryRepository itinaryRepository;

    public ItinaryResource(ItinaryService itinaryService, ItinaryRepository itinaryRepository) {
        this.itinaryService = itinaryService;
        this.itinaryRepository = itinaryRepository;
    }

    /**
     * {@code POST  /itinaries} : Create a new itinary.
     *
     * @param itinaryDTO the itinaryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itinaryDTO, or with status {@code 400 (Bad Request)} if the itinary has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ItinaryDTO>> createItinary(@Valid @RequestBody ItinaryDTO itinaryDTO) throws URISyntaxException {
        log.debug("REST request to save Itinary : {}", itinaryDTO);
        if (itinaryDTO.getId() != null) {
            throw new BadRequestAlertException("A new itinary cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return itinaryService
            .save(itinaryDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/itinaries/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /itinaries/:id} : Updates an existing itinary.
     *
     * @param id the id of the itinaryDTO to save.
     * @param itinaryDTO the itinaryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itinaryDTO,
     * or with status {@code 400 (Bad Request)} if the itinaryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itinaryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ItinaryDTO>> updateItinary(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ItinaryDTO itinaryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Itinary : {}, {}", id, itinaryDTO);
        if (itinaryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itinaryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return itinaryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return itinaryService
                    .update(itinaryDTO)
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
     * {@code PATCH  /itinaries/:id} : Partial updates given fields of an existing itinary, field will ignore if it is null
     *
     * @param id the id of the itinaryDTO to save.
     * @param itinaryDTO the itinaryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itinaryDTO,
     * or with status {@code 400 (Bad Request)} if the itinaryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the itinaryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the itinaryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ItinaryDTO>> partialUpdateItinary(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ItinaryDTO itinaryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Itinary partially : {}, {}", id, itinaryDTO);
        if (itinaryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itinaryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return itinaryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ItinaryDTO> result = itinaryService.partialUpdate(itinaryDTO);

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
     * {@code GET  /itinaries} : get all the itinaries.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itinaries in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<ItinaryDTO>> getAllItinaries() {
        log.debug("REST request to get all Itinaries");
        return itinaryService.findAll().collectList();
    }

    /**
     * {@code GET  /itinaries} : get all the itinaries as a stream.
     * @return the {@link Flux} of itinaries.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ItinaryDTO> getAllItinariesAsStream() {
        log.debug("REST request to get all Itinaries as a stream");
        return itinaryService.findAll();
    }

    /**
     * {@code GET  /itinaries/:id} : get the "id" itinary.
     *
     * @param id the id of the itinaryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itinaryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ItinaryDTO>> getItinary(@PathVariable("id") Long id) {
        log.debug("REST request to get Itinary : {}", id);
        Mono<ItinaryDTO> itinaryDTO = itinaryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itinaryDTO);
    }

    /**
     * {@code DELETE  /itinaries/:id} : delete the "id" itinary.
     *
     * @param id the id of the itinaryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteItinary(@PathVariable("id") Long id) {
        log.debug("REST request to delete Itinary : {}", id);
        return itinaryService
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
