package mr.rowad.web.rest;

import mr.rowad.RowadApp;

import mr.rowad.domain.Parameter;
import mr.rowad.repository.ParameterRepository;
import mr.rowad.service.ParameterService;
import mr.rowad.web.rest.errors.ExceptionTranslator;
import mr.rowad.service.dto.ParameterCriteria;
import mr.rowad.service.ParameterQueryService;

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
import java.util.List;

import static mr.rowad.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ParameterResource REST controller.
 *
 * @see ParameterResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RowadApp.class)
public class ParameterResourceIntTest {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    @Autowired
    private ParameterRepository parameterRepository;

    @Autowired
    private ParameterService parameterService;

    @Autowired
    private ParameterQueryService parameterQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restParameterMockMvc;

    private Parameter parameter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ParameterResource parameterResource = new ParameterResource(parameterService, parameterQueryService);
        this.restParameterMockMvc = MockMvcBuilders.standaloneSetup(parameterResource)
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
    public static Parameter createEntity(EntityManager em) {
        Parameter parameter = new Parameter()
            .type(DEFAULT_TYPE)
            .value(DEFAULT_VALUE);
        return parameter;
    }

    @Before
    public void initTest() {
        parameter = createEntity(em);
    }

    @Test
    @Transactional
    public void createParameter() throws Exception {
        int databaseSizeBeforeCreate = parameterRepository.findAll().size();

        // Create the Parameter
        restParameterMockMvc.perform(post("/api/parameters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parameter)))
            .andExpect(status().isCreated());

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeCreate + 1);
        Parameter testParameter = parameterList.get(parameterList.size() - 1);
        assertThat(testParameter.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testParameter.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createParameterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = parameterRepository.findAll().size();

        // Create the Parameter with an existing ID
        parameter.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restParameterMockMvc.perform(post("/api/parameters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parameter)))
            .andExpect(status().isBadRequest());

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllParameters() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList
        restParameterMockMvc.perform(get("/api/parameters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parameter.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getParameter() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get the parameter
        restParameterMockMvc.perform(get("/api/parameters/{id}", parameter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(parameter.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getAllParametersByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where type equals to DEFAULT_TYPE
        defaultParameterShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the parameterList where type equals to UPDATED_TYPE
        defaultParameterShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllParametersByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultParameterShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the parameterList where type equals to UPDATED_TYPE
        defaultParameterShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllParametersByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where type is not null
        defaultParameterShouldBeFound("type.specified=true");

        // Get all the parameterList where type is null
        defaultParameterShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllParametersByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where value equals to DEFAULT_VALUE
        defaultParameterShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the parameterList where value equals to UPDATED_VALUE
        defaultParameterShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllParametersByValueIsInShouldWork() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultParameterShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the parameterList where value equals to UPDATED_VALUE
        defaultParameterShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllParametersByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList where value is not null
        defaultParameterShouldBeFound("value.specified=true");

        // Get all the parameterList where value is null
        defaultParameterShouldNotBeFound("value.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultParameterShouldBeFound(String filter) throws Exception {
        restParameterMockMvc.perform(get("/api/parameters?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parameter.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultParameterShouldNotBeFound(String filter) throws Exception {
        restParameterMockMvc.perform(get("/api/parameters?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingParameter() throws Exception {
        // Get the parameter
        restParameterMockMvc.perform(get("/api/parameters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParameter() throws Exception {
        // Initialize the database
        parameterService.save(parameter);

        int databaseSizeBeforeUpdate = parameterRepository.findAll().size();

        // Update the parameter
        Parameter updatedParameter = parameterRepository.findOne(parameter.getId());
        // Disconnect from session so that the updates on updatedParameter are not directly saved in db
        em.detach(updatedParameter);
        updatedParameter
            .type(UPDATED_TYPE)
            .value(UPDATED_VALUE);

        restParameterMockMvc.perform(put("/api/parameters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedParameter)))
            .andExpect(status().isOk());

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeUpdate);
        Parameter testParameter = parameterList.get(parameterList.size() - 1);
        assertThat(testParameter.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testParameter.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingParameter() throws Exception {
        int databaseSizeBeforeUpdate = parameterRepository.findAll().size();

        // Create the Parameter

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restParameterMockMvc.perform(put("/api/parameters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parameter)))
            .andExpect(status().isCreated());

        // Validate the Parameter in the database
        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteParameter() throws Exception {
        // Initialize the database
        parameterService.save(parameter);

        int databaseSizeBeforeDelete = parameterRepository.findAll().size();

        // Get the parameter
        restParameterMockMvc.perform(delete("/api/parameters/{id}", parameter.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Parameter> parameterList = parameterRepository.findAll();
        assertThat(parameterList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Parameter.class);
        Parameter parameter1 = new Parameter();
        parameter1.setId(1L);
        Parameter parameter2 = new Parameter();
        parameter2.setId(parameter1.getId());
        assertThat(parameter1).isEqualTo(parameter2);
        parameter2.setId(2L);
        assertThat(parameter1).isNotEqualTo(parameter2);
        parameter1.setId(null);
        assertThat(parameter1).isNotEqualTo(parameter2);
    }
}
