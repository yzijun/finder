package com.app.finder.web.rest;

import com.app.finder.Application;
import com.app.finder.domain.ArticleCategory;
import com.app.finder.repository.ArticleCategoryRepository;
import com.app.finder.repository.search.ArticleCategorySearchRepository;

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
 * Test class for the ArticleCategoryResource REST controller.
 *
 * @see ArticleCategoryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ArticleCategoryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private ArticleCategoryRepository articleCategoryRepository;

    @Inject
    private ArticleCategorySearchRepository articleCategorySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restArticleCategoryMockMvc;

    private ArticleCategory articleCategory;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ArticleCategoryResource articleCategoryResource = new ArticleCategoryResource();
        ReflectionTestUtils.setField(articleCategoryResource, "articleCategorySearchRepository", articleCategorySearchRepository);
        ReflectionTestUtils.setField(articleCategoryResource, "articleCategoryRepository", articleCategoryRepository);
        this.restArticleCategoryMockMvc = MockMvcBuilders.standaloneSetup(articleCategoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        articleCategory = new ArticleCategory();
        articleCategory.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createArticleCategory() throws Exception {
        int databaseSizeBeforeCreate = articleCategoryRepository.findAll().size();

        // Create the ArticleCategory

        restArticleCategoryMockMvc.perform(post("/api/articleCategorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(articleCategory)))
                .andExpect(status().isCreated());

        // Validate the ArticleCategory in the database
        List<ArticleCategory> articleCategorys = articleCategoryRepository.findAll();
        assertThat(articleCategorys).hasSize(databaseSizeBeforeCreate + 1);
        ArticleCategory testArticleCategory = articleCategorys.get(articleCategorys.size() - 1);
        assertThat(testArticleCategory.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = articleCategoryRepository.findAll().size();
        // set the field null
        articleCategory.setName(null);

        // Create the ArticleCategory, which fails.

        restArticleCategoryMockMvc.perform(post("/api/articleCategorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(articleCategory)))
                .andExpect(status().isBadRequest());

        List<ArticleCategory> articleCategorys = articleCategoryRepository.findAll();
        assertThat(articleCategorys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllArticleCategorys() throws Exception {
        // Initialize the database
        articleCategoryRepository.saveAndFlush(articleCategory);

        // Get all the articleCategorys
        restArticleCategoryMockMvc.perform(get("/api/articleCategorys?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(articleCategory.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getArticleCategory() throws Exception {
        // Initialize the database
        articleCategoryRepository.saveAndFlush(articleCategory);

        // Get the articleCategory
        restArticleCategoryMockMvc.perform(get("/api/articleCategorys/{id}", articleCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(articleCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingArticleCategory() throws Exception {
        // Get the articleCategory
        restArticleCategoryMockMvc.perform(get("/api/articleCategorys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArticleCategory() throws Exception {
        // Initialize the database
        articleCategoryRepository.saveAndFlush(articleCategory);

		int databaseSizeBeforeUpdate = articleCategoryRepository.findAll().size();

        // Update the articleCategory
        articleCategory.setName(UPDATED_NAME);

        restArticleCategoryMockMvc.perform(put("/api/articleCategorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(articleCategory)))
                .andExpect(status().isOk());

        // Validate the ArticleCategory in the database
        List<ArticleCategory> articleCategorys = articleCategoryRepository.findAll();
        assertThat(articleCategorys).hasSize(databaseSizeBeforeUpdate);
        ArticleCategory testArticleCategory = articleCategorys.get(articleCategorys.size() - 1);
        assertThat(testArticleCategory.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteArticleCategory() throws Exception {
        // Initialize the database
        articleCategoryRepository.saveAndFlush(articleCategory);

		int databaseSizeBeforeDelete = articleCategoryRepository.findAll().size();

        // Get the articleCategory
        restArticleCategoryMockMvc.perform(delete("/api/articleCategorys/{id}", articleCategory.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ArticleCategory> articleCategorys = articleCategoryRepository.findAll();
        assertThat(articleCategorys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
