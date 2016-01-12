package com.app.finder.repository;

import com.app.finder.domain.Article;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Article entity.
 */
public interface ArticleRepository extends JpaRepository<Article,Long> {

    @Query("select article from Article article where article.user.login = ?#{principal.username}")
    List<Article> findByUserIsCurrentUser();
    
    /*
     * 访问文章详细页面时更新页面浏览次数,
     * 不用默认更新原因是会更新所有字段没有必要
     */
    @Modifying
    @Query("update Article a set a.pageView = a.pageView + 1 where a.id = ?1")
    int updatePageView(Long id);
    
    //取得该用户全部文章数 uid = user_id
    @Query("select count(*) from Article article where article.user.id = ?1 group by article.user.id")
    int findByCountArticleIsUid(Long uid);

}
