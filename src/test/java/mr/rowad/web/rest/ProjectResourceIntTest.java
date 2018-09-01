package mr.rowad.web.rest;

import mr.rowad.RowadApp;

import mr.rowad.domain.Project;
import mr.rowad.domain.Team;
import mr.rowad.repository.ProjectRepository;
import mr.rowad.service.ProjectService;
import mr.rowad.web.rest.errors.ExceptionTranslator;
import mr.rowad.service.dto.ProjectCriteria;
import mr.rowad.service.ProjectQueryService;

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
 * Test class for the ProjectResource REST controller.
 *
 * @see ProjectResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RowadApp.class)
public class ProjectResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS = "BBBBBBBBBB";

    private static final Double DEFAULT_RENDEMENT = 1D;
    private static final Double UPDATED_RENDEMENT = 2D;

    private static final Double DEFAULT_BUDGET = 1D;
    private static final Double UPDATED_BUDGET = 2D;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ESTIMATED_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ESTIMATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_CIBLE = "AAAAAAAAAA";
    private static final String UPDATED_CIBLE = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectQueryService projectQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProjectMockMvc;

    private Project project;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProjectResource projectResource = new ProjectResource(projectService, projectQueryService);
        this.restProjectMockMvc = MockMvcBuilders.standaloneSetup(projectResource)
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
    public static Project createEntity(EntityManager em) {
        Project project = new Project()
            .title(DEFAULT_TITLE)
            .shortDescription(DEFAULT_SHORT_DESCRIPTION)
            .details(DEFAULT_DETAILS)
            .rendement(DEFAULT_RENDEMENT)
            .budget(DEFAULT_BUDGET)
            .startDate(DEFAULT_START_DATE)
            .estimatedEndDate(DEFAULT_ESTIMATED_END_DATE)
            .status(DEFAULT_STATUS)
            .cible(DEFAULT_CIBLE)
            .type(DEFAULT_TYPE);
        return project;
    }

    @Before
    public void initTest() {
        project = createEntity(em);
    }

    @Test
    @Transactional
    public void createProject() throws Exception {
        int databaseSizeBeforeCreate = projectRepository.findAll().size();

        // Create the Project
        restProjectMockMvc.perform(post("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isCreated());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeCreate + 1);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testProject.getShortDescription()).isEqualTo(DEFAULT_SHORT_DESCRIPTION);
        assertThat(testProject.getDetails()).isEqualTo(DEFAULT_DETAILS);
        assertThat(testProject.getRendement()).isEqualTo(DEFAULT_RENDEMENT);
        assertThat(testProject.getBudget()).isEqualTo(DEFAULT_BUDGET);
        assertThat(testProject.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testProject.getEstimatedEndDate()).isEqualTo(DEFAULT_ESTIMATED_END_DATE);
        assertThat(testProject.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testProject.getCible()).isEqualTo(DEFAULT_CIBLE);
        assertThat(testProject.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createProjectWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projectRepository.findAll().size();

        // Create the Project with an existing ID
        project.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectMockMvc.perform(post("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProjects() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList
        restProjectMockMvc.perform(get("/api/projects?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(project.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].shortDescription").value(hasItem(DEFAULT_SHORT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS.toString())))
            .andExpect(jsonPath("$.[*].rendement").value(hasItem(DEFAULT_RENDEMENT.doubleValue())))
            .andExpect(jsonPath("$.[*].budget").value(hasItem(DEFAULT_BUDGET.doubleValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].estimatedEndDate").value(hasItem(DEFAULT_ESTIMATED_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].cible").value(hasItem(DEFAULT_CIBLE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get the project
        restProjectMockMvc.perform(get("/api/projects/{id}", project.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(project.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.shortDescription").value(DEFAULT_SHORT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS.toString()))
            .andExpect(jsonPath("$.rendement").value(DEFAULT_RENDEMENT.doubleValue()))
            .andExpect(jsonPath("$.budget").value(DEFAULT_BUDGET.doubleValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.estimatedEndDate").value(DEFAULT_ESTIMATED_END_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.cible").value(DEFAULT_CIBLE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getAllProjectsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where title equals to DEFAULT_TITLE
        defaultProjectShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the projectList where title equals to UPDATED_TITLE
        defaultProjectShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllProjectsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultProjectShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the projectList where title equals to UPDATED_TITLE
        defaultProjectShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllProjectsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where title is not null
        defaultProjectShouldBeFound("title.specified=true");

        // Get all the projectList where title is null
        defaultProjectShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectsByShortDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where shortDescription equals to DEFAULT_SHORT_DESCRIPTION
        defaultProjectShouldBeFound("shortDescription.equals=" + DEFAULT_SHORT_DESCRIPTION);

        // Get all the projectList where shortDescription equals to UPDATED_SHORT_DESCRIPTION
        defaultProjectShouldNotBeFound("shortDescription.equals=" + UPDATED_SHORT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProjectsByShortDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where shortDescription in DEFAULT_SHORT_DESCRIPTION or UPDATED_SHORT_DESCRIPTION
        defaultProjectShouldBeFound("shortDescription.in=" + DEFAULT_SHORT_DESCRIPTION + "," + UPDATED_SHORT_DESCRIPTION);

        // Get all the projectList where shortDescription equals to UPDATED_SHORT_DESCRIPTION
        defaultProjectShouldNotBeFound("shortDescription.in=" + UPDATED_SHORT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProjectsByShortDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where shortDescription is not null
        defaultProjectShouldBeFound("shortDescription.specified=true");

        // Get all the projectList where shortDescription is null
        defaultProjectShouldNotBeFound("shortDescription.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectsByDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where details equals to DEFAULT_DETAILS
        defaultProjectShouldBeFound("details.equals=" + DEFAULT_DETAILS);

        // Get all the projectList where details equals to UPDATED_DETAILS
        defaultProjectShouldNotBeFound("details.equals=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    public void getAllProjectsByDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where details in DEFAULT_DETAILS or UPDATED_DETAILS
        defaultProjectShouldBeFound("details.in=" + DEFAULT_DETAILS + "," + UPDATED_DETAILS);

        // Get all the projectList where details equals to UPDATED_DETAILS
        defaultProjectShouldNotBeFound("details.in=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    public void getAllProjectsByDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where details is not null
        defaultProjectShouldBeFound("details.specified=true");

        // Get all the projectList where details is null
        defaultProjectShouldNotBeFound("details.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectsByRendementIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where rendement equals to DEFAULT_RENDEMENT
        defaultProjectShouldBeFound("rendement.equals=" + DEFAULT_RENDEMENT);

        // Get all the projectList where rendement equals to UPDATED_RENDEMENT
        defaultProjectShouldNotBeFound("rendement.equals=" + UPDATED_RENDEMENT);
    }

    @Test
    @Transactional
    public void getAllProjectsByRendementIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where rendement in DEFAULT_RENDEMENT or UPDATED_RENDEMENT
        defaultProjectShouldBeFound("rendement.in=" + DEFAULT_RENDEMENT + "," + UPDATED_RENDEMENT);

        // Get all the projectList where rendement equals to UPDATED_RENDEMENT
        defaultProjectShouldNotBeFound("rendement.in=" + UPDATED_RENDEMENT);
    }

    @Test
    @Transactional
    public void getAllProjectsByRendementIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where rendement is not null
        defaultProjectShouldBeFound("rendement.specified=true");

        // Get all the projectList where rendement is null
        defaultProjectShouldNotBeFound("rendement.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectsByBudgetIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where budget equals to DEFAULT_BUDGET
        defaultProjectShouldBeFound("budget.equals=" + DEFAULT_BUDGET);

        // Get all the projectList where budget equals to UPDATED_BUDGET
        defaultProjectShouldNotBeFound("budget.equals=" + UPDATED_BUDGET);
    }

    @Test
    @Transactional
    public void getAllProjectsByBudgetIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where budget in DEFAULT_BUDGET or UPDATED_BUDGET
        defaultProjectShouldBeFound("budget.in=" + DEFAULT_BUDGET + "," + UPDATED_BUDGET);

        // Get all the projectList where budget equals to UPDATED_BUDGET
        defaultProjectShouldNotBeFound("budget.in=" + UPDATED_BUDGET);
    }

    @Test
    @Transactional
    public void getAllProjectsByBudgetIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where budget is not null
        defaultProjectShouldBeFound("budget.specified=true");

        // Get all the projectList where budget is null
        defaultProjectShouldNotBeFound("budget.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where startDate equals to DEFAULT_START_DATE
        defaultProjectShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the projectList where startDate equals to UPDATED_START_DATE
        defaultProjectShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllProjectsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultProjectShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the projectList where startDate equals to UPDATED_START_DATE
        defaultProjectShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllProjectsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where startDate is not null
        defaultProjectShouldBeFound("startDate.specified=true");

        // Get all the projectList where startDate is null
        defaultProjectShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where startDate greater than or equals to DEFAULT_START_DATE
        defaultProjectShouldBeFound("startDate.greaterOrEqualThan=" + DEFAULT_START_DATE);

        // Get all the projectList where startDate greater than or equals to UPDATED_START_DATE
        defaultProjectShouldNotBeFound("startDate.greaterOrEqualThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllProjectsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where startDate less than or equals to DEFAULT_START_DATE
        defaultProjectShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the projectList where startDate less than or equals to UPDATED_START_DATE
        defaultProjectShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }


    @Test
    @Transactional
    public void getAllProjectsByEstimatedEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where estimatedEndDate equals to DEFAULT_ESTIMATED_END_DATE
        defaultProjectShouldBeFound("estimatedEndDate.equals=" + DEFAULT_ESTIMATED_END_DATE);

        // Get all the projectList where estimatedEndDate equals to UPDATED_ESTIMATED_END_DATE
        defaultProjectShouldNotBeFound("estimatedEndDate.equals=" + UPDATED_ESTIMATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllProjectsByEstimatedEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where estimatedEndDate in DEFAULT_ESTIMATED_END_DATE or UPDATED_ESTIMATED_END_DATE
        defaultProjectShouldBeFound("estimatedEndDate.in=" + DEFAULT_ESTIMATED_END_DATE + "," + UPDATED_ESTIMATED_END_DATE);

        // Get all the projectList where estimatedEndDate equals to UPDATED_ESTIMATED_END_DATE
        defaultProjectShouldNotBeFound("estimatedEndDate.in=" + UPDATED_ESTIMATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllProjectsByEstimatedEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where estimatedEndDate is not null
        defaultProjectShouldBeFound("estimatedEndDate.specified=true");

        // Get all the projectList where estimatedEndDate is null
        defaultProjectShouldNotBeFound("estimatedEndDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectsByEstimatedEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where estimatedEndDate greater than or equals to DEFAULT_ESTIMATED_END_DATE
        defaultProjectShouldBeFound("estimatedEndDate.greaterOrEqualThan=" + DEFAULT_ESTIMATED_END_DATE);

        // Get all the projectList where estimatedEndDate greater than or equals to UPDATED_ESTIMATED_END_DATE
        defaultProjectShouldNotBeFound("estimatedEndDate.greaterOrEqualThan=" + UPDATED_ESTIMATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllProjectsByEstimatedEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where estimatedEndDate less than or equals to DEFAULT_ESTIMATED_END_DATE
        defaultProjectShouldNotBeFound("estimatedEndDate.lessThan=" + DEFAULT_ESTIMATED_END_DATE);

        // Get all the projectList where estimatedEndDate less than or equals to UPDATED_ESTIMATED_END_DATE
        defaultProjectShouldBeFound("estimatedEndDate.lessThan=" + UPDATED_ESTIMATED_END_DATE);
    }


    @Test
    @Transactional
    public void getAllProjectsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where status equals to DEFAULT_STATUS
        defaultProjectShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the projectList where status equals to UPDATED_STATUS
        defaultProjectShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllProjectsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultProjectShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the projectList where status equals to UPDATED_STATUS
        defaultProjectShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllProjectsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where status is not null
        defaultProjectShouldBeFound("status.specified=true");

        // Get all the projectList where status is null
        defaultProjectShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectsByCibleIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where cible equals to DEFAULT_CIBLE
        defaultProjectShouldBeFound("cible.equals=" + DEFAULT_CIBLE);

        // Get all the projectList where cible equals to UPDATED_CIBLE
        defaultProjectShouldNotBeFound("cible.equals=" + UPDATED_CIBLE);
    }

    @Test
    @Transactional
    public void getAllProjectsByCibleIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where cible in DEFAULT_CIBLE or UPDATED_CIBLE
        defaultProjectShouldBeFound("cible.in=" + DEFAULT_CIBLE + "," + UPDATED_CIBLE);

        // Get all the projectList where cible equals to UPDATED_CIBLE
        defaultProjectShouldNotBeFound("cible.in=" + UPDATED_CIBLE);
    }

    @Test
    @Transactional
    public void getAllProjectsByCibleIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where cible is not null
        defaultProjectShouldBeFound("cible.specified=true");

        // Get all the projectList where cible is null
        defaultProjectShouldNotBeFound("cible.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where type equals to DEFAULT_TYPE
        defaultProjectShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the projectList where type equals to UPDATED_TYPE
        defaultProjectShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllProjectsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultProjectShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the projectList where type equals to UPDATED_TYPE
        defaultProjectShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllProjectsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where type is not null
        defaultProjectShouldBeFound("type.specified=true");

        // Get all the projectList where type is null
        defaultProjectShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectsByTeamIsEqualToSomething() throws Exception {
        // Initialize the database
        Team team = TeamResourceIntTest.createEntity(em);
        em.persist(team);
        em.flush();
        project.setTeam(team);
        projectRepository.saveAndFlush(project);
        Long teamId = team.getId();

        // Get all the projectList where team equals to teamId
        defaultProjectShouldBeFound("teamId.equals=" + teamId);

        // Get all the projectList where team equals to teamId + 1
        defaultProjectShouldNotBeFound("teamId.equals=" + (teamId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProjectShouldBeFound(String filter) throws Exception {
        restProjectMockMvc.perform(get("/api/projects?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(project.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].shortDescription").value(hasItem(DEFAULT_SHORT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS.toString())))
            .andExpect(jsonPath("$.[*].rendement").value(hasItem(DEFAULT_RENDEMENT.doubleValue())))
            .andExpect(jsonPath("$.[*].budget").value(hasItem(DEFAULT_BUDGET.doubleValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].estimatedEndDate").value(hasItem(DEFAULT_ESTIMATED_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].cible").value(hasItem(DEFAULT_CIBLE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProjectShouldNotBeFound(String filter) throws Exception {
        restProjectMockMvc.perform(get("/api/projects?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingProject() throws Exception {
        // Get the project
        restProjectMockMvc.perform(get("/api/projects/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProject() throws Exception {
        // Initialize the database
        projectService.save(project);

        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Update the project
        Project updatedProject = projectRepository.findOne(project.getId());
        // Disconnect from session so that the updates on updatedProject are not directly saved in db
        em.detach(updatedProject);
        updatedProject
            .title(UPDATED_TITLE)
            .shortDescription(UPDATED_SHORT_DESCRIPTION)
            .details(UPDATED_DETAILS)
            .rendement(UPDATED_RENDEMENT)
            .budget(UPDATED_BUDGET)
            .startDate(UPDATED_START_DATE)
            .estimatedEndDate(UPDATED_ESTIMATED_END_DATE)
            .status(UPDATED_STATUS)
            .cible(UPDATED_CIBLE)
            .type(UPDATED_TYPE);

        restProjectMockMvc.perform(put("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProject)))
            .andExpect(status().isOk());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testProject.getShortDescription()).isEqualTo(UPDATED_SHORT_DESCRIPTION);
        assertThat(testProject.getDetails()).isEqualTo(UPDATED_DETAILS);
        assertThat(testProject.getRendement()).isEqualTo(UPDATED_RENDEMENT);
        assertThat(testProject.getBudget()).isEqualTo(UPDATED_BUDGET);
        assertThat(testProject.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testProject.getEstimatedEndDate()).isEqualTo(UPDATED_ESTIMATED_END_DATE);
        assertThat(testProject.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProject.getCible()).isEqualTo(UPDATED_CIBLE);
        assertThat(testProject.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Create the Project

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProjectMockMvc.perform(put("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isCreated());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProject() throws Exception {
        // Initialize the database
        projectService.save(project);

        int databaseSizeBeforeDelete = projectRepository.findAll().size();

        // Get the project
        restProjectMockMvc.perform(delete("/api/projects/{id}", project.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Project.class);
        Project project1 = new Project();
        project1.setId(1L);
        Project project2 = new Project();
        project2.setId(project1.getId());
        assertThat(project1).isEqualTo(project2);
        project2.setId(2L);
        assertThat(project1).isNotEqualTo(project2);
        project1.setId(null);
        assertThat(project1).isNotEqualTo(project2);
    }
}
