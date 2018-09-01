package mr.rowad.web.rest;

import mr.rowad.RowadApp;

import mr.rowad.domain.ProjectEvent;
import mr.rowad.domain.Project;
import mr.rowad.domain.User;
import mr.rowad.repository.ProjectEventRepository;
import mr.rowad.service.ProjectEventService;
import mr.rowad.web.rest.errors.ExceptionTranslator;
import mr.rowad.service.dto.ProjectEventCriteria;
import mr.rowad.service.ProjectEventQueryService;

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
 * Test class for the ProjectEventResource REST controller.
 *
 * @see ProjectEventResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RowadApp.class)
public class ProjectEventResourceIntTest {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ProjectEventRepository projectEventRepository;

    @Autowired
    private ProjectEventService projectEventService;

    @Autowired
    private ProjectEventQueryService projectEventQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProjectEventMockMvc;

    private ProjectEvent projectEvent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProjectEventResource projectEventResource = new ProjectEventResource(projectEventService, projectEventQueryService);
        this.restProjectEventMockMvc = MockMvcBuilders.standaloneSetup(projectEventResource)
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
    public static ProjectEvent createEntity(EntityManager em) {
        ProjectEvent projectEvent = new ProjectEvent()
            .type(DEFAULT_TYPE)
            .creationDate(DEFAULT_CREATION_DATE)
            .description(DEFAULT_DESCRIPTION);
        return projectEvent;
    }

    @Before
    public void initTest() {
        projectEvent = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjectEvent() throws Exception {
        int databaseSizeBeforeCreate = projectEventRepository.findAll().size();

        // Create the ProjectEvent
        restProjectEventMockMvc.perform(post("/api/project-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectEvent)))
            .andExpect(status().isCreated());

        // Validate the ProjectEvent in the database
        List<ProjectEvent> projectEventList = projectEventRepository.findAll();
        assertThat(projectEventList).hasSize(databaseSizeBeforeCreate + 1);
        ProjectEvent testProjectEvent = projectEventList.get(projectEventList.size() - 1);
        assertThat(testProjectEvent.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testProjectEvent.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testProjectEvent.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createProjectEventWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projectEventRepository.findAll().size();

        // Create the ProjectEvent with an existing ID
        projectEvent.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectEventMockMvc.perform(post("/api/project-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectEvent)))
            .andExpect(status().isBadRequest());

        // Validate the ProjectEvent in the database
        List<ProjectEvent> projectEventList = projectEventRepository.findAll();
        assertThat(projectEventList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProjectEvents() throws Exception {
        // Initialize the database
        projectEventRepository.saveAndFlush(projectEvent);

        // Get all the projectEventList
        restProjectEventMockMvc.perform(get("/api/project-events?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getProjectEvent() throws Exception {
        // Initialize the database
        projectEventRepository.saveAndFlush(projectEvent);

        // Get the projectEvent
        restProjectEventMockMvc.perform(get("/api/project-events/{id}", projectEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projectEvent.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getAllProjectEventsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        projectEventRepository.saveAndFlush(projectEvent);

        // Get all the projectEventList where type equals to DEFAULT_TYPE
        defaultProjectEventShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the projectEventList where type equals to UPDATED_TYPE
        defaultProjectEventShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllProjectEventsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        projectEventRepository.saveAndFlush(projectEvent);

        // Get all the projectEventList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultProjectEventShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the projectEventList where type equals to UPDATED_TYPE
        defaultProjectEventShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllProjectEventsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectEventRepository.saveAndFlush(projectEvent);

        // Get all the projectEventList where type is not null
        defaultProjectEventShouldBeFound("type.specified=true");

        // Get all the projectEventList where type is null
        defaultProjectEventShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectEventsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        projectEventRepository.saveAndFlush(projectEvent);

        // Get all the projectEventList where creationDate equals to DEFAULT_CREATION_DATE
        defaultProjectEventShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the projectEventList where creationDate equals to UPDATED_CREATION_DATE
        defaultProjectEventShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllProjectEventsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        projectEventRepository.saveAndFlush(projectEvent);

        // Get all the projectEventList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultProjectEventShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the projectEventList where creationDate equals to UPDATED_CREATION_DATE
        defaultProjectEventShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllProjectEventsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectEventRepository.saveAndFlush(projectEvent);

        // Get all the projectEventList where creationDate is not null
        defaultProjectEventShouldBeFound("creationDate.specified=true");

        // Get all the projectEventList where creationDate is null
        defaultProjectEventShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectEventsByCreationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectEventRepository.saveAndFlush(projectEvent);

        // Get all the projectEventList where creationDate greater than or equals to DEFAULT_CREATION_DATE
        defaultProjectEventShouldBeFound("creationDate.greaterOrEqualThan=" + DEFAULT_CREATION_DATE);

        // Get all the projectEventList where creationDate greater than or equals to UPDATED_CREATION_DATE
        defaultProjectEventShouldNotBeFound("creationDate.greaterOrEqualThan=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllProjectEventsByCreationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        projectEventRepository.saveAndFlush(projectEvent);

        // Get all the projectEventList where creationDate less than or equals to DEFAULT_CREATION_DATE
        defaultProjectEventShouldNotBeFound("creationDate.lessThan=" + DEFAULT_CREATION_DATE);

        // Get all the projectEventList where creationDate less than or equals to UPDATED_CREATION_DATE
        defaultProjectEventShouldBeFound("creationDate.lessThan=" + UPDATED_CREATION_DATE);
    }


    @Test
    @Transactional
    public void getAllProjectEventsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        projectEventRepository.saveAndFlush(projectEvent);

        // Get all the projectEventList where description equals to DEFAULT_DESCRIPTION
        defaultProjectEventShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the projectEventList where description equals to UPDATED_DESCRIPTION
        defaultProjectEventShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProjectEventsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        projectEventRepository.saveAndFlush(projectEvent);

        // Get all the projectEventList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultProjectEventShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the projectEventList where description equals to UPDATED_DESCRIPTION
        defaultProjectEventShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProjectEventsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectEventRepository.saveAndFlush(projectEvent);

        // Get all the projectEventList where description is not null
        defaultProjectEventShouldBeFound("description.specified=true");

        // Get all the projectEventList where description is null
        defaultProjectEventShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectEventsByProjectIsEqualToSomething() throws Exception {
        // Initialize the database
        Project project = ProjectResourceIntTest.createEntity(em);
        em.persist(project);
        em.flush();
        projectEvent.setProject(project);
        projectEventRepository.saveAndFlush(projectEvent);
        Long projectId = project.getId();

        // Get all the projectEventList where project equals to projectId
        defaultProjectEventShouldBeFound("projectId.equals=" + projectId);

        // Get all the projectEventList where project equals to projectId + 1
        defaultProjectEventShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }


    @Test
    @Transactional
    public void getAllProjectEventsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        projectEvent.setUser(user);
        projectEventRepository.saveAndFlush(projectEvent);
        Long userId = user.getId();

        // Get all the projectEventList where user equals to userId
        defaultProjectEventShouldBeFound("userId.equals=" + userId);

        // Get all the projectEventList where user equals to userId + 1
        defaultProjectEventShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProjectEventShouldBeFound(String filter) throws Exception {
        restProjectEventMockMvc.perform(get("/api/project-events?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProjectEventShouldNotBeFound(String filter) throws Exception {
        restProjectEventMockMvc.perform(get("/api/project-events?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingProjectEvent() throws Exception {
        // Get the projectEvent
        restProjectEventMockMvc.perform(get("/api/project-events/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectEvent() throws Exception {
        // Initialize the database
        projectEventService.save(projectEvent);

        int databaseSizeBeforeUpdate = projectEventRepository.findAll().size();

        // Update the projectEvent
        ProjectEvent updatedProjectEvent = projectEventRepository.findOne(projectEvent.getId());
        // Disconnect from session so that the updates on updatedProjectEvent are not directly saved in db
        em.detach(updatedProjectEvent);
        updatedProjectEvent
            .type(UPDATED_TYPE)
            .creationDate(UPDATED_CREATION_DATE)
            .description(UPDATED_DESCRIPTION);

        restProjectEventMockMvc.perform(put("/api/project-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProjectEvent)))
            .andExpect(status().isOk());

        // Validate the ProjectEvent in the database
        List<ProjectEvent> projectEventList = projectEventRepository.findAll();
        assertThat(projectEventList).hasSize(databaseSizeBeforeUpdate);
        ProjectEvent testProjectEvent = projectEventList.get(projectEventList.size() - 1);
        assertThat(testProjectEvent.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testProjectEvent.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testProjectEvent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingProjectEvent() throws Exception {
        int databaseSizeBeforeUpdate = projectEventRepository.findAll().size();

        // Create the ProjectEvent

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProjectEventMockMvc.perform(put("/api/project-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectEvent)))
            .andExpect(status().isCreated());

        // Validate the ProjectEvent in the database
        List<ProjectEvent> projectEventList = projectEventRepository.findAll();
        assertThat(projectEventList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProjectEvent() throws Exception {
        // Initialize the database
        projectEventService.save(projectEvent);

        int databaseSizeBeforeDelete = projectEventRepository.findAll().size();

        // Get the projectEvent
        restProjectEventMockMvc.perform(delete("/api/project-events/{id}", projectEvent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProjectEvent> projectEventList = projectEventRepository.findAll();
        assertThat(projectEventList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectEvent.class);
        ProjectEvent projectEvent1 = new ProjectEvent();
        projectEvent1.setId(1L);
        ProjectEvent projectEvent2 = new ProjectEvent();
        projectEvent2.setId(projectEvent1.getId());
        assertThat(projectEvent1).isEqualTo(projectEvent2);
        projectEvent2.setId(2L);
        assertThat(projectEvent1).isNotEqualTo(projectEvent2);
        projectEvent1.setId(null);
        assertThat(projectEvent1).isNotEqualTo(projectEvent2);
    }
}
