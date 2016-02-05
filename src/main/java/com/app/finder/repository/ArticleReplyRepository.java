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
}
