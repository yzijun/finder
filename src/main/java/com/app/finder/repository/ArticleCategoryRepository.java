package com.app.finder.repository;

import com.app.finder.domain.ArticleCategory;

import org.springframework.data.jpa.repository.*;

import java.util.List;

import javax.persistence.QueryHint;

/**
 * Spring Data JPA repository for the ArticleCategory entity.
 */
public interface ArticleCategoryRepository extends JpaRepository<ArticleCategory,Long> {

	// 通过@QueryHint来实现查询缓存
	@Query("from ArticleCategory")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	List<ArticleCategory> findAllCached();
}
