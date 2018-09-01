package mr.rowad.web.rest;

import com.codahale.metrics.annotation.Timed;
import mr.rowad.domain.ProjectFile;
import mr.rowad.service.ProjectFileService;
import mr.rowad.web.rest.errors.BadRequestAlertException;
import mr.rowad.web.rest.util.HeaderUtil;
import mr.rowad.web.rest.util.PaginationUtil;
import mr.rowad.service.dto.ProjectFileCriteria;
import mr.rowad.service.ProjectFileQueryService;
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
 * REST controller for managing ProjectFile.
 */
@RestController
@RequestMapping("/api")
public class ProjectFileResource {

    private final Logger log = LoggerFactory.getLogger(ProjectFileResource.class);

    private static final String ENTITY_NAME = "projectFile";

    private final ProjectFileService projectFileService;

    private final ProjectFileQueryService projectFileQueryService;

    public ProjectFileResource(ProjectFileService projectFileService, ProjectFileQueryService projectFileQueryService) {
        this.projectFileService = projectFileService;
        this.projectFileQueryService = projectFileQueryService;
    }

    /**
     * POST  /project-files : Create a new projectFile.
     *
     * @param projectFile the projectFile to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projectFile, or with status 400 (Bad Request) if the projectFile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/project-files")
    @Timed
    public ResponseEntity<ProjectFile> createProjectFile(@RequestBody ProjectFile projectFile) throws URISyntaxException {
        log.debug("REST request to save ProjectFile : {}", projectFile);
        if (projectFile.getId() != null) {
            throw new BadRequestAlertException("A new projectFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProjectFile result = projectFileService.save(projectFile);
        return ResponseEntity.created(new URI("/api/project-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /project-files : Updates an existing projectFile.
     *
     * @param projectFile the projectFile to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectFile,
     * or with status 400 (Bad Request) if the projectFile is not valid,
     * or with status 500 (Internal Server Error) if the projectFile couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/project-files")
    @Timed
    public ResponseEntity<ProjectFile> updateProjectFile(@RequestBody ProjectFile projectFile) throws URISyntaxException {
        log.debug("REST request to update ProjectFile : {}", projectFile);
        if (projectFile.getId() == null) {
            return createProjectFile(projectFile);
        }
        ProjectFile result = projectFileService.save(projectFile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, projectFile.getId().toString()))
            .body(result);
    }

    /**
     * GET  /project-files : get all the projectFiles.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of projectFiles in body
     */
    @GetMapping("/project-files")
    @Timed
    public ResponseEntity<List<ProjectFile>> getAllProjectFiles(ProjectFileCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ProjectFiles by criteria: {}", criteria);
        Page<ProjectFile> page = projectFileQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/project-files");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /project-files/:id : get the "id" projectFile.
     *
     * @param id the id of the projectFile to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectFile, or with status 404 (Not Found)
     */
    @GetMapping("/project-files/{id}")
    @Timed
    public ResponseEntity<ProjectFile> getProjectFile(@PathVariable Long id) {
        log.debug("REST request to get ProjectFile : {}", id);
        ProjectFile projectFile = projectFileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(projectFile));
    }

    /**
     * DELETE  /project-files/:id : delete the "id" projectFile.
     *
     * @param id the id of the projectFile to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/project-files/{id}")
    @Timed
    public ResponseEntity<Void> deleteProjectFile(@PathVariable Long id) {
        log.debug("REST request to delete ProjectFile : {}", id);
        projectFileService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
