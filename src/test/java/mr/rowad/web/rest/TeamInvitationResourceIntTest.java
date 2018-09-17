package mr.rowad.web.rest;

import static mr.rowad.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import mr.rowad.RowadApp;
import mr.rowad.domain.Team;
import mr.rowad.domain.TeamInvitation;
import mr.rowad.domain.TeamMember;
import mr.rowad.repository.TeamInvitationRepository;
import mr.rowad.repository.TeamMemberRepository;
import mr.rowad.service.TeamInvitationQueryService;
import mr.rowad.service.TeamInvitationService;
import mr.rowad.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the TeamInvitationResource REST controller.
 *
 * @see TeamInvitationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RowadApp.class)
public class TeamInvitationResourceIntTest {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private TeamInvitationRepository teamInvitationRepository;

    @Autowired
    private TeamInvitationService teamInvitationService;

    @Autowired
    private TeamInvitationQueryService teamInvitationQueryService;

    @Autowired
    private TeamMemberRepository memberRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTeamInvitationMockMvc;

    private TeamInvitation teamInvitation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TeamInvitationResource teamInvitationResource = new TeamInvitationResource(teamInvitationService, memberRepository, teamInvitationQueryService);
        restTeamInvitationMockMvc = MockMvcBuilders.standaloneSetup(teamInvitationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TeamInvitation createEntity(EntityManager em) {
        TeamInvitation teamInvitation = new TeamInvitation()
            .status(DEFAULT_STATUS)
            .date(DEFAULT_DATE);
        return teamInvitation;
    }

    @Before
    public void initTest() {
        teamInvitation = createEntity(em);
    }

    @Test
    @Transactional
    public void createTeamInvitation() throws Exception {
        int databaseSizeBeforeCreate = teamInvitationRepository.findAll().size();

        // Create the TeamInvitation
        restTeamInvitationMockMvc.perform(post("/api/team-invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamInvitation)))
            .andExpect(status().isCreated());

        // Validate the TeamInvitation in the database
        List<TeamInvitation> teamInvitationList = teamInvitationRepository.findAll();
        assertThat(teamInvitationList).hasSize(databaseSizeBeforeCreate + 1);
        TeamInvitation testTeamInvitation = teamInvitationList.get(teamInvitationList.size() - 1);
        assertThat(testTeamInvitation.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTeamInvitation.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createTeamInvitationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = teamInvitationRepository.findAll().size();

        // Create the TeamInvitation with an existing ID
        teamInvitation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeamInvitationMockMvc.perform(post("/api/team-invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamInvitation)))
            .andExpect(status().isBadRequest());

        // Validate the TeamInvitation in the database
        List<TeamInvitation> teamInvitationList = teamInvitationRepository.findAll();
        assertThat(teamInvitationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTeamInvitations() throws Exception {
        // Initialize the database
        teamInvitationRepository.saveAndFlush(teamInvitation);

        // Get all the teamInvitationList
        restTeamInvitationMockMvc.perform(get("/api/team-invitations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teamInvitation.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    public void getTeamInvitation() throws Exception {
        // Initialize the database
        teamInvitationRepository.saveAndFlush(teamInvitation);

        // Get the teamInvitation
        restTeamInvitationMockMvc.perform(get("/api/team-invitations/{id}", teamInvitation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(teamInvitation.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllTeamInvitationsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        teamInvitationRepository.saveAndFlush(teamInvitation);

        // Get all the teamInvitationList where status equals to DEFAULT_STATUS
        defaultTeamInvitationShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the teamInvitationList where status equals to UPDATED_STATUS
        defaultTeamInvitationShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllTeamInvitationsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        teamInvitationRepository.saveAndFlush(teamInvitation);

        // Get all the teamInvitationList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultTeamInvitationShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the teamInvitationList where status equals to UPDATED_STATUS
        defaultTeamInvitationShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllTeamInvitationsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        teamInvitationRepository.saveAndFlush(teamInvitation);

        // Get all the teamInvitationList where status is not null
        defaultTeamInvitationShouldBeFound("status.specified=true");

        // Get all the teamInvitationList where status is null
        defaultTeamInvitationShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeamInvitationsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        teamInvitationRepository.saveAndFlush(teamInvitation);

        // Get all the teamInvitationList where date equals to DEFAULT_DATE
        defaultTeamInvitationShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the teamInvitationList where date equals to UPDATED_DATE
        defaultTeamInvitationShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllTeamInvitationsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        teamInvitationRepository.saveAndFlush(teamInvitation);

        // Get all the teamInvitationList where date in DEFAULT_DATE or UPDATED_DATE
        defaultTeamInvitationShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the teamInvitationList where date equals to UPDATED_DATE
        defaultTeamInvitationShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllTeamInvitationsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        teamInvitationRepository.saveAndFlush(teamInvitation);

        // Get all the teamInvitationList where date is not null
        defaultTeamInvitationShouldBeFound("date.specified=true");

        // Get all the teamInvitationList where date is null
        defaultTeamInvitationShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeamInvitationsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teamInvitationRepository.saveAndFlush(teamInvitation);

        // Get all the teamInvitationList where date greater than or equals to DEFAULT_DATE
        defaultTeamInvitationShouldBeFound("date.greaterOrEqualThan=" + DEFAULT_DATE);

        // Get all the teamInvitationList where date greater than or equals to UPDATED_DATE
        defaultTeamInvitationShouldNotBeFound("date.greaterOrEqualThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllTeamInvitationsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        teamInvitationRepository.saveAndFlush(teamInvitation);

        // Get all the teamInvitationList where date less than or equals to DEFAULT_DATE
        defaultTeamInvitationShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the teamInvitationList where date less than or equals to UPDATED_DATE
        defaultTeamInvitationShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }


    @Test
    @Transactional
    public void getAllTeamInvitationsBySenderIsEqualToSomething() throws Exception {
        // Initialize the database
        TeamMember sender = TeamMemberResourceIntTest.createEntity(em);
        em.persist(sender);
        em.flush();
        teamInvitation.setSender(sender);
        teamInvitationRepository.saveAndFlush(teamInvitation);
        Long senderId = sender.getId();

        // Get all the teamInvitationList where sender equals to senderId
        defaultTeamInvitationShouldBeFound("senderId.equals=" + senderId);

        // Get all the teamInvitationList where sender equals to senderId + 1
        defaultTeamInvitationShouldNotBeFound("senderId.equals=" + (senderId + 1));
    }


    @Test
    @Transactional
    public void getAllTeamInvitationsByReceiverIsEqualToSomething() throws Exception {
        // Initialize the database
        TeamMember receiver = TeamMemberResourceIntTest.createEntity(em);
        em.persist(receiver);
        em.flush();
        teamInvitation.setReceiver(receiver);
        teamInvitationRepository.saveAndFlush(teamInvitation);
        Long receiverId = receiver.getId();

        // Get all the teamInvitationList where receiver equals to receiverId
        defaultTeamInvitationShouldBeFound("receiverId.equals=" + receiverId);

        // Get all the teamInvitationList where receiver equals to receiverId + 1
        defaultTeamInvitationShouldNotBeFound("receiverId.equals=" + (receiverId + 1));
    }


    @Test
    @Transactional
    public void getAllTeamInvitationsByTeamIsEqualToSomething() throws Exception {
        // Initialize the database
        Team team = TeamResourceIntTest.createEntity(em);
        em.persist(team);
        em.flush();
        teamInvitation.setTeam(team);
        teamInvitationRepository.saveAndFlush(teamInvitation);
        Long teamId = team.getId();

        // Get all the teamInvitationList where team equals to teamId
        defaultTeamInvitationShouldBeFound("teamId.equals=" + teamId);

        // Get all the teamInvitationList where team equals to teamId + 1
        defaultTeamInvitationShouldNotBeFound("teamId.equals=" + (teamId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTeamInvitationShouldBeFound(String filter) throws Exception {
        restTeamInvitationMockMvc.perform(get("/api/team-invitations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teamInvitation.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultTeamInvitationShouldNotBeFound(String filter) throws Exception {
        restTeamInvitationMockMvc.perform(get("/api/team-invitations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingTeamInvitation() throws Exception {
        // Get the teamInvitation
        restTeamInvitationMockMvc.perform(get("/api/team-invitations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTeamInvitation() throws Exception {
        // Initialize the database
        teamInvitationService.save(teamInvitation);

        int databaseSizeBeforeUpdate = teamInvitationRepository.findAll().size();

        // Update the teamInvitation
        TeamInvitation updatedTeamInvitation = teamInvitationRepository.findOne(teamInvitation.getId());
        // Disconnect from session so that the updates on updatedTeamInvitation are not directly saved in db
        em.detach(updatedTeamInvitation);
        updatedTeamInvitation
            .status(UPDATED_STATUS)
            .date(UPDATED_DATE);

        restTeamInvitationMockMvc.perform(put("/api/team-invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTeamInvitation)))
            .andExpect(status().isOk());

        // Validate the TeamInvitation in the database
        List<TeamInvitation> teamInvitationList = teamInvitationRepository.findAll();
        assertThat(teamInvitationList).hasSize(databaseSizeBeforeUpdate);
        TeamInvitation testTeamInvitation = teamInvitationList.get(teamInvitationList.size() - 1);
        assertThat(testTeamInvitation.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTeamInvitation.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingTeamInvitation() throws Exception {
        int databaseSizeBeforeUpdate = teamInvitationRepository.findAll().size();

        // Create the TeamInvitation

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTeamInvitationMockMvc.perform(put("/api/team-invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamInvitation)))
            .andExpect(status().isCreated());

        // Validate the TeamInvitation in the database
        List<TeamInvitation> teamInvitationList = teamInvitationRepository.findAll();
        assertThat(teamInvitationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTeamInvitation() throws Exception {
        // Initialize the database
        teamInvitationService.save(teamInvitation);

        int databaseSizeBeforeDelete = teamInvitationRepository.findAll().size();

        // Get the teamInvitation
        restTeamInvitationMockMvc.perform(delete("/api/team-invitations/{id}", teamInvitation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TeamInvitation> teamInvitationList = teamInvitationRepository.findAll();
        assertThat(teamInvitationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeamInvitation.class);
        TeamInvitation teamInvitation1 = new TeamInvitation();
        teamInvitation1.setId(1L);
        TeamInvitation teamInvitation2 = new TeamInvitation();
        teamInvitation2.setId(teamInvitation1.getId());
        assertThat(teamInvitation1).isEqualTo(teamInvitation2);
        teamInvitation2.setId(2L);
        assertThat(teamInvitation1).isNotEqualTo(teamInvitation2);
        teamInvitation1.setId(null);
        assertThat(teamInvitation1).isNotEqualTo(teamInvitation2);
    }
}
