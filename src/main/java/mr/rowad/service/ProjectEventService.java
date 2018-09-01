package mr.rowad.service;

import mr.rowad.domain.ProjectEvent;
import mr.rowad.repository.ProjectEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing ProjectEvent.
 */
@Service
@Transactional
public class ProjectEventService {

    private final Logger log = LoggerFactory.getLogger(ProjectEventService.class);

    private final ProjectEventRepository projectEventRepository;

    public ProjectEventService(ProjectEventRepository projectEventRepository) {
        this.projectEventRepository = projectEventRepository;
    }

    /**
     * Save a projectEvent.
     *
     * @param projectEvent the entity to save
     * @return the persisted entity
     */
    public ProjectEvent save(ProjectEvent projectEvent) {
        log.debug("Request to save ProjectEvent : {}", projectEvent);
        return projectEventRepository.save(projectEvent);
    }

    /**
     * Get all the projectEvents.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ProjectEvent> findAll(Pageable pageable) {
        log.debug("Request to get all ProjectEvents");
        return projectEventRepository.findAll(pageable);
    }

    /**
     * Get one projectEvent by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ProjectEvent findOne(Long id) {
        log.debug("Request to get ProjectEvent : {}", id);
        return projectEventRepository.findOne(id);
    }

    /**
     * Delete the projectEvent by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProjectEvent : {}", id);
        projectEventRepository.delete(id);
    }
}
