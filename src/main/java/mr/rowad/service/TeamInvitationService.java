package mr.rowad.service;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mr.rowad.domain.TeamInvitation;
import mr.rowad.domain.TeamMember;
import mr.rowad.repository.TeamInvitationRepository;
import mr.rowad.repository.TeamMemberRepository;


/**
 * Service Implementation for managing TeamInvitation.
 */
@Service
@Transactional
public class TeamInvitationService {

    private final Logger log = LoggerFactory.getLogger(TeamInvitationService.class);

    private final TeamInvitationRepository teamInvitationRepository;
    private final TeamMemberRepository memberRepository;

    public TeamInvitationService(TeamInvitationRepository teamInvitationRepository,
            TeamMemberRepository memberRepository) {
        this.teamInvitationRepository = teamInvitationRepository;
        this.memberRepository = memberRepository;
    }

    /**
     * Save a teamInvitation.
     *
     * @param teamInvitation the entity to save
     * @return the persisted entity
     */
    public TeamInvitation save(TeamInvitation teamInvitation) {

        TeamMember sender = memberRepository.findByUserIsCurrentUser().get(0);
        teamInvitation.setSender(sender);
        teamInvitation.setTeam(sender.getTeam());
        teamInvitation.setStatus("En cours");
        teamInvitation.setDate(LocalDate.now());
        log.debug("Request to save TeamInvitation : {}", teamInvitation);
        return teamInvitationRepository.save(teamInvitation);
    }

    public TeamInvitation acceptTeamInvitation(TeamInvitation teamInvitation) {
        teamInvitation.getReceiver().setTeam(teamInvitation.getTeam());
        memberRepository.save(teamInvitation.getReceiver());
        teamInvitation.setStatus("Acceptée");
        return teamInvitationRepository.save(teamInvitation);
    }

    public TeamInvitation declineTeamInvitation(TeamInvitation teamInvitation) {
        teamInvitation.getReceiver().setTeam(teamInvitation.getTeam());
        memberRepository.save(teamInvitation.getReceiver());
        teamInvitation.setStatus("Refusée");
        return teamInvitationRepository.save(teamInvitation);
    }


    /**
     * Get all the teamInvitations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TeamInvitation> findAll(Pageable pageable) {
        log.debug("Request to get all TeamInvitations");
        return teamInvitationRepository.findAll(pageable);
    }

    /**
     * Get one teamInvitation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public TeamInvitation findOne(Long id) {
        log.debug("Request to get TeamInvitation : {}", id);
        return teamInvitationRepository.findOne(id);
    }

    /**
     * Delete the teamInvitation by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TeamInvitation : {}", id);
        teamInvitationRepository.delete(id);
    }
}
