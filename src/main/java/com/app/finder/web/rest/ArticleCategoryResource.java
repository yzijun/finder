package com.app.finder.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.app.finder.domain.ArticleCategory;
import com.app.finder.repository.ArticleCategoryRepository;
import com.app.finder.repository.search.ArticleCategorySearchRepository;
import com.app.finder.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ArticleCategory.
 */
@RestController
@RequestMapping("/api")
public class ArticleCategoryResource {

    private final Logger log = LoggerFactory.getLogger(ArticleCategoryResource.class);
        
    @Inject
    private ArticleCategoryRepository articleCategoryRepository;
    
    @Inject
    private ArticleCategorySearchRepository articleCategorySearchRepository;
    
    /**
     * POST  /articleCategorys -> Create a new articleCategory.
     */
    @RequestMapping(value = "/articleCategorys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ArticleCategory> createArticleCategory(@Valid @RequestBody ArticleCategory articleCategory) throws URISyntaxException {
        log.debug("REST request to save ArticleCategory : {}", articleCategory);
        if (articleCategory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("articleCategory", "idexists", "A new articleCategory cannot already have an ID")).body(null);
        }
        ArticleCategory result = articleCategoryRepository.save(articleCategory);
        articleCategorySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/articleCategorys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("articleCategory", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /articleCategorys -> Updates an existing articleCategory.
     */
    @RequestMapping(value = "/articleCategorys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ArticleCategory> updateArticleCategory(@Valid @RequestBody ArticleCategory articleCategory) throws URISyntaxException {
        log.debug("REST request to update ArticleCategory : {}", articleCategory);
        if (articleCategory.getId() == null) {
            return createArticleCategory(articleCategory);
        }
        ArticleCategory result = articleCategoryRepository.save(articleCategory);
        articleCategorySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("articleCategory", articleCategory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /articleCategorys -> get all the articleCategorys.
     */
    @RequestMapping(value = "/articleCategorys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ArticleCategory> getAllArticleCategorys() {
        log.debug("REST request to get all ArticleCategorys");
        return articleCategoryRepository.findAll();
    }

    /**
     * GET  /articleCategorys/:id -> get the "id" articleCategory.
     */
    @RequestMapping(value = "/articleCategorys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ArticleCategory> getArticleCategory(@PathVariable Long id) {
        log.debug("REST request to get ArticleCategory : {}", id);
        ArticleCategory articleCategory = articleCategoryRepository.findOne(id);
        return Optional.ofNullable(articleCategory)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /articleCategorys/:id -> delete the "id" articleCategory.
     */
    @RequestMapping(value = "/articleCategorys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteArticleCategory(@PathVariable Long id) {
        log.debug("REST request to delete ArticleCategory : {}", id);
        articleCategoryRepository.delete(id);
        articleCategorySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("articleCategory", id.toString())).build();
    }

    /**
     * SEARCH  /_search/articleCategorys/:query -> search for the articleCategory corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/articleCategorys/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ArticleCategory> searchArticleCategorys(@PathVariable String query) {
        log.debug("REST request to search ArticleCategorys for query {}", query);
        return StreamSupport
            .stream(articleCategorySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
