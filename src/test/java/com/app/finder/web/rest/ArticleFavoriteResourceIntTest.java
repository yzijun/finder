package com.app.finder.web.rest;

import com.app.finder.Application;
import com.app.finder.domain.ArticleFavorite;
import com.app.finder.repository.ArticleFavoriteRepository;
import com.app.finder.service.ArticleFavoriteService;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ArticleFavoriteResource REST controller.
 *
 * @see ArticleFavoriteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ArticleFavoriteResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATED_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATED_DATE);

    @Inject
    private ArticleFavoriteRepository articleFavoriteRepository;

    @Inject
    private ArticleFavoriteService articleFavoriteService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restArticleFavoriteMockMvc;

    private ArticleFavorite articleFavorite;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ArticleFavoriteResource articleFavoriteResource = new ArticleFavoriteResource();
        ReflectionTestUtils.setField(articleFavoriteResource, "articleFavoriteService", articleFavoriteService);
        this.restArticleFavoriteMockMvc = MockMvcBuilders.standaloneSetup(articleFavoriteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        articleFavorite = new ArticleFavorite();
        articleFavorite.setCreatedDate(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    public void createArticleFavorite() throws Exception {
        int databaseSizeBeforeCreate = articleFavoriteRepository.findAll().size();

        // Create the ArticleFavorite

        restArticleFavoriteMockMvc.perform(post("/api/articleFavorites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(articleFavorite)))
                .andExpect(status().isCreated());

        // Validate the ArticleFavorite in the database
        List<ArticleFavorite> articleFavorites = articleFavoriteRepository.findAll();
        assertThat(articleFavorites).hasSize(databaseSizeBeforeCreate + 1);
        ArticleFavorite testArticleFavorite = articleFavorites.get(articleFavorites.size() - 1);
        assertThat(testArticleFavorite.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllArticleFavorites() throws Exception {
        // Initialize the database
        articleFavoriteRepository.saveAndFlush(articleFavorite);

        // Get all the articleFavorites
        restArticleFavoriteMockMvc.perform(get("/api/articleFavorites?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(articleFavorite.getId().intValue())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE_STR)));
    }

    @Test
    @Transactional
    public void getArticleFavorite() throws Exception {
        // Initialize the database
        articleFavoriteRepository.saveAndFlush(articleFavorite);

        // Get the articleFavorite
        restArticleFavoriteMockMvc.perform(get("/api/articleFavorites/{id}", articleFavorite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(articleFavorite.getId().intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingArticleFavorite() throws Exception {
        // Get the articleFavorite
        restArticleFavoriteMockMvc.perform(get("/api/articleFavorites/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArticleFavorite() throws Exception {
        // Initialize the database
        articleFavoriteRepository.saveAndFlush(articleFavorite);

		int databaseSizeBeforeUpdate = articleFavoriteRepository.findAll().size();

        // Update the articleFavorite
        articleFavorite.setCreatedDate(UPDATED_CREATED_DATE);

        restArticleFavoriteMockMvc.perform(put("/api/articleFavorites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(articleFavorite)))
                .andExpect(status().isOk());

        // Validate the ArticleFavorite in the database
        List<ArticleFavorite> articleFavorites = articleFavoriteRepository.findAll();
        assertThat(articleFavorites).hasSize(databaseSizeBeforeUpdate);
        ArticleFavorite testArticleFavorite = articleFavorites.get(articleFavorites.size() - 1);
        assertThat(testArticleFavorite.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void deleteArticleFavorite() throws Exception {
        // Initialize the database
        articleFavoriteRepository.saveAndFlush(articleFavorite);

		int databaseSizeBeforeDelete = articleFavoriteRepository.findAll().size();

        // Get the articleFavorite
        restArticleFavoriteMockMvc.perform(delete("/api/articleFavorites/{id}", articleFavorite.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ArticleFavorite> articleFavorites = articleFavoriteRepository.findAll();
        assertThat(articleFavorites).hasSize(databaseSizeBeforeDelete - 1);
    }
}
