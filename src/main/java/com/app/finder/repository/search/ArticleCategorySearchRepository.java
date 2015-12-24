package com.app.finder.repository.search;

import com.app.finder.domain.ArticleCategory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ArticleCategory entity.
 */
public interface ArticleCategorySearchRepository extends ElasticsearchRepository<ArticleCategory, Long> {
}
