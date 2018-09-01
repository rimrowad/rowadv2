package mr.rowad.web.rest;

import com.codahale.metrics.annotation.Timed;
import mr.rowad.domain.ProjectEvent;
import mr.rowad.service.ProjectEventService;
import mr.rowad.web.rest.errors.BadRequestAlertException;
import mr.rowad.web.rest.util.HeaderUtil;
import mr.rowad.web.rest.util.PaginationUtil;
import mr.rowad.service.dto.ProjectEventCriteria;
import mr.rowad.service.ProjectEventQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ProjectEvent.
 */
@RestController
@RequestMapping("/api")
public class ProjectEventResource {

    private final Logger log = LoggerFactory.getLogger(ProjectEventResource.class);

    private static final String ENTITY_NAME = "projectEvent";

    private final ProjectEventService projectEventService;

    private final ProjectEventQueryService projectEventQueryService;

    public ProjectEventResource(ProjectEventService projectEventService, ProjectEventQueryService projectEventQueryService) {
        this.projectEventService = projectEventService;
        this.projectEventQueryService = projectEventQueryService;
    }

    /**
     * POST  /project-events : Create a new projectEvent.
     *
     * @param projectEvent the projectEvent to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projectEvent, or with status 400 (Bad Request) if the projectEvent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/project-events")
    @Timed
    public ResponseEntity<ProjectEvent> createProjectEvent(@RequestBody ProjectEvent projectEvent) throws URISyntaxException {
        log.debug("REST request to save ProjectEvent : {}", projectEvent);
        if (projectEvent.getId() != null) {
            throw new BadRequestAlertException("A new projectEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProjectEvent result = projectEventService.save(projectEvent);
        return ResponseEntity.created(new URI("/api/project-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /project-events : Updates an existing projectEvent.
     *
     * @param projectEvent the projectEvent to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectEvent,
     * or with status 400 (Bad Request) if the projectEvent is not valid,
     * or with status 500 (Internal Server Error) if the projectEvent couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/project-events")
    @Timed
    public ResponseEntity<ProjectEvent> updateProjectEvent(@RequestBody ProjectEvent projectEvent) throws URISyntaxException {
        log.debug("REST request to update ProjectEvent : {}", projectEvent);
        if (projectEvent.getId() == null) {
            return createProjectEvent(projectEvent);
        }
        ProjectEvent result = projectEventService.save(projectEvent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, projectEvent.getId().toString()))
            .body(result);
    }

    /**
     * GET  /project-events : get all the projectEvents.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of projectEvents in body
     */
    @GetMapping("/project-events")
    @Timed
    public ResponseEntity<List<ProjectEvent>> getAllProjectEvents(ProjectEventCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ProjectEvents by criteria: {}", criteria);
        Page<ProjectEvent> page = projectEventQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/project-events");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /project-events/:id : get the "id" projectEvent.
     *
     * @param id the id of the projectEvent to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectEvent, or with status 404 (Not Found)
     */
    @GetMapping("/project-events/{id}")
    @Timed
    public ResponseEntity<ProjectEvent> getProjectEvent(@PathVariable Long id) {
        log.debug("REST request to get ProjectEvent : {}", id);
        ProjectEvent projectEvent = projectEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(projectEvent));
    }

    /**
     * DELETE  /project-events/:id : delete the "id" projectEvent.
     *
     * @param id the id of the projectEvent to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/project-events/{id}")
    @Timed
    public ResponseEntity<Void> deleteProjectEvent(@PathVariable Long id) {
        log.debug("REST request to delete ProjectEvent : {}", id);
        projectEventService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
