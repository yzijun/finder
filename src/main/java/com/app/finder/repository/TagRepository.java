package com.app.finder.repository;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.app.finder.domain.Tag;

/**
 * Spring Data JPA repository for the Tag entity.
 */
public interface TagRepository extends JpaRepository<Tag,Long> {

	// 通过@QueryHint来实现查询缓存
	@Query("from Tag")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	List<Tag> findAllCached();
}
