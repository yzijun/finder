package com.app.finder.repository.search;

import com.app.finder.domain.ForbiddenWord;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ForbiddenWord entity.
 */
public interface ForbiddenWordSearchRepository extends ElasticsearchRepository<ForbiddenWord, Long> {
}
