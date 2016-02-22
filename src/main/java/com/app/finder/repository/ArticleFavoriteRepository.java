package com.app.finder.repository;

import com.app.finder.domain.ArticleFavorite;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ArticleFavorite entity.
 */
public interface ArticleFavoriteRepository extends JpaRepository<ArticleFavorite,Long> {

    @Query("select articleFavorite from ArticleFavorite articleFavorite where articleFavorite.user.login = ?#{principal.username}")
    List<ArticleFavorite> findByUserIsCurrentUser();

}
