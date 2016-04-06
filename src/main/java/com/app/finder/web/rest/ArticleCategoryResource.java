package com.app.finder.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.finder.domain.ArticleCategory;
import com.app.finder.repository.ArticleCategoryRepository;
import com.app.finder.security.AuthoritiesConstants;
import com.app.finder.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing ArticleCategory.
 */
@RestController
@RequestMapping("/api")
public class ArticleCategoryResource {

    private final Logger log = LoggerFactory.getLogger(ArticleCategoryResource.class);
        
    @Inject
    private ArticleCategoryRepository articleCategoryRepository;
    
    
    /**
     * POST  /articleCategorys -> Create a new articleCategory.
     */
    @RequestMapping(value = "/articleCategorys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.ARTICLE_ADMIN) //文章管理者角色可以访问
    public ResponseEntity<ArticleCategory> createArticleCategory(@Valid @RequestBody ArticleCategory articleCategory) throws URISyntaxException {
        log.debug("REST request to save ArticleCategory : {}", articleCategory);
        if (articleCategory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("articleCategory", "idexists", "A new articleCategory cannot already have an ID")).body(null);
        }
        ArticleCategory result = articleCategoryRepository.save(articleCategory);
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
    @Secured(AuthoritiesConstants.ARTICLE_ADMIN) //文章管理者角色可以访问
    public ResponseEntity<ArticleCategory> updateArticleCategory(@Valid @RequestBody ArticleCategory articleCategory) throws URISyntaxException {
        log.debug("REST request to update ArticleCategory : {}", articleCategory);
        if (articleCategory.getId() == null) {
            return createArticleCategory(articleCategory);
        }
        ArticleCategory result = articleCategoryRepository.save(articleCategory);
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
        List<ArticleCategory> list =  articleCategoryRepository.findAllCached();
        // 过滤子条目的文章类别
        return list.stream()
        		   .filter(s -> s.getParentId() != null)
        	       .collect(Collectors.toList());
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
    @Secured(AuthoritiesConstants.ARTICLE_ADMIN) //文章管理者角色可以访问
    public ResponseEntity<Void> deleteArticleCategory(@PathVariable Long id) {
        log.debug("REST request to delete ArticleCategory : {}", id);
        articleCategoryRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("articleCategory", id.toString())).build();
    }

}
