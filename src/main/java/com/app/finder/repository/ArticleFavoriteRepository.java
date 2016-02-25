package com.app.finder.repository;

import com.app.finder.domain.ArticleFavorite;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ArticleFavorite entity.
 */
public interface ArticleFavoriteRepository extends JpaRepository<ArticleFavorite,Long> {

    @Query("select articleFavorite from ArticleFavorite articleFavorite where articleFavorite.user.login = ?#{principal.username}")
    List<ArticleFavorite> findByUserIsCurrentUser();

    // 取得该文章的收藏数 aid = article_id
    @Query("select count(*) from ArticleFavorite af where af.article.id = ?1")
    Integer findByCountArticleFavoriteAid(Long aid);
    
    // 根据用户id和文章id查询文章收藏
    ArticleFavorite findByUserIdAndArticleId(Long uid, Long aid);
}
