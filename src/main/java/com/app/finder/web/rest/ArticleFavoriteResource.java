package com.app.finder.web.rest;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.finder.domain.ArticleFavorite;
import com.app.finder.service.ArticleFavoriteService;
import com.app.finder.web.rest.dto.ArticleFavoriteDTO;
import com.app.finder.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing ArticleFavorite.
 * 文章收获喜欢
 */
@RestController
@RequestMapping("/api")
public class ArticleFavoriteResource {

    private final Logger log = LoggerFactory.getLogger(ArticleFavoriteResource.class);
        
    @Inject
    private ArticleFavoriteService articleFavoriteService;
    
    /**
     * POST  /articleFavorites -> Create a new articleFavorite.
     */
    @RequestMapping(value = "/articleFavorites",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ArticleFavoriteDTO> createArticleFavorite(@RequestBody ArticleFavorite articleFavorite) throws URISyntaxException {
        log.debug("REST request to save ArticleFavorite : {}", articleFavorite);
        if (articleFavorite.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("articleFavorite", "idexists", "A new articleFavorite cannot already have an ID")).body(null);
        }
        ArticleFavoriteDTO result = articleFavoriteService.save(articleFavorite);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * PUT  /articleFavorites -> Updates an existing articleFavorite.
     */
    @RequestMapping(value = "/articleFavorites",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ArticleFavoriteDTO> updateArticleFavorite(@RequestBody ArticleFavorite articleFavorite) throws URISyntaxException {
        log.debug("REST request to update ArticleFavorite : {}", articleFavorite);
        if (articleFavorite.getId() == null) {
            return createArticleFavorite(articleFavorite);
        }
        ArticleFavoriteDTO result = articleFavoriteService.save(articleFavorite);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("articleFavorite", articleFavorite.getId().toString()))
            .body(result);
    }

    /**
     * GET  /articleFavorites -> get all the articleFavorites.
     */
    @RequestMapping(value = "/articleFavorites",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ArticleFavorite> getAllArticleFavorites() {
        log.debug("REST request to get all ArticleFavorites");
        return articleFavoriteService.findAll();
            }

    /**
     * GET  /articleFavorites/:id -> get the "id" articleFavorite.
     */
    @RequestMapping(value = "/articleFavorites/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ArticleFavorite> getArticleFavorite(@PathVariable Long id) {
        log.debug("REST request to get ArticleFavorite : {}", id);
        ArticleFavorite articleFavorite = articleFavoriteService.findOne(id);
        return Optional.ofNullable(articleFavorite)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 
     */
    @RequestMapping(value = "/articleFavorites/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteArticleFavorite(@PathVariable Long id) {
        log.debug("REST request to delete ArticleFavorite : {}", id);
        articleFavoriteService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("articleFavorite", id.toString())).build();
    }
    
    /**
     * 删除文章收藏并清空该文章缓存
     */
    @RequestMapping(value = "/delFavoriteWithCache",
    		method = RequestMethod.GET,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Integer delFavoriteWithCache(@RequestParam(required = true) Long id,
    												 @RequestParam(required = true) Long aid) {
    	log.debug("REST request to delete delFavoriteWithCache");
    	Integer favoriteCount = articleFavoriteService.delete(id, aid);
    	return favoriteCount;
    }

}
