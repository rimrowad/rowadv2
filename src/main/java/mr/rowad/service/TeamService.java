package mr.rowad.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mr.rowad.domain.Team;
import mr.rowad.domain.TeamMember;
import mr.rowad.repository.TeamMemberRepository;
import mr.rowad.repository.TeamRepository;


/**
 * Service Implementation for managing Team.
 */
@Service
@Transactional
public class TeamService {

    private final Logger log = LoggerFactory.getLogger(TeamService.class);

    private final TeamRepository teamRepository;
    private final TeamMemberRepository memberRepository;
    private final UserService userService;



    public TeamService(TeamRepository teamRepository, TeamMemberRepository memberRepository, UserService userService) {
        this.teamRepository = teamRepository;
        this.memberRepository = memberRepository;
        this.userService = userService;
    }

    /**
     * Save a team.
     *
     * @param team the entity to save
     * @return the persisted entity
     */
    public Team save(Team team) {
        log.debug("Request to save Team : {}", team);
        if(team.getId() == null || team.getId() <= 0) {
            team.setCreationDate(LocalDate.now());

        }
        Team dbTeam = teamRepository.save(team.owner(userService.getCurrentUser().get()));
        List<TeamMember> list = memberRepository.findByUserIsCurrentUser();
        TeamMember teamMember = list.get(0);
        teamMember.setTeam(dbTeam);
        memberRepository.save(teamMember);
        return dbTeam;
    }

    /**
     * Get all the teams.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Team> findAll(Pageable pageable) {
        log.debug("Request to get all Teams");
        return teamRepository.findAll(pageable);
    }

    /**
     * Get one team by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Team findOne(Long id) {
        log.debug("Request to get Team : {}", id);
        return teamRepository.findOne(id);
    }

    /**
     * Delete the team by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Team : {}", id);
        teamRepository.delete(id);
    }
}
