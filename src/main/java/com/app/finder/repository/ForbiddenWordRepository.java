package com.app.finder.repository;

import com.app.finder.domain.ForbiddenWord;

import org.springframework.data.jpa.repository.*;

import java.util.List;

import javax.persistence.QueryHint;

/**
 * Spring Data JPA repository for the ForbiddenWord entity.
 */
public interface ForbiddenWordRepository extends JpaRepository<ForbiddenWord, Long> {

	// 通过@QueryHint来实现查询缓存
	@Query("from ForbiddenWord")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	List<ForbiddenWord> findAllCached();
}
