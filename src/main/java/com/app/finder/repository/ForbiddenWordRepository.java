package com.app.finder.repository;

import com.app.finder.domain.ForbiddenWord;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ForbiddenWord entity.
 */
public interface ForbiddenWordRepository extends JpaRepository<ForbiddenWord,Long> {

}
