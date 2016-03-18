package com.app.finder.service;

import com.app.finder.domain.ArticleFavorite;
import com.app.finder.domain.User;
import com.app.finder.repository.ArticleFavoriteRepository;
import com.app.finder.repository.UserRepository;
import com.app.finder.security.SecurityUtils;
import com.app.finder.web.rest.dto.ArticleFavoriteDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import java.time.ZonedDateTime;
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

    @Inject
    private UserRepository userRepository;
    
    /**
     * Save a articleFavorite.
     * @return the persisted entity
     */
    // 清空ArticleService中的缓存名称是  articleDetail 对应的文章ID 缓存
    @CacheEvict(value = "articleDetail", key = "#articleFavorite.getArticle().getId()")
    public ArticleFavoriteDTO save(ArticleFavorite articleFavorite) {
        log.debug("Request to save ArticleFavorite : {}", articleFavorite);
        // 设置默认值
        articleFavorite.setCreatedDate(ZonedDateTime.now());
        // 页面参数传不过来,重新查找User
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        articleFavorite.setUser(user);
        
        ArticleFavorite result = articleFavoriteRepository.save(articleFavorite);
        
        // 取得文章的收藏数
     	Integer countArticleSaveAid = articleFavoriteRepository.findByCountArticleFavoriteAid(articleFavorite.getArticle().getId());
        
        return new ArticleFavoriteDTO(result, countArticleSaveAid);
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
    
    /**
     * 删除文章收藏并清空该文章缓存
     * @param id 文章收藏id
     * @param aid 文章id
     */
    // 清空ArticleService中的缓存名称是  articleDetail 对应的文章ID 缓存
    @CacheEvict(value = "articleDetail", key = "#aid")
    public void delete(Long id, Long aid) {
    	log.debug("Request to delete ArticleFavorite id: {}, aid: {}", id, aid);
    	articleFavoriteRepository.delete(id);
    }

}
