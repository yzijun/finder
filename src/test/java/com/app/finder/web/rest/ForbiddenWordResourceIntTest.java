package com.app.finder.web.rest;

import com.app.finder.Application;
import com.app.finder.domain.ForbiddenWord;
import com.app.finder.repository.ForbiddenWordRepository;
import com.app.finder.repository.search.ForbiddenWordSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ForbiddenWordResource REST controller.
 *
 * @see ForbiddenWordResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ForbiddenWordResourceIntTest {

    private static final String DEFAULT_WORD = "AAAAA";
    private static final String UPDATED_WORD = "BBBBB";

    @Inject
    private ForbiddenWordRepository forbiddenWordRepository;

    @Inject
    private ForbiddenWordSearchRepository forbiddenWordSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restForbiddenWordMockMvc;

    private ForbiddenWord forbiddenWord;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ForbiddenWordResource forbiddenWordResource = new ForbiddenWordResource();
        ReflectionTestUtils.setField(forbiddenWordResource, "forbiddenWordSearchRepository", forbiddenWordSearchRepository);
        ReflectionTestUtils.setField(forbiddenWordResource, "forbiddenWordRepository", forbiddenWordRepository);
        this.restForbiddenWordMockMvc = MockMvcBuilders.standaloneSetup(forbiddenWordResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        forbiddenWord = new ForbiddenWord();
        forbiddenWord.setWord(DEFAULT_WORD);
    }

    @Test
    @Transactional
    public void createForbiddenWord() throws Exception {
        int databaseSizeBeforeCreate = forbiddenWordRepository.findAll().size();

        // Create the ForbiddenWord

        restForbiddenWordMockMvc.perform(post("/api/forbiddenWords")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(forbiddenWord)))
                .andExpect(status().isCreated());

        // Validate the ForbiddenWord in the database
        List<ForbiddenWord> forbiddenWords = forbiddenWordRepository.findAll();
        assertThat(forbiddenWords).hasSize(databaseSizeBeforeCreate + 1);
        ForbiddenWord testForbiddenWord = forbiddenWords.get(forbiddenWords.size() - 1);
        assertThat(testForbiddenWord.getWord()).isEqualTo(DEFAULT_WORD);
    }

    @Test
    @Transactional
    public void getAllForbiddenWords() throws Exception {
        // Initialize the database
        forbiddenWordRepository.saveAndFlush(forbiddenWord);

        // Get all the forbiddenWords
        restForbiddenWordMockMvc.perform(get("/api/forbiddenWords?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(forbiddenWord.getId().intValue())))
                .andExpect(jsonPath("$.[*].word").value(hasItem(DEFAULT_WORD.toString())));
    }

    @Test
    @Transactional
    public void getForbiddenWord() throws Exception {
        // Initialize the database
        forbiddenWordRepository.saveAndFlush(forbiddenWord);

        // Get the forbiddenWord
        restForbiddenWordMockMvc.perform(get("/api/forbiddenWords/{id}", forbiddenWord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(forbiddenWord.getId().intValue()))
            .andExpect(jsonPath("$.word").value(DEFAULT_WORD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingForbiddenWord() throws Exception {
        // Get the forbiddenWord
        restForbiddenWordMockMvc.perform(get("/api/forbiddenWords/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateForbiddenWord() throws Exception {
        // Initialize the database
        forbiddenWordRepository.saveAndFlush(forbiddenWord);

		int databaseSizeBeforeUpdate = forbiddenWordRepository.findAll().size();

        // Update the forbiddenWord
        forbiddenWord.setWord(UPDATED_WORD);

        restForbiddenWordMockMvc.perform(put("/api/forbiddenWords")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(forbiddenWord)))
                .andExpect(status().isOk());

        // Validate the ForbiddenWord in the database
        List<ForbiddenWord> forbiddenWords = forbiddenWordRepository.findAll();
        assertThat(forbiddenWords).hasSize(databaseSizeBeforeUpdate);
        ForbiddenWord testForbiddenWord = forbiddenWords.get(forbiddenWords.size() - 1);
        assertThat(testForbiddenWord.getWord()).isEqualTo(UPDATED_WORD);
    }

    @Test
    @Transactional
    public void deleteForbiddenWord() throws Exception {
        // Initialize the database
        forbiddenWordRepository.saveAndFlush(forbiddenWord);

		int databaseSizeBeforeDelete = forbiddenWordRepository.findAll().size();

        // Get the forbiddenWord
        restForbiddenWordMockMvc.perform(delete("/api/forbiddenWords/{id}", forbiddenWord.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ForbiddenWord> forbiddenWords = forbiddenWordRepository.findAll();
        assertThat(forbiddenWords).hasSize(databaseSizeBeforeDelete - 1);
    }
}
