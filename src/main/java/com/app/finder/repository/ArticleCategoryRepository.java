package com.app.finder.repository;

import com.app.finder.domain.ArticleCategory;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ArticleCategory entity.
 */
public interface ArticleCategoryRepository extends JpaRepository<ArticleCategory,Long> {

}
