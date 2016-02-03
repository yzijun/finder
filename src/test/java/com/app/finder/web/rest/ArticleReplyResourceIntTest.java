package com.app.finder.web.rest;

import com.app.finder.Application;
import com.app.finder.domain.ArticleReply;
import com.app.finder.repository.ArticleReplyRepository;

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
 * Test class for the ArticleReplyResource REST controller.
 *
 * @see ArticleReplyResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ArticleReplyResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_CONTENT = "AAAAA";
    private static final String UPDATED_CONTENT = "BBBBB";

    private static final Boolean DEFAULT_PUBLISHED = false;
    private static final Boolean UPDATED_PUBLISHED = true;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATED_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATED_DATE);

    @Inject
    private ArticleReplyRepository articleReplyRepository;


    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restArticleReplyMockMvc;

    private ArticleReply articleReply;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ArticleReplyResource articleReplyResource = new ArticleReplyResource();
        ReflectionTestUtils.setField(articleReplyResource, "articleReplyRepository", articleReplyRepository);
        this.restArticleReplyMockMvc = MockMvcBuilders.standaloneSetup(articleReplyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        articleReply = new ArticleReply();
        articleReply.setContent(DEFAULT_CONTENT);
        articleReply.setPublished(DEFAULT_PUBLISHED);
        articleReply.setCreatedDate(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    public void createArticleReply() throws Exception {
        int databaseSizeBeforeCreate = articleReplyRepository.findAll().size();

        // Create the ArticleReply

        restArticleReplyMockMvc.perform(post("/api/articleReplys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(articleReply)))
                .andExpect(status().isCreated());

        // Validate the ArticleReply in the database
        List<ArticleReply> articleReplys = articleReplyRepository.findAll();
        assertThat(articleReplys).hasSize(databaseSizeBeforeCreate + 1);
        ArticleReply testArticleReply = articleReplys.get(articleReplys.size() - 1);
        assertThat(testArticleReply.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testArticleReply.getPublished()).isEqualTo(DEFAULT_PUBLISHED);
        assertThat(testArticleReply.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = articleReplyRepository.findAll().size();
        // set the field null
        articleReply.setContent(null);

        // Create the ArticleReply, which fails.

        restArticleReplyMockMvc.perform(post("/api/articleReplys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(articleReply)))
                .andExpect(status().isBadRequest());

        List<ArticleReply> articleReplys = articleReplyRepository.findAll();
        assertThat(articleReplys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllArticleReplys() throws Exception {
        // Initialize the database
        articleReplyRepository.saveAndFlush(articleReply);

        // Get all the articleReplys
        restArticleReplyMockMvc.perform(get("/api/articleReplys?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(articleReply.getId().intValue())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
                .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED.booleanValue())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE_STR)));
    }

    @Test
    @Transactional
    public void getArticleReply() throws Exception {
        // Initialize the database
        articleReplyRepository.saveAndFlush(articleReply);

        // Get the articleReply
        restArticleReplyMockMvc.perform(get("/api/articleReplys/{id}", articleReply.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(articleReply.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.published").value(DEFAULT_PUBLISHED.booleanValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingArticleReply() throws Exception {
        // Get the articleReply
        restArticleReplyMockMvc.perform(get("/api/articleReplys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArticleReply() throws Exception {
        // Initialize the database
        articleReplyRepository.saveAndFlush(articleReply);

		int databaseSizeBeforeUpdate = articleReplyRepository.findAll().size();

        // Update the articleReply
        articleReply.setContent(UPDATED_CONTENT);
        articleReply.setPublished(UPDATED_PUBLISHED);
        articleReply.setCreatedDate(UPDATED_CREATED_DATE);

        restArticleReplyMockMvc.perform(put("/api/articleReplys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(articleReply)))
                .andExpect(status().isOk());

        // Validate the ArticleReply in the database
        List<ArticleReply> articleReplys = articleReplyRepository.findAll();
        assertThat(articleReplys).hasSize(databaseSizeBeforeUpdate);
        ArticleReply testArticleReply = articleReplys.get(articleReplys.size() - 1);
        assertThat(testArticleReply.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testArticleReply.getPublished()).isEqualTo(UPDATED_PUBLISHED);
        assertThat(testArticleReply.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void deleteArticleReply() throws Exception {
        // Initialize the database
        articleReplyRepository.saveAndFlush(articleReply);

		int databaseSizeBeforeDelete = articleReplyRepository.findAll().size();

        // Get the articleReply
        restArticleReplyMockMvc.perform(delete("/api/articleReplys/{id}", articleReply.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ArticleReply> articleReplys = articleReplyRepository.findAll();
        assertThat(articleReplys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
