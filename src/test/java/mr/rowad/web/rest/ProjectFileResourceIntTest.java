package mr.rowad.web.rest;

import mr.rowad.RowadApp;

import mr.rowad.domain.ProjectFile;
import mr.rowad.domain.Project;
import mr.rowad.repository.ProjectFileRepository;
import mr.rowad.service.ProjectFileService;
import mr.rowad.web.rest.errors.ExceptionTranslator;
import mr.rowad.service.dto.ProjectFileCriteria;
import mr.rowad.service.ProjectFileQueryService;

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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static mr.rowad.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProjectFileResource REST controller.
 *
 * @see ProjectFileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RowadApp.class)
public class ProjectFileResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_CONTENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONTENT = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_CONTENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONTENT_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_TEXT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT_CONTENT = "BBBBBBBBBB";

    @Autowired
    private ProjectFileRepository projectFileRepository;

    @Autowired
    private ProjectFileService projectFileService;

    @Autowired
    private ProjectFileQueryService projectFileQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProjectFileMockMvc;

    private ProjectFile projectFile;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProjectFileResource projectFileResource = new ProjectFileResource(projectFileService, projectFileQueryService);
        this.restProjectFileMockMvc = MockMvcBuilders.standaloneSetup(projectFileResource)
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
    public static ProjectFile createEntity(EntityManager em) {
        ProjectFile projectFile = new ProjectFile()
            .name(DEFAULT_NAME)
            .path(DEFAULT_PATH)
            .type(DEFAULT_TYPE)
            .content(DEFAULT_CONTENT)
            .contentContentType(DEFAULT_CONTENT_CONTENT_TYPE)
            .textContent(DEFAULT_TEXT_CONTENT);
        return projectFile;
    }

    @Before
    public void initTest() {
        projectFile = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjectFile() throws Exception {
        int databaseSizeBeforeCreate = projectFileRepository.findAll().size();

        // Create the ProjectFile
        restProjectFileMockMvc.perform(post("/api/project-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectFile)))
            .andExpect(status().isCreated());

        // Validate the ProjectFile in the database
        List<ProjectFile> projectFileList = projectFileRepository.findAll();
        assertThat(projectFileList).hasSize(databaseSizeBeforeCreate + 1);
        ProjectFile testProjectFile = projectFileList.get(projectFileList.size() - 1);
        assertThat(testProjectFile.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProjectFile.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testProjectFile.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testProjectFile.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testProjectFile.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);
        assertThat(testProjectFile.getTextContent()).isEqualTo(DEFAULT_TEXT_CONTENT);
    }

    @Test
    @Transactional
    public void createProjectFileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projectFileRepository.findAll().size();

        // Create the ProjectFile with an existing ID
        projectFile.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectFileMockMvc.perform(post("/api/project-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectFile)))
            .andExpect(status().isBadRequest());

        // Validate the ProjectFile in the database
        List<ProjectFile> projectFileList = projectFileRepository.findAll();
        assertThat(projectFileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProjectFiles() throws Exception {
        // Initialize the database
        projectFileRepository.saveAndFlush(projectFile);

        // Get all the projectFileList
        restProjectFileMockMvc.perform(get("/api/project-files?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTENT))))
            .andExpect(jsonPath("$.[*].textContent").value(hasItem(DEFAULT_TEXT_CONTENT.toString())));
    }

    @Test
    @Transactional
    public void getProjectFile() throws Exception {
        // Initialize the database
        projectFileRepository.saveAndFlush(projectFile);

        // Get the projectFile
        restProjectFileMockMvc.perform(get("/api/project-files/{id}", projectFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projectFile.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.contentContentType").value(DEFAULT_CONTENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.content").value(Base64Utils.encodeToString(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.textContent").value(DEFAULT_TEXT_CONTENT.toString()));
    }

    @Test
    @Transactional
    public void getAllProjectFilesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        projectFileRepository.saveAndFlush(projectFile);

        // Get all the projectFileList where name equals to DEFAULT_NAME
        defaultProjectFileShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the projectFileList where name equals to UPDATED_NAME
        defaultProjectFileShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProjectFilesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        projectFileRepository.saveAndFlush(projectFile);

        // Get all the projectFileList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProjectFileShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the projectFileList where name equals to UPDATED_NAME
        defaultProjectFileShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProjectFilesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectFileRepository.saveAndFlush(projectFile);

        // Get all the projectFileList where name is not null
        defaultProjectFileShouldBeFound("name.specified=true");

        // Get all the projectFileList where name is null
        defaultProjectFileShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectFilesByPathIsEqualToSomething() throws Exception {
        // Initialize the database
        projectFileRepository.saveAndFlush(projectFile);

        // Get all the projectFileList where path equals to DEFAULT_PATH
        defaultProjectFileShouldBeFound("path.equals=" + DEFAULT_PATH);

        // Get all the projectFileList where path equals to UPDATED_PATH
        defaultProjectFileShouldNotBeFound("path.equals=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    public void getAllProjectFilesByPathIsInShouldWork() throws Exception {
        // Initialize the database
        projectFileRepository.saveAndFlush(projectFile);

        // Get all the projectFileList where path in DEFAULT_PATH or UPDATED_PATH
        defaultProjectFileShouldBeFound("path.in=" + DEFAULT_PATH + "," + UPDATED_PATH);

        // Get all the projectFileList where path equals to UPDATED_PATH
        defaultProjectFileShouldNotBeFound("path.in=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    public void getAllProjectFilesByPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectFileRepository.saveAndFlush(projectFile);

        // Get all the projectFileList where path is not null
        defaultProjectFileShouldBeFound("path.specified=true");

        // Get all the projectFileList where path is null
        defaultProjectFileShouldNotBeFound("path.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectFilesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        projectFileRepository.saveAndFlush(projectFile);

        // Get all the projectFileList where type equals to DEFAULT_TYPE
        defaultProjectFileShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the projectFileList where type equals to UPDATED_TYPE
        defaultProjectFileShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllProjectFilesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        projectFileRepository.saveAndFlush(projectFile);

        // Get all the projectFileList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultProjectFileShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the projectFileList where type equals to UPDATED_TYPE
        defaultProjectFileShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllProjectFilesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectFileRepository.saveAndFlush(projectFile);

        // Get all the projectFileList where type is not null
        defaultProjectFileShouldBeFound("type.specified=true");

        // Get all the projectFileList where type is null
        defaultProjectFileShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectFilesByProjectIsEqualToSomething() throws Exception {
        // Initialize the database
        Project project = ProjectResourceIntTest.createEntity(em);
        em.persist(project);
        em.flush();
        projectFile.setProject(project);
        projectFileRepository.saveAndFlush(projectFile);
        Long projectId = project.getId();

        // Get all the projectFileList where project equals to projectId
        defaultProjectFileShouldBeFound("projectId.equals=" + projectId);

        // Get all the projectFileList where project equals to projectId + 1
        defaultProjectFileShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProjectFileShouldBeFound(String filter) throws Exception {
        restProjectFileMockMvc.perform(get("/api/project-files?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTENT))))
            .andExpect(jsonPath("$.[*].textContent").value(hasItem(DEFAULT_TEXT_CONTENT.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProjectFileShouldNotBeFound(String filter) throws Exception {
        restProjectFileMockMvc.perform(get("/api/project-files?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingProjectFile() throws Exception {
        // Get the projectFile
        restProjectFileMockMvc.perform(get("/api/project-files/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectFile() throws Exception {
        // Initialize the database
        projectFileService.save(projectFile);

        int databaseSizeBeforeUpdate = projectFileRepository.findAll().size();

        // Update the projectFile
        ProjectFile updatedProjectFile = projectFileRepository.findOne(projectFile.getId());
        // Disconnect from session so that the updates on updatedProjectFile are not directly saved in db
        em.detach(updatedProjectFile);
        updatedProjectFile
            .name(UPDATED_NAME)
            .path(UPDATED_PATH)
            .type(UPDATED_TYPE)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .textContent(UPDATED_TEXT_CONTENT);

        restProjectFileMockMvc.perform(put("/api/project-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProjectFile)))
            .andExpect(status().isOk());

        // Validate the ProjectFile in the database
        List<ProjectFile> projectFileList = projectFileRepository.findAll();
        assertThat(projectFileList).hasSize(databaseSizeBeforeUpdate);
        ProjectFile testProjectFile = projectFileList.get(projectFileList.size() - 1);
        assertThat(testProjectFile.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProjectFile.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testProjectFile.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testProjectFile.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testProjectFile.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testProjectFile.getTextContent()).isEqualTo(UPDATED_TEXT_CONTENT);
    }

    @Test
    @Transactional
    public void updateNonExistingProjectFile() throws Exception {
        int databaseSizeBeforeUpdate = projectFileRepository.findAll().size();

        // Create the ProjectFile

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProjectFileMockMvc.perform(put("/api/project-files")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectFile)))
            .andExpect(status().isCreated());

        // Validate the ProjectFile in the database
        List<ProjectFile> projectFileList = projectFileRepository.findAll();
        assertThat(projectFileList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProjectFile() throws Exception {
        // Initialize the database
        projectFileService.save(projectFile);

        int databaseSizeBeforeDelete = projectFileRepository.findAll().size();

        // Get the projectFile
        restProjectFileMockMvc.perform(delete("/api/project-files/{id}", projectFile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProjectFile> projectFileList = projectFileRepository.findAll();
        assertThat(projectFileList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectFile.class);
        ProjectFile projectFile1 = new ProjectFile();
        projectFile1.setId(1L);
        ProjectFile projectFile2 = new ProjectFile();
        projectFile2.setId(projectFile1.getId());
        assertThat(projectFile1).isEqualTo(projectFile2);
        projectFile2.setId(2L);
        assertThat(projectFile1).isNotEqualTo(projectFile2);
        projectFile1.setId(null);
        assertThat(projectFile1).isNotEqualTo(projectFile2);
    }
}
