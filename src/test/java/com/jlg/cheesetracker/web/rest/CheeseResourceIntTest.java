package com.jlg.cheesetracker.web.rest;

import com.jlg.cheesetracker.CheesetrackerApp;

import com.jlg.cheesetracker.domain.Cheese;
import com.jlg.cheesetracker.repository.CheeseRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CheeseResource REST controller.
 *
 * @see CheeseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CheesetrackerApp.class)
public class CheeseResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final String DEFAULT_MILK = "AAAAA";
    private static final String UPDATED_MILK = "BBBBB";

    @Inject
    private CheeseRepository cheeseRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCheeseMockMvc;

    private Cheese cheese;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CheeseResource cheeseResource = new CheeseResource();
        ReflectionTestUtils.setField(cheeseResource, "cheeseRepository", cheeseRepository);
        this.restCheeseMockMvc = MockMvcBuilders.standaloneSetup(cheeseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cheese createEntity(EntityManager em) {
        Cheese cheese = new Cheese()
                .name(DEFAULT_NAME)
                .milk(DEFAULT_MILK);
        return cheese;
    }

    @Before
    public void initTest() {
        cheese = createEntity(em);
    }

    @Test
    @Transactional
    public void createCheese() throws Exception {
        int databaseSizeBeforeCreate = cheeseRepository.findAll().size();

        // Create the Cheese

        restCheeseMockMvc.perform(post("/api/cheeses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cheese)))
                .andExpect(status().isCreated());

        // Validate the Cheese in the database
        List<Cheese> cheeses = cheeseRepository.findAll();
        assertThat(cheeses).hasSize(databaseSizeBeforeCreate + 1);
        Cheese testCheese = cheeses.get(cheeses.size() - 1);
        assertThat(testCheese.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCheese.getMilk()).isEqualTo(DEFAULT_MILK);
    }

    @Test
    @Transactional
    public void getAllCheeses() throws Exception {
        // Initialize the database
        cheeseRepository.saveAndFlush(cheese);

        // Get all the cheeses
        restCheeseMockMvc.perform(get("/api/cheeses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(cheese.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].milk").value(hasItem(DEFAULT_MILK.toString())));
    }

    @Test
    @Transactional
    public void getCheese() throws Exception {
        // Initialize the database
        cheeseRepository.saveAndFlush(cheese);

        // Get the cheese
        restCheeseMockMvc.perform(get("/api/cheeses/{id}", cheese.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cheese.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.milk").value(DEFAULT_MILK.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCheese() throws Exception {
        // Get the cheese
        restCheeseMockMvc.perform(get("/api/cheeses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCheese() throws Exception {
        // Initialize the database
        cheeseRepository.saveAndFlush(cheese);
        int databaseSizeBeforeUpdate = cheeseRepository.findAll().size();

        // Update the cheese
        Cheese updatedCheese = cheeseRepository.findOne(cheese.getId());
        updatedCheese
                .name(UPDATED_NAME)
                .milk(UPDATED_MILK);

        restCheeseMockMvc.perform(put("/api/cheeses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCheese)))
                .andExpect(status().isOk());

        // Validate the Cheese in the database
        List<Cheese> cheeses = cheeseRepository.findAll();
        assertThat(cheeses).hasSize(databaseSizeBeforeUpdate);
        Cheese testCheese = cheeses.get(cheeses.size() - 1);
        assertThat(testCheese.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCheese.getMilk()).isEqualTo(UPDATED_MILK);
    }

    @Test
    @Transactional
    public void deleteCheese() throws Exception {
        // Initialize the database
        cheeseRepository.saveAndFlush(cheese);
        int databaseSizeBeforeDelete = cheeseRepository.findAll().size();

        // Get the cheese
        restCheeseMockMvc.perform(delete("/api/cheeses/{id}", cheese.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Cheese> cheeses = cheeseRepository.findAll();
        assertThat(cheeses).hasSize(databaseSizeBeforeDelete - 1);
    }
}
