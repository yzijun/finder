package com.app.finder.service;

import com.app.finder.domain.ArticleFavorite;
import com.app.finder.repository.ArticleFavoriteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ArticleFavorite.
 * 文章收藏
 */
@Service
@Transactional
public class ArticleFavoriteService {

    private final Logger log = LoggerFactory.getLogger(ArticleFavoriteService.class);
    
    @Inject
    private ArticleFavoriteRepository articleFavoriteRepository;

    
    /**
     * Save a articleFavorite.
     * @return the persisted entity
     */
    public ArticleFavorite save(ArticleFavorite articleFavorite) {
        log.debug("Request to save ArticleFavorite : {}", articleFavorite);
        ArticleFavorite result = articleFavoriteRepository.save(articleFavorite);
        return result;
    }

    /**
     *  get all the articleFavorites.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<ArticleFavorite> findAll() {
        log.debug("Request to get all ArticleFavorites");
        List<ArticleFavorite> result = articleFavoriteRepository.findAll();
        return result;
    }

    /**
     *  get one articleFavorite by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ArticleFavorite findOne(Long id) {
        log.debug("Request to get ArticleFavorite : {}", id);
        ArticleFavorite articleFavorite = articleFavoriteRepository.findOne(id);
        return articleFavorite;
    }

    /**
     *  delete the  articleFavorite by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete ArticleFavorite : {}", id);
        articleFavoriteRepository.delete(id);
    }

}
