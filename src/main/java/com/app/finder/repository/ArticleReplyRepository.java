package com.app.finder.repository;

import com.app.finder.domain.ArticleReply;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ArticleReply entity.
 */
public interface ArticleReplyRepository extends JpaRepository<ArticleReply,Long> {

}
