package mr.rowad.web.rest;

import mr.rowad.RowadApp;

import mr.rowad.domain.TeamMember;
import mr.rowad.domain.Team;
import mr.rowad.domain.User;
import mr.rowad.repository.TeamMemberRepository;
import mr.rowad.service.TeamMemberService;
import mr.rowad.web.rest.errors.ExceptionTranslator;
import mr.rowad.service.dto.TeamMemberCriteria;
import mr.rowad.service.TeamMemberQueryService;

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

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static mr.rowad.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TeamMemberResource REST controller.
 *
 * @see TeamMemberResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RowadApp.class)
public class TeamMemberResourceIntTest {

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DIPLOME = "AAAAAAAAAA";
    private static final String UPDATED_DIPLOME = "BBBBBBBBBB";

    private static final String DEFAULT_RESUME = "AAAAAAAAAA";
    private static final String UPDATED_RESUME = "BBBBBBBBBB";

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private TeamMemberService teamMemberService;

    @Autowired
    private TeamMemberQueryService teamMemberQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTeamMemberMockMvc;

    private TeamMember teamMember;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TeamMemberResource teamMemberResource = new TeamMemberResource(teamMemberService, teamMemberQueryService);
        this.restTeamMemberMockMvc = MockMvcBuilders.standaloneSetup(teamMemberResource)
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
    public static TeamMember createEntity(EntityManager em) {
        TeamMember teamMember = new TeamMember()
            .address(DEFAULT_ADDRESS)
            .phone(DEFAULT_PHONE)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .diplome(DEFAULT_DIPLOME)
            .resume(DEFAULT_RESUME);
        return teamMember;
    }

    @Before
    public void initTest() {
        teamMember = createEntity(em);
    }

    @Test
    @Transactional
    public void createTeamMember() throws Exception {
        int databaseSizeBeforeCreate = teamMemberRepository.findAll().size();

        // Create the TeamMember
        restTeamMemberMockMvc.perform(post("/api/team-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamMember)))
            .andExpect(status().isCreated());

        // Validate the TeamMember in the database
        List<TeamMember> teamMemberList = teamMemberRepository.findAll();
        assertThat(teamMemberList).hasSize(databaseSizeBeforeCreate + 1);
        TeamMember testTeamMember = teamMemberList.get(teamMemberList.size() - 1);
        assertThat(testTeamMember.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testTeamMember.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testTeamMember.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testTeamMember.getDiplome()).isEqualTo(DEFAULT_DIPLOME);
        assertThat(testTeamMember.getResume()).isEqualTo(DEFAULT_RESUME);
    }

    @Test
    @Transactional
    public void createTeamMemberWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = teamMemberRepository.findAll().size();

        // Create the TeamMember with an existing ID
        teamMember.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeamMemberMockMvc.perform(post("/api/team-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamMember)))
            .andExpect(status().isBadRequest());

        // Validate the TeamMember in the database
        List<TeamMember> teamMemberList = teamMemberRepository.findAll();
        assertThat(teamMemberList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTeamMembers() throws Exception {
        // Initialize the database
        teamMemberRepository.saveAndFlush(teamMember);

        // Get all the teamMemberList
        restTeamMemberMockMvc.perform(get("/api/team-members?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teamMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].diplome").value(hasItem(DEFAULT_DIPLOME.toString())))
            .andExpect(jsonPath("$.[*].resume").value(hasItem(DEFAULT_RESUME.toString())));
    }

    @Test
    @Transactional
    public void getTeamMember() throws Exception {
        // Initialize the database
        teamMemberRepository.saveAndFlush(teamMember);

        // Get the teamMember
        restTeamMemberMockMvc.perform(get("/api/team-members/{id}", teamMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(teamMember.getId().intValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.diplome").value(DEFAULT_DIPLOME.toString()))
            .andExpect(jsonPath("$.resume").value(DEFAULT_RESUME.toString()));
    }

    @Test
    @Transactional
    public void getAllTeamMembersByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        teamMemberRepository.saveAndFlush(teamMember);

        // Get all the teamMemberList where address equals to DEFAULT_ADDRESS
        defaultTeamMemberShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the teamMemberList where address equals to UPDATED_ADDRESS
        defaultTeamMemberShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllTeamMembersByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        teamMemberRepository.saveAndFlush(teamMember);

        // Get all the teamMemberList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultTeamMemberShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the teamMemberList where address equals to UPDATED_ADDRESS
        defaultTeamMemberShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllTeamMembersByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        teamMemberRepository.saveAndFlush(teamMember);

        // Get all the teamMemberList where address is not null
        defaultTeamMemberShouldBeFound("address.specified=true");

        // Get all the teamMemberList where address is null
        defaultTeamMemberShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeamMembersByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        teamMemberRepository.saveAndFlush(teamMember);

        // Get all the teamMemberList where phone equals to DEFAULT_PHONE
        defaultTeamMemberShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the teamMemberList where phone equals to UPDATED_PHONE
        defaultTeamMemberShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllTeamMembersByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        teamMemberRepository.saveAndFlush(teamMember);

        // Get all the teamMemberList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultTeamMemberShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the teamMemberList where phone equals to UPDATED_PHONE
        defaultTeamMemberShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllTeamMembersByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        teamMemberRepository.saveAndFlush(teamMember);

        // Get all the teamMemberList where phone is not null
        defaultTeamMemberShouldBeFound("phone.specified=true");

        // Get all the teamMemberList where phone is null
        defaultTeamMemberShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeamMembersByDateOfBirthIsEqualToSomething() throws Exception {
        // Initialize the database
        teamMemberRepository.saveAndFlush(teamMember);

        // Get all the teamMemberList where dateOfBirth equals to DEFAULT_DATE_OF_BIRTH
        defaultTeamMemberShouldBeFound("dateOfBirth.equals=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the teamMemberList where dateOfBirth equals to UPDATED_DATE_OF_BIRTH
        defaultTeamMemberShouldNotBeFound("dateOfBirth.equals=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllTeamMembersByDateOfBirthIsInShouldWork() throws Exception {
        // Initialize the database
        teamMemberRepository.saveAndFlush(teamMember);

        // Get all the teamMemberList where dateOfBirth in DEFAULT_DATE_OF_BIRTH or UPDATED_DATE_OF_BIRTH
        defaultTeamMemberShouldBeFound("dateOfBirth.in=" + DEFAULT_DATE_OF_BIRTH + "," + UPDATED_DATE_OF_BIRTH);

        // Get all the teamMemberList where dateOfBirth equals to UPDATED_DATE_OF_BIRTH
        defaultTeamMemberShouldNotBeFound("dateOfBirth.in=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllTeamMembersByDateOfBirthIsNullOrNotNull() throws Exception {
        // Initialize the database
        teamMemberRepository.saveAndFlush(teamMember);

        // Get all the teamMemberList where dateOfBirth is not null
        defaultTeamMemberShouldBeFound("dateOfBirth.specified=true");

        // Get all the teamMemberList where dateOfBirth is null
        defaultTeamMemberShouldNotBeFound("dateOfBirth.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeamMembersByDateOfBirthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teamMemberRepository.saveAndFlush(teamMember);

        // Get all the teamMemberList where dateOfBirth greater than or equals to DEFAULT_DATE_OF_BIRTH
        defaultTeamMemberShouldBeFound("dateOfBirth.greaterOrEqualThan=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the teamMemberList where dateOfBirth greater than or equals to UPDATED_DATE_OF_BIRTH
        defaultTeamMemberShouldNotBeFound("dateOfBirth.greaterOrEqualThan=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllTeamMembersByDateOfBirthIsLessThanSomething() throws Exception {
        // Initialize the database
        teamMemberRepository.saveAndFlush(teamMember);

        // Get all the teamMemberList where dateOfBirth less than or equals to DEFAULT_DATE_OF_BIRTH
        defaultTeamMemberShouldNotBeFound("dateOfBirth.lessThan=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the teamMemberList where dateOfBirth less than or equals to UPDATED_DATE_OF_BIRTH
        defaultTeamMemberShouldBeFound("dateOfBirth.lessThan=" + UPDATED_DATE_OF_BIRTH);
    }


    @Test
    @Transactional
    public void getAllTeamMembersByDiplomeIsEqualToSomething() throws Exception {
        // Initialize the database
        teamMemberRepository.saveAndFlush(teamMember);

        // Get all the teamMemberList where diplome equals to DEFAULT_DIPLOME
        defaultTeamMemberShouldBeFound("diplome.equals=" + DEFAULT_DIPLOME);

        // Get all the teamMemberList where diplome equals to UPDATED_DIPLOME
        defaultTeamMemberShouldNotBeFound("diplome.equals=" + UPDATED_DIPLOME);
    }

    @Test
    @Transactional
    public void getAllTeamMembersByDiplomeIsInShouldWork() throws Exception {
        // Initialize the database
        teamMemberRepository.saveAndFlush(teamMember);

        // Get all the teamMemberList where diplome in DEFAULT_DIPLOME or UPDATED_DIPLOME
        defaultTeamMemberShouldBeFound("diplome.in=" + DEFAULT_DIPLOME + "," + UPDATED_DIPLOME);

        // Get all the teamMemberList where diplome equals to UPDATED_DIPLOME
        defaultTeamMemberShouldNotBeFound("diplome.in=" + UPDATED_DIPLOME);
    }

    @Test
    @Transactional
    public void getAllTeamMembersByDiplomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        teamMemberRepository.saveAndFlush(teamMember);

        // Get all the teamMemberList where diplome is not null
        defaultTeamMemberShouldBeFound("diplome.specified=true");

        // Get all the teamMemberList where diplome is null
        defaultTeamMemberShouldNotBeFound("diplome.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeamMembersByResumeIsEqualToSomething() throws Exception {
        // Initialize the database
        teamMemberRepository.saveAndFlush(teamMember);

        // Get all the teamMemberList where resume equals to DEFAULT_RESUME
        defaultTeamMemberShouldBeFound("resume.equals=" + DEFAULT_RESUME);

        // Get all the teamMemberList where resume equals to UPDATED_RESUME
        defaultTeamMemberShouldNotBeFound("resume.equals=" + UPDATED_RESUME);
    }

    @Test
    @Transactional
    public void getAllTeamMembersByResumeIsInShouldWork() throws Exception {
        // Initialize the database
        teamMemberRepository.saveAndFlush(teamMember);

        // Get all the teamMemberList where resume in DEFAULT_RESUME or UPDATED_RESUME
        defaultTeamMemberShouldBeFound("resume.in=" + DEFAULT_RESUME + "," + UPDATED_RESUME);

        // Get all the teamMemberList where resume equals to UPDATED_RESUME
        defaultTeamMemberShouldNotBeFound("resume.in=" + UPDATED_RESUME);
    }

    @Test
    @Transactional
    public void getAllTeamMembersByResumeIsNullOrNotNull() throws Exception {
        // Initialize the database
        teamMemberRepository.saveAndFlush(teamMember);

        // Get all the teamMemberList where resume is not null
        defaultTeamMemberShouldBeFound("resume.specified=true");

        // Get all the teamMemberList where resume is null
        defaultTeamMemberShouldNotBeFound("resume.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeamMembersByTeamIsEqualToSomething() throws Exception {
        // Initialize the database
        Team team = TeamResourceIntTest.createEntity(em);
        em.persist(team);
        em.flush();
        teamMember.setTeam(team);
        teamMemberRepository.saveAndFlush(teamMember);
        Long teamId = team.getId();

        // Get all the teamMemberList where team equals to teamId
        defaultTeamMemberShouldBeFound("teamId.equals=" + teamId);

        // Get all the teamMemberList where team equals to teamId + 1
        defaultTeamMemberShouldNotBeFound("teamId.equals=" + (teamId + 1));
    }


    @Test
    @Transactional
    public void getAllTeamMembersByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        teamMember.setUser(user);
        teamMemberRepository.saveAndFlush(teamMember);
        Long userId = user.getId();

        // Get all the teamMemberList where user equals to userId
        defaultTeamMemberShouldBeFound("userId.equals=" + userId);

        // Get all the teamMemberList where user equals to userId + 1
        defaultTeamMemberShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTeamMemberShouldBeFound(String filter) throws Exception {
        restTeamMemberMockMvc.perform(get("/api/team-members?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teamMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].diplome").value(hasItem(DEFAULT_DIPLOME.toString())))
            .andExpect(jsonPath("$.[*].resume").value(hasItem(DEFAULT_RESUME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultTeamMemberShouldNotBeFound(String filter) throws Exception {
        restTeamMemberMockMvc.perform(get("/api/team-members?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingTeamMember() throws Exception {
        // Get the teamMember
        restTeamMemberMockMvc.perform(get("/api/team-members/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTeamMember() throws Exception {
        // Initialize the database
        teamMemberService.save(teamMember);

        int databaseSizeBeforeUpdate = teamMemberRepository.findAll().size();

        // Update the teamMember
        TeamMember updatedTeamMember = teamMemberRepository.findOne(teamMember.getId());
        // Disconnect from session so that the updates on updatedTeamMember are not directly saved in db
        em.detach(updatedTeamMember);
        updatedTeamMember
            .address(UPDATED_ADDRESS)
            .phone(UPDATED_PHONE)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .diplome(UPDATED_DIPLOME)
            .resume(UPDATED_RESUME);

        restTeamMemberMockMvc.perform(put("/api/team-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTeamMember)))
            .andExpect(status().isOk());

        // Validate the TeamMember in the database
        List<TeamMember> teamMemberList = teamMemberRepository.findAll();
        assertThat(teamMemberList).hasSize(databaseSizeBeforeUpdate);
        TeamMember testTeamMember = teamMemberList.get(teamMemberList.size() - 1);
        assertThat(testTeamMember.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testTeamMember.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testTeamMember.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testTeamMember.getDiplome()).isEqualTo(UPDATED_DIPLOME);
        assertThat(testTeamMember.getResume()).isEqualTo(UPDATED_RESUME);
    }

    @Test
    @Transactional
    public void updateNonExistingTeamMember() throws Exception {
        int databaseSizeBeforeUpdate = teamMemberRepository.findAll().size();

        // Create the TeamMember

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTeamMemberMockMvc.perform(put("/api/team-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamMember)))
            .andExpect(status().isCreated());

        // Validate the TeamMember in the database
        List<TeamMember> teamMemberList = teamMemberRepository.findAll();
        assertThat(teamMemberList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTeamMember() throws Exception {
        // Initialize the database
        teamMemberService.save(teamMember);

        int databaseSizeBeforeDelete = teamMemberRepository.findAll().size();

        // Get the teamMember
        restTeamMemberMockMvc.perform(delete("/api/team-members/{id}", teamMember.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TeamMember> teamMemberList = teamMemberRepository.findAll();
        assertThat(teamMemberList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeamMember.class);
        TeamMember teamMember1 = new TeamMember();
        teamMember1.setId(1L);
        TeamMember teamMember2 = new TeamMember();
        teamMember2.setId(teamMember1.getId());
        assertThat(teamMember1).isEqualTo(teamMember2);
        teamMember2.setId(2L);
        assertThat(teamMember1).isNotEqualTo(teamMember2);
        teamMember1.setId(null);
        assertThat(teamMember1).isNotEqualTo(teamMember2);
    }
}
