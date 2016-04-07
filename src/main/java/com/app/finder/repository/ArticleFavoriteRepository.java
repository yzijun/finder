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

    // 取得该文章收获喜欢数 aid = article_id
    @Query("select count(*) from ArticleFavorite af where af.article.id = ?1")
    Integer findByCountArticleFavoriteAid(Long aid);
    
    // 根据用户id和文章id查询文章收获喜欢
    ArticleFavorite findByUserIdAndArticleId(Long uid, Long aid);
    
    // 根据文章收获喜欢ID和用户ID和文章ID删除文章收获喜欢记录
    @Modifying
    @Query("delete ArticleFavorite f where f.id = ?1 and f.user.id = ?2 and f.article.id = ?3")
    void deleteByidUid(Long id, Long uid, Long aid);
}
