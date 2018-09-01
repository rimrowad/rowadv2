package mr.rowad.web.rest;

import mr.rowad.RowadApp;

import mr.rowad.domain.Investor;
import mr.rowad.domain.User;
import mr.rowad.repository.InvestorRepository;
import mr.rowad.service.InvestorService;
import mr.rowad.web.rest.errors.ExceptionTranslator;
import mr.rowad.service.dto.InvestorCriteria;
import mr.rowad.service.InvestorQueryService;

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
 * Test class for the InvestorResource REST controller.
 *
 * @see InvestorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RowadApp.class)
public class InvestorResourceIntTest {

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_SHORT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private InvestorRepository investorRepository;

    @Autowired
    private InvestorService investorService;

    @Autowired
    private InvestorQueryService investorQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInvestorMockMvc;

    private Investor investor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InvestorResource investorResource = new InvestorResource(investorService, investorQueryService);
        this.restInvestorMockMvc = MockMvcBuilders.standaloneSetup(investorResource)
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
    public static Investor createEntity(EntityManager em) {
        Investor investor = new Investor()
            .address(DEFAULT_ADDRESS)
            .phone(DEFAULT_PHONE)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .shortDescription(DEFAULT_SHORT_DESCRIPTION)
            .description(DEFAULT_DESCRIPTION);
        return investor;
    }

    @Before
    public void initTest() {
        investor = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvestor() throws Exception {
        int databaseSizeBeforeCreate = investorRepository.findAll().size();

        // Create the Investor
        restInvestorMockMvc.perform(post("/api/investors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(investor)))
            .andExpect(status().isCreated());

        // Validate the Investor in the database
        List<Investor> investorList = investorRepository.findAll();
        assertThat(investorList).hasSize(databaseSizeBeforeCreate + 1);
        Investor testInvestor = investorList.get(investorList.size() - 1);
        assertThat(testInvestor.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testInvestor.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testInvestor.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testInvestor.getShortDescription()).isEqualTo(DEFAULT_SHORT_DESCRIPTION);
        assertThat(testInvestor.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createInvestorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = investorRepository.findAll().size();

        // Create the Investor with an existing ID
        investor.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvestorMockMvc.perform(post("/api/investors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(investor)))
            .andExpect(status().isBadRequest());

        // Validate the Investor in the database
        List<Investor> investorList = investorRepository.findAll();
        assertThat(investorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllInvestors() throws Exception {
        // Initialize the database
        investorRepository.saveAndFlush(investor);

        // Get all the investorList
        restInvestorMockMvc.perform(get("/api/investors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(investor.getId().intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].shortDescription").value(hasItem(DEFAULT_SHORT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getInvestor() throws Exception {
        // Initialize the database
        investorRepository.saveAndFlush(investor);

        // Get the investor
        restInvestorMockMvc.perform(get("/api/investors/{id}", investor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(investor.getId().intValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.shortDescription").value(DEFAULT_SHORT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getAllInvestorsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        investorRepository.saveAndFlush(investor);

        // Get all the investorList where address equals to DEFAULT_ADDRESS
        defaultInvestorShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the investorList where address equals to UPDATED_ADDRESS
        defaultInvestorShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllInvestorsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        investorRepository.saveAndFlush(investor);

        // Get all the investorList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultInvestorShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the investorList where address equals to UPDATED_ADDRESS
        defaultInvestorShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllInvestorsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        investorRepository.saveAndFlush(investor);

        // Get all the investorList where address is not null
        defaultInvestorShouldBeFound("address.specified=true");

        // Get all the investorList where address is null
        defaultInvestorShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvestorsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        investorRepository.saveAndFlush(investor);

        // Get all the investorList where phone equals to DEFAULT_PHONE
        defaultInvestorShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the investorList where phone equals to UPDATED_PHONE
        defaultInvestorShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllInvestorsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        investorRepository.saveAndFlush(investor);

        // Get all the investorList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultInvestorShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the investorList where phone equals to UPDATED_PHONE
        defaultInvestorShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllInvestorsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        investorRepository.saveAndFlush(investor);

        // Get all the investorList where phone is not null
        defaultInvestorShouldBeFound("phone.specified=true");

        // Get all the investorList where phone is null
        defaultInvestorShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvestorsByDateOfBirthIsEqualToSomething() throws Exception {
        // Initialize the database
        investorRepository.saveAndFlush(investor);

        // Get all the investorList where dateOfBirth equals to DEFAULT_DATE_OF_BIRTH
        defaultInvestorShouldBeFound("dateOfBirth.equals=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the investorList where dateOfBirth equals to UPDATED_DATE_OF_BIRTH
        defaultInvestorShouldNotBeFound("dateOfBirth.equals=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllInvestorsByDateOfBirthIsInShouldWork() throws Exception {
        // Initialize the database
        investorRepository.saveAndFlush(investor);

        // Get all the investorList where dateOfBirth in DEFAULT_DATE_OF_BIRTH or UPDATED_DATE_OF_BIRTH
        defaultInvestorShouldBeFound("dateOfBirth.in=" + DEFAULT_DATE_OF_BIRTH + "," + UPDATED_DATE_OF_BIRTH);

        // Get all the investorList where dateOfBirth equals to UPDATED_DATE_OF_BIRTH
        defaultInvestorShouldNotBeFound("dateOfBirth.in=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllInvestorsByDateOfBirthIsNullOrNotNull() throws Exception {
        // Initialize the database
        investorRepository.saveAndFlush(investor);

        // Get all the investorList where dateOfBirth is not null
        defaultInvestorShouldBeFound("dateOfBirth.specified=true");

        // Get all the investorList where dateOfBirth is null
        defaultInvestorShouldNotBeFound("dateOfBirth.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvestorsByDateOfBirthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        investorRepository.saveAndFlush(investor);

        // Get all the investorList where dateOfBirth greater than or equals to DEFAULT_DATE_OF_BIRTH
        defaultInvestorShouldBeFound("dateOfBirth.greaterOrEqualThan=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the investorList where dateOfBirth greater than or equals to UPDATED_DATE_OF_BIRTH
        defaultInvestorShouldNotBeFound("dateOfBirth.greaterOrEqualThan=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllInvestorsByDateOfBirthIsLessThanSomething() throws Exception {
        // Initialize the database
        investorRepository.saveAndFlush(investor);

        // Get all the investorList where dateOfBirth less than or equals to DEFAULT_DATE_OF_BIRTH
        defaultInvestorShouldNotBeFound("dateOfBirth.lessThan=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the investorList where dateOfBirth less than or equals to UPDATED_DATE_OF_BIRTH
        defaultInvestorShouldBeFound("dateOfBirth.lessThan=" + UPDATED_DATE_OF_BIRTH);
    }


    @Test
    @Transactional
    public void getAllInvestorsByShortDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        investorRepository.saveAndFlush(investor);

        // Get all the investorList where shortDescription equals to DEFAULT_SHORT_DESCRIPTION
        defaultInvestorShouldBeFound("shortDescription.equals=" + DEFAULT_SHORT_DESCRIPTION);

        // Get all the investorList where shortDescription equals to UPDATED_SHORT_DESCRIPTION
        defaultInvestorShouldNotBeFound("shortDescription.equals=" + UPDATED_SHORT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllInvestorsByShortDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        investorRepository.saveAndFlush(investor);

        // Get all the investorList where shortDescription in DEFAULT_SHORT_DESCRIPTION or UPDATED_SHORT_DESCRIPTION
        defaultInvestorShouldBeFound("shortDescription.in=" + DEFAULT_SHORT_DESCRIPTION + "," + UPDATED_SHORT_DESCRIPTION);

        // Get all the investorList where shortDescription equals to UPDATED_SHORT_DESCRIPTION
        defaultInvestorShouldNotBeFound("shortDescription.in=" + UPDATED_SHORT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllInvestorsByShortDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        investorRepository.saveAndFlush(investor);

        // Get all the investorList where shortDescription is not null
        defaultInvestorShouldBeFound("shortDescription.specified=true");

        // Get all the investorList where shortDescription is null
        defaultInvestorShouldNotBeFound("shortDescription.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvestorsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        investorRepository.saveAndFlush(investor);

        // Get all the investorList where description equals to DEFAULT_DESCRIPTION
        defaultInvestorShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the investorList where description equals to UPDATED_DESCRIPTION
        defaultInvestorShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllInvestorsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        investorRepository.saveAndFlush(investor);

        // Get all the investorList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultInvestorShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the investorList where description equals to UPDATED_DESCRIPTION
        defaultInvestorShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllInvestorsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        investorRepository.saveAndFlush(investor);

        // Get all the investorList where description is not null
        defaultInvestorShouldBeFound("description.specified=true");

        // Get all the investorList where description is null
        defaultInvestorShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvestorsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        investor.setUser(user);
        investorRepository.saveAndFlush(investor);
        Long userId = user.getId();

        // Get all the investorList where user equals to userId
        defaultInvestorShouldBeFound("userId.equals=" + userId);

        // Get all the investorList where user equals to userId + 1
        defaultInvestorShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultInvestorShouldBeFound(String filter) throws Exception {
        restInvestorMockMvc.perform(get("/api/investors?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(investor.getId().intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].shortDescription").value(hasItem(DEFAULT_SHORT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultInvestorShouldNotBeFound(String filter) throws Exception {
        restInvestorMockMvc.perform(get("/api/investors?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingInvestor() throws Exception {
        // Get the investor
        restInvestorMockMvc.perform(get("/api/investors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvestor() throws Exception {
        // Initialize the database
        investorService.save(investor);

        int databaseSizeBeforeUpdate = investorRepository.findAll().size();

        // Update the investor
        Investor updatedInvestor = investorRepository.findOne(investor.getId());
        // Disconnect from session so that the updates on updatedInvestor are not directly saved in db
        em.detach(updatedInvestor);
        updatedInvestor
            .address(UPDATED_ADDRESS)
            .phone(UPDATED_PHONE)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .shortDescription(UPDATED_SHORT_DESCRIPTION)
            .description(UPDATED_DESCRIPTION);

        restInvestorMockMvc.perform(put("/api/investors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInvestor)))
            .andExpect(status().isOk());

        // Validate the Investor in the database
        List<Investor> investorList = investorRepository.findAll();
        assertThat(investorList).hasSize(databaseSizeBeforeUpdate);
        Investor testInvestor = investorList.get(investorList.size() - 1);
        assertThat(testInvestor.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testInvestor.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testInvestor.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testInvestor.getShortDescription()).isEqualTo(UPDATED_SHORT_DESCRIPTION);
        assertThat(testInvestor.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingInvestor() throws Exception {
        int databaseSizeBeforeUpdate = investorRepository.findAll().size();

        // Create the Investor

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInvestorMockMvc.perform(put("/api/investors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(investor)))
            .andExpect(status().isCreated());

        // Validate the Investor in the database
        List<Investor> investorList = investorRepository.findAll();
        assertThat(investorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInvestor() throws Exception {
        // Initialize the database
        investorService.save(investor);

        int databaseSizeBeforeDelete = investorRepository.findAll().size();

        // Get the investor
        restInvestorMockMvc.perform(delete("/api/investors/{id}", investor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Investor> investorList = investorRepository.findAll();
        assertThat(investorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Investor.class);
        Investor investor1 = new Investor();
        investor1.setId(1L);
        Investor investor2 = new Investor();
        investor2.setId(investor1.getId());
        assertThat(investor1).isEqualTo(investor2);
        investor2.setId(2L);
        assertThat(investor1).isNotEqualTo(investor2);
        investor1.setId(null);
        assertThat(investor1).isNotEqualTo(investor2);
    }
}
