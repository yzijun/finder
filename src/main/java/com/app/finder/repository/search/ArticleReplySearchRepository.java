package com.app.finder.repository.search;

import com.app.finder.domain.ArticleReply;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ArticleReply entity.
 */
public interface ArticleReplySearchRepository extends ElasticsearchRepository<ArticleReply, Long> {
}
