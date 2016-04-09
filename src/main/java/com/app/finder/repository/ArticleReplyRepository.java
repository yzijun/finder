package com.app.finder.repository;

import com.app.finder.domain.ArticleReply;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ArticleReply entity.
 */
public interface ArticleReplyRepository extends JpaRepository<ArticleReply,Long> {

	/**
	 * 查询一篇文章对应的全部评论
	 * @return
	 */
    @Query("select a from ArticleReply a left join fetch a.replyer where a.article.id = ?1 and published = ?2 order by a.createdDate asc")
    List<ArticleReply> findReplyByArticleID(Long aid, boolean published);
    
    // 取得该文章全部评论数 aid 文章id
    @Query("select count(*) from ArticleReply reply where reply.article.id = ?1 and reply.published = true")
    Integer findByCountArticleUid(Long aid);
    
    //取得该用户全部评论数 uid = replyer_id
    @Query("select count(*) from ArticleReply reply where reply.replyer.id = ?1 and reply.published = true")
    Integer findByCountArticleReplyUid(Long uid);
}
