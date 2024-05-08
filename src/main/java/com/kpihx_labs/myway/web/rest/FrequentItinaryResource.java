package com.kpihx_labs.myway.web.rest;

import com.kpihx_labs.myway.repository.FrequentItinaryRepository;
import com.kpihx_labs.myway.service.FrequentItinaryService;
import com.kpihx_labs.myway.service.dto.FrequentItinaryDTO;
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
 * REST controller for managing {@link com.kpihx_labs.myway.domain.FrequentItinary}.
 */
@RestController
@RequestMapping("/api/frequent-itinaries")
public class FrequentItinaryResource {

    private final Logger log = LoggerFactory.getLogger(FrequentItinaryResource.class);

    private static final String ENTITY_NAME = "frequentItinary";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FrequentItinaryService frequentItinaryService;

    private final FrequentItinaryRepository frequentItinaryRepository;

    public FrequentItinaryResource(FrequentItinaryService frequentItinaryService, FrequentItinaryRepository frequentItinaryRepository) {
        this.frequentItinaryService = frequentItinaryService;
        this.frequentItinaryRepository = frequentItinaryRepository;
    }

    /**
     * {@code POST  /frequent-itinaries} : Create a new frequentItinary.
     *
     * @param frequentItinaryDTO the frequentItinaryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new frequentItinaryDTO, or with status {@code 400 (Bad Request)} if the frequentItinary has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<FrequentItinaryDTO>> createFrequentItinary(@Valid @RequestBody FrequentItinaryDTO frequentItinaryDTO)
        throws URISyntaxException {
        log.debug("REST request to save FrequentItinary : {}", frequentItinaryDTO);
        if (frequentItinaryDTO.getId() != null) {
            throw new BadRequestAlertException("A new frequentItinary cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return frequentItinaryService
            .save(frequentItinaryDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/frequent-itinaries/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /frequent-itinaries/:id} : Updates an existing frequentItinary.
     *
     * @param id the id of the frequentItinaryDTO to save.
     * @param frequentItinaryDTO the frequentItinaryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated frequentItinaryDTO,
     * or with status {@code 400 (Bad Request)} if the frequentItinaryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the frequentItinaryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<FrequentItinaryDTO>> updateFrequentItinary(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FrequentItinaryDTO frequentItinaryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FrequentItinary : {}, {}", id, frequentItinaryDTO);
        if (frequentItinaryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, frequentItinaryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return frequentItinaryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return frequentItinaryService
                    .update(frequentItinaryDTO)
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
     * {@code PATCH  /frequent-itinaries/:id} : Partial updates given fields of an existing frequentItinary, field will ignore if it is null
     *
     * @param id the id of the frequentItinaryDTO to save.
     * @param frequentItinaryDTO the frequentItinaryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated frequentItinaryDTO,
     * or with status {@code 400 (Bad Request)} if the frequentItinaryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the frequentItinaryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the frequentItinaryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<FrequentItinaryDTO>> partialUpdateFrequentItinary(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FrequentItinaryDTO frequentItinaryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FrequentItinary partially : {}, {}", id, frequentItinaryDTO);
        if (frequentItinaryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, frequentItinaryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return frequentItinaryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<FrequentItinaryDTO> result = frequentItinaryService.partialUpdate(frequentItinaryDTO);

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
     * {@code GET  /frequent-itinaries} : get all the frequentItinaries.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of frequentItinaries in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<FrequentItinaryDTO>> getAllFrequentItinaries() {
        log.debug("REST request to get all FrequentItinaries");
        return frequentItinaryService.findAll().collectList();
    }

    /**
     * {@code GET  /frequent-itinaries} : get all the frequentItinaries as a stream.
     * @return the {@link Flux} of frequentItinaries.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<FrequentItinaryDTO> getAllFrequentItinariesAsStream() {
        log.debug("REST request to get all FrequentItinaries as a stream");
        return frequentItinaryService.findAll();
    }

    /**
     * {@code GET  /frequent-itinaries/:id} : get the "id" frequentItinary.
     *
     * @param id the id of the frequentItinaryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the frequentItinaryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<FrequentItinaryDTO>> getFrequentItinary(@PathVariable("id") Long id) {
        log.debug("REST request to get FrequentItinary : {}", id);
        Mono<FrequentItinaryDTO> frequentItinaryDTO = frequentItinaryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(frequentItinaryDTO);
    }

    /**
     * {@code DELETE  /frequent-itinaries/:id} : delete the "id" frequentItinary.
     *
     * @param id the id of the frequentItinaryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteFrequentItinary(@PathVariable("id") Long id) {
        log.debug("REST request to delete FrequentItinary : {}", id);
        return frequentItinaryService
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
