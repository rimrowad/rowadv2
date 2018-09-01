package mr.rowad.service;

import mr.rowad.domain.TeamMember;
import mr.rowad.repository.TeamMemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing TeamMember.
 */
@Service
@Transactional
public class TeamMemberService {

    private final Logger log = LoggerFactory.getLogger(TeamMemberService.class);

    private final TeamMemberRepository teamMemberRepository;

    public TeamMemberService(TeamMemberRepository teamMemberRepository) {
        this.teamMemberRepository = teamMemberRepository;
    }

    /**
     * Save a teamMember.
     *
     * @param teamMember the entity to save
     * @return the persisted entity
     */
    public TeamMember save(TeamMember teamMember) {
        log.debug("Request to save TeamMember : {}", teamMember);
        return teamMemberRepository.save(teamMember);
    }

    /**
     * Get all the teamMembers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TeamMember> findAll(Pageable pageable) {
        log.debug("Request to get all TeamMembers");
        return teamMemberRepository.findAll(pageable);
    }

    /**
     * Get one teamMember by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public TeamMember findOne(Long id) {
        log.debug("Request to get TeamMember : {}", id);
        return teamMemberRepository.findOne(id);
    }

    /**
     * Delete the teamMember by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TeamMember : {}", id);
        teamMemberRepository.delete(id);
    }
}
