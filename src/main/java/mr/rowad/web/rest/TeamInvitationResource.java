package mr.rowad.web.rest;

import com.codahale.metrics.annotation.Timed;
import mr.rowad.domain.TeamInvitation;
import mr.rowad.service.TeamInvitationService;
import mr.rowad.web.rest.errors.BadRequestAlertException;
import mr.rowad.web.rest.util.HeaderUtil;
import mr.rowad.service.dto.TeamInvitationCriteria;
import mr.rowad.service.TeamInvitationQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TeamInvitation.
 */
@RestController
@RequestMapping("/api")
public class TeamInvitationResource {

    private final Logger log = LoggerFactory.getLogger(TeamInvitationResource.class);

    private static final String ENTITY_NAME = "teamInvitation";

    private final TeamInvitationService teamInvitationService;

    private final TeamInvitationQueryService teamInvitationQueryService;

    public TeamInvitationResource(TeamInvitationService teamInvitationService, TeamInvitationQueryService teamInvitationQueryService) {
        this.teamInvitationService = teamInvitationService;
        this.teamInvitationQueryService = teamInvitationQueryService;
    }

    /**
     * POST  /team-invitations : Create a new teamInvitation.
     *
     * @param teamInvitation the teamInvitation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new teamInvitation, or with status 400 (Bad Request) if the teamInvitation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/team-invitations")
    @Timed
    public ResponseEntity<TeamInvitation> createTeamInvitation(@RequestBody TeamInvitation teamInvitation) throws URISyntaxException {
        log.debug("REST request to save TeamInvitation : {}", teamInvitation);
        if (teamInvitation.getId() != null) {
            throw new BadRequestAlertException("A new teamInvitation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TeamInvitation result = teamInvitationService.save(teamInvitation);
        return ResponseEntity.created(new URI("/api/team-invitations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /team-invitations : Updates an existing teamInvitation.
     *
     * @param teamInvitation the teamInvitation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated teamInvitation,
     * or with status 400 (Bad Request) if the teamInvitation is not valid,
     * or with status 500 (Internal Server Error) if the teamInvitation couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/team-invitations")
    @Timed
    public ResponseEntity<TeamInvitation> updateTeamInvitation(@RequestBody TeamInvitation teamInvitation) throws URISyntaxException {
        log.debug("REST request to update TeamInvitation : {}", teamInvitation);
        if (teamInvitation.getId() == null) {
            return createTeamInvitation(teamInvitation);
        }
        TeamInvitation result = teamInvitationService.save(teamInvitation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, teamInvitation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /team-invitations : get all the teamInvitations.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of teamInvitations in body
     */
    @GetMapping("/team-invitations")
    @Timed
    public ResponseEntity<List<TeamInvitation>> getAllTeamInvitations(TeamInvitationCriteria criteria) {
        log.debug("REST request to get TeamInvitations by criteria: {}", criteria);
        List<TeamInvitation> entityList = teamInvitationQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * GET  /team-invitations/:id : get the "id" teamInvitation.
     *
     * @param id the id of the teamInvitation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the teamInvitation, or with status 404 (Not Found)
     */
    @GetMapping("/team-invitations/{id}")
    @Timed
    public ResponseEntity<TeamInvitation> getTeamInvitation(@PathVariable Long id) {
        log.debug("REST request to get TeamInvitation : {}", id);
        TeamInvitation teamInvitation = teamInvitationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(teamInvitation));
    }

    /**
     * DELETE  /team-invitations/:id : delete the "id" teamInvitation.
     *
     * @param id the id of the teamInvitation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/team-invitations/{id}")
    @Timed
    public ResponseEntity<Void> deleteTeamInvitation(@PathVariable Long id) {
        log.debug("REST request to delete TeamInvitation : {}", id);
        teamInvitationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
